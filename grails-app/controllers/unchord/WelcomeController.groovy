package unchord

class WelcomeController {

    def index = {
        redirect(controller: 'welcome',action: 'tests')
    }

    def tests = {
        def ts = Exercise.list()
        render(view: 'testList',model:[tests: ts])
    }

    def showTest = {
        def test = Exercise.get(params.id)
        render(view: 'testDetail',model:[test: test])
    }
}
