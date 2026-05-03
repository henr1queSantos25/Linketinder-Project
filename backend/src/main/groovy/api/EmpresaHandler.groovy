package api

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import controller.EmpresaController
import model.Empresa
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder

class EmpresaHandler implements HttpHandler {
    private final EmpresaController controller

    EmpresaHandler(EmpresaController controller) {
        this.controller = controller
    }

    @Override
    void handle(HttpExchange exchange) {
        if (CorsHandler.handleOptions(exchange)) return
        CorsHandler.addCorsHeaders(exchange)

        try {
            if (exchange.requestMethod == "POST") {
                def json = new JsonSlurper().parse(exchange.getRequestBody())

                Empresa e = new Empresa(
                        nome: json.nome ?: "",
                        cnpj: json.cnpj ?: "",
                        emailCorporativo: json.emailCorporativo ?: "",
                        senha: json.senha ?: "",
                        descricao: json.descricao ?: "",
                        pais: json.pais ?: "Brasil",
                        cep: json.cep ?: ""
                )

                if (controller.salvar(e)) {
                    sendResponse(exchange, 201, [mensagem: "Empresa cadastrada com sucesso!", id: e.id])
                } else {
                    sendResponse(exchange, 400, [erro: "Falha no cadastro. Verifique duplicidade de CNPJ ou E-mail."])
                }

            } else if (exchange.requestMethod == "GET") {
                def lista = controller.listar()
                def response = lista.collect { emp ->
                    [
                            id: emp.id,
                            descricao: emp.descricao
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
