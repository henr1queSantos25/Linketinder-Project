package api

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import controller.CandidatoController
import model.Candidato
import model.Competencia
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import java.time.LocalDate

class CandidatoHandler implements HttpHandler {
    private final CandidatoController controller

    CandidatoHandler(CandidatoController controller) {
        this.controller = controller
    }

    @Override
    void handle(HttpExchange exchange) {
        if (CorsHandler.handleOptions(exchange)) return
        CorsHandler.addCorsHeaders(exchange)

        try {
            if (exchange.requestMethod == "POST") {
                def json = new JsonSlurper().parse(exchange.getRequestBody())

                Candidato c = new Candidato(
                        nome: json.nome ?: "",
                        sobrenome: json.sobrenome ?: "",
                        email: json.email ?: "",
                        cpf: json.cpf ?: "",
                        senha: json.senha ?: "",
                        descricao: json.descricao ?: "",
                        pais: json.pais ?: "Brasil",
                        cep: json.cep ?: "",
                        dataNascimento: json.dataNascimento ? LocalDate.parse(json.dataNascimento) : LocalDate.now()
                )

                if (json.competencias && json.competencias instanceof List) {
                    c.competencias = json.competencias.collect { new Competencia(nome: it.toString().trim()) }
                }

                if (controller.salvar(c)) {
                    sendResponse(exchange, 201, [mensagem: "Candidato cadastrado com sucesso!"])
                } else {
                    sendResponse(exchange, 400, [erro: "Falha no cadastro. Verifique duplicidade de CPF ou E-mail."])
                }

            } else if (exchange.requestMethod == "GET") {
                def lista = controller.listar()
                def response = lista.collect { c ->
                    [
                            id: c.id,
                            descricao: c.descricao,
                            competencias: c.competencias.collect { it.nome }
                    ]
                }
                sendResponse(exchange, 200, response)

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