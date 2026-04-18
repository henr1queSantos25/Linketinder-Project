package controller

import dao.CandidatoDAO
import model.Candidato
import spock.lang.Specification
import java.time.LocalDate

class CandidatoControllerTest extends Specification {

    CandidatoController controller
    CandidatoDAO mockDao

    def setup() {
        mockDao = Mock(CandidatoDAO)
        controller = new CandidatoController()
        controller.dao = mockDao
    }

    def "deve retornar verdadeiro ao salvar um novo candidato com sucesso"() {
        given: "um candidato valido"
            def candidato = new Candidato(
                    nome: "João",
                    sobrenome: "Silva",
                    email: "joao@email.com",
                    cpf: "123.456.789-00",
                    dataNascimento: LocalDate.of(1995, 5, 20),
                    descricao: "Desenvolvedor Backend"
            )

        when: "o controller tentar salvar esse candidato"
            def resultado = controller.salvar(candidato)

        then: "o DAO eh chamado simulando sucesso no banco"
            1 * mockDao.salvar(candidato) >> true
            resultado == true
    }

    def "deve retornar falso ao ocorrer erro de persistencia no DAO"() {
        given: "um candidato tentando se cadastrar com dados problematicos"
            def candidatoDuplicado = new Candidato(nome: "José", cpf: "999.999.999-99")

        when: "o controller tentar salvar o candidato duplicado"
            def resultado = controller.salvar(candidatoDuplicado)

        then: "o DAO recusa a insercao e o controller lida corretamente retornando false"
            1 * mockDao.salvar(candidatoDuplicado) >> false
            resultado == false
    }
}