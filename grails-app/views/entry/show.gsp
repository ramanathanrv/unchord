
<%@ page import="unchord.Entry" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'entry.label', default: 'Entry')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: entryInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.linkToAsset.label" default="Link To Asset" /></td>

                            <g:if test="${entryInstance.linkToAsset}">
                                <td valign="top" class="value">
                                    <a class="media" href="${entryInstance.linkToAsset}">Play me</a>
                                </td>
                            </g:if>
                            <g:else>
                                <td valign="top" class="value">${fieldValue(bean: entryInstance, field: "linkToAsset")}</td>
                            </g:else>

                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.notes.label" default="Notes" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: entryInstance, field: "notes")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="entry.state.label" default="State" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: entryInstance, field: "state")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${entryInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
