package controller

import model.Candidato
import repository.Memoria
import spock.lang.Specification

class CandidatoControllerTest extends Specification {

    CandidatoController controller

    def setup() {
        controller = new CandidatoController()
        Memoria.candidatos.clear()
    }

    def "deve salvar um novo candidato com sucesso quando o CPF for unico"() {
        given: "um candidato valido com um cpf que ainda nao existe"
        def candidato = new Candidato(
                nome: "João Silva",
                email: "joao@email.com",
                cpf: "123.456.789-00",
                idade: 25,
                estado: "SP",
                cep: "01000-000",
                descricao: "Desenvolvedor Backend",
                competencias: ["Java", "Groovy"]
        )

        when: "o controller tentar salvar esse candidato"
        def resultado = controller.salvar(candidato)

        then: "o retorno deve ser verdadeiro (true)"
        resultado == true

        and: "o candidato deve ter sido adicionado na lista da memoria"
        Memoria.candidatos.size() == 1
        Memoria.candidatos[0].cpf == "123.456.789-00"
    }

    def "nao deve salvar um candidato se o CPF ja estiver cadastrado"() {
        given: "que ja existe um candidato salvo na memoria"
        def candidatoExistente = new Candidato(nome: "Maria", cpf: "999.999.999-99")
        controller.salvar(candidatoExistente)

        and: "um novo candidato tentando se cadastrar com o mesmo CPF"
        def candidatoDuplicado = new Candidato(nome: "José", cpf: "999.999.999-99")

        when: "o controller tentar salvar o candidato duplicado"
        def resultado = controller.salvar(candidatoDuplicado)

        then: "o retorno deve ser falso (false)"
        resultado == false

        and: "a lista da memoria deve continuar contendo apenas o primeiro candidato"
        Memoria.candidatos.size() == 1
        Memoria.candidatos[0].nome == "Maria"
    }
}