package api

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import controller.VagaController
import model.Vaga
import model.Competencia
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder

class VagaHandler implements HttpHandler {
    private final VagaController controller

    VagaHandler(VagaController controller) {
        this.controller = controller
    }

    @Override
    void handle(HttpExchange exchange) {
        if (CorsHandler.handleOptions(exchange)) return
        CorsHandler.addCorsHeaders(exchange)

        try {
            if (exchange.requestMethod == "POST") {
                def json = new JsonSlurper().parse(exchange.getRequestBody())

                Vaga v = new Vaga(
                        empresaId: (json.empresaId ?: 0) as Integer,
                        nome: json.nome ?: "",
                        descricao: json.descricao ?: "",
                        local: json.local ?: ""
                )

                if (json.competencias && json.competencias instanceof List) {
                    v.competencias = json.competencias.collect { new Competencia(nome: it.toString().trim()) }
                }

                if (controller.salvar(v)) {
                    sendResponse(exchange, 201, [mensagem: "Vaga cadastrada com sucesso!", id: v.id])
                } else {
                    sendResponse(exchange, 400, [erro: "Falha no cadastro da vaga."])
                }

            } else if (exchange.requestMethod == "GET") {
                def lista = controller.listar()
                def response = lista.collect { v ->
                    [
                            id: v.id,
                            nome: v.nome,
                            descricao: v.descricao,
                            local: v.local,
                            competencias: v.competencias.collect { it.nome }
                    ]
                }
                sendResponse(exchange, 200, response)

            } else {
                sendResponse(exchange, 405, [erro: "Metodo nao permitido"])
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, [erro: "Erro no servidor: ${e.message}"])
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, Object responseObj) {
        String response = new JsonBuilder(responseObj).toString()
        exchange.responseHeaders.add("Content-Type", "application/json")
        byte[] bytes = response.getBytes("UTF-8")
        exchange.sendResponseHeaders(statusCode, bytes.length)
        exchange.getResponseBody().write(bytes)
        exchange.close()
    }
}
