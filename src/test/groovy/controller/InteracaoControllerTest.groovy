package controller

import dao.InteracaoDAO
import spock.lang.Specification

class InteracaoControllerTest extends Specification {

    InteracaoController controller
    InteracaoDAO mockDao

    def setup() {
        mockDao = Mock(InteracaoDAO)
        controller = new InteracaoController()
        controller.dao = mockDao
    }

    def "deve retornar falso se o candidato curtir uma vaga e nao houver match"() {
        when: "o candidato 1 curte a vaga 10"
            boolean deuMatch = controller.curtirVaga(1, 10)

        then: "o DAO regista a curtida mas retorna false pois a empresa dona da vaga nao curtiu o candidato"
            1 * mockDao.curtirVaga(1, 10) >> false
            deuMatch == false
    }

    def "deve retornar verdadeiro se o candidato curtir uma vaga e gerar match"() {
        when: "o candidato curte a vaga"
            boolean deuMatch = controller.curtirVaga(2, 20)

        then: "o DAO deteta reciprocidade, consolida o match e retorna true"
            1 * mockDao.curtirVaga(2, 20) >> true
            deuMatch == true
    }

    def "deve retornar null se a empresa curtir um candidato e nao houver match"() {
        when: "a empresa 5 curte o candidato 1"
            Integer vagaMatchId = controller.curtirCandidato(5, 1)

        then: "o DAO regista a curtida, mas retorna null pois o candidato nao havia curtido nenhuma vaga dessa empresa"
            1 * mockDao.curtirCandidato(5, 1) >> null
            vagaMatchId == null
    }

    def "deve retornar o id da vaga se a empresa curtir um candidato e gerar match"() {
        when: "a empresa 3 curte o candidato 2"
            def vagaMatchId = controller.curtirCandidato(3, 2)

        then: "o DAO deteta a reciprocidade e devolve o ID da vaga (ex: 15) que o candidato havia curtido antes"
            1 * mockDao.curtirCandidato(3, 2) >> 15
            vagaMatchId == 15
    }
}