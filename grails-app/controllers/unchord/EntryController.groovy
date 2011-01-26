package unchord

import org.jfugue.Player
import org.jfugue.Pattern
import org.grails.s3.S3Asset
import org.grails.s3.S3AssetService

class EntryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [entryInstanceList: Entry.list(params), entryInstanceTotal: Entry.count()]
    }

    def create = {
        def entryInstance = new Entry()
        entryInstance.properties = params
        return [entryInstance: entryInstance]
    }

    S3AssetService s3AssetService
    def createAsset(Entry entryInstance) {
        //we have to make an mp3 file out of it
        Player player = new Player();
        Pattern pattern = new Pattern(entryInstance.notes)
        player.saveMidi(pattern, new File("/tmp/sample.mid"))
        def a = new S3Asset()
        a.bucket = "unchord_midi_files"
        a.mimeType = "audio/mid"
        a.newFile(new File("/tmp/sample.mid"),[addAsync: false])
        s3AssetService.put(a)
        println "Saving file"
        println a.url()
        entryInstance.linkToAsset = a.url()
        return entryInstance
    }

    def save = {
        def entryInstance = new Entry(params)
        if (entryInstance.save(flush: true)) {
            entryInstance = createAsset(entryInstance)
            println "We are here"
            entryInstance.save(flush:true)
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'entry.label', default: 'Entry'), entryInstance.id])}"
            redirect(action: "show", id: entryInstance.id)
        }
        else {
            render(view: "create", model: [entryInstance: entryInstance])
        }
    }

    def show = {
        def entryInstance = Entry.get(params.id)
        if (!entryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'entry.label', default: 'Entry'), params.id])}"
            redirect(action: "list")
        }
        else {
            [entryInstance: entryInstance]
        }
    }

    def edit = {
        def entryInstance = Entry.get(params.id)
        if (!entryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'entry.label', default: 'Entry'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [entryInstance: entryInstance]
        }
    }

    def update = {
        def entryInstance = Entry.get(params.id)
        if (entryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (entryInstance.version > version) {
                    
                    entryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'entry.label', default: 'Entry')] as Object[], "Another user has updated this Entry while you were editing")
                    render(view: "edit", model: [entryInstance: entryInstance])
                    return
                }
            }
            entryInstance.properties = params
            if (!entryInstance.hasErrors() && entryInstance.save(flush: true)) {
                createAsset(entryInstance)
                println "Updating s3 asset as well"
                entryInstance.save(flush:true)
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'entry.label', default: 'Entry'), entryInstance.id])}"
                redirect(action: "show", id: entryInstance.id)
            }
            else {
                render(view: "edit", model: [entryInstance: entryInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'entry.label', default: 'Entry'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def entryInstance = Entry.get(params.id)
        if (entryInstance) {
            try {
                entryInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'entry.label', default: 'Entry'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'entry.label', default: 'Entry'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'entry.label', default: 'Entry'), params.id])}"
            redirect(action: "list")
        }
    }
}
