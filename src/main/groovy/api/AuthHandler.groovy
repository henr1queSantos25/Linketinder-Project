package api

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import controller.CandidatoController
import controller.EmpresaController
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder

class AuthHandler implements HttpHandler {
    private final CandidatoController candidatoCtrl
    private final EmpresaController empresaCtrl

    AuthHandler(CandidatoController candidatoCtrl, EmpresaController empresaCtrl) {
        this.candidatoCtrl = candidatoCtrl
        this.empresaCtrl = empresaCtrl
    }

    @Override
    void handle(HttpExchange exchange) {
        if (CorsHandler.handleOptions(exchange)) return
        CorsHandler.addCorsHeaders(exchange)

        try {
            if (exchange.requestMethod == "POST") {
                def json = new JsonSlurper().parse(exchange.getRequestBody())
                String email = json.email
                String senha = json.senha
                String tipo = json.tipo

                Object usuario = null

                if (tipo == "CANDIDATO") {
                    usuario = candidatoCtrl.listar().find { it.email == email && it.senha == senha }
                } else if (tipo == "EMPRESA") {
                    usuario = empresaCtrl.listar().find { it.emailCorporativo == email && it.senha == senha }
                }

                if (usuario) {
                    sendResponse(exchange, 200, [mensagem: "Login realizado com sucesso", id: usuario.id, nome: usuario.nome, tipo: tipo])
                } else {
                    sendResponse(exchange, 401, [erro: "Credenciais inválidas"])
                }
            } else {
                sendResponse(exchange, 405, [erro: "Método não permitido"])
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