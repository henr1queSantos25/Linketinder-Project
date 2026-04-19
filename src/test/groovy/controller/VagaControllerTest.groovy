package controller

import dao.ICrudDAO
import dao.VagaDAO
import model.Competencia
import model.Vaga
import spock.lang.Specification

class VagaControllerTest extends Specification {

    VagaController controller
    ICrudDAO mockDao

    def setup() {
        mockDao = Mock(ICrudDAO)
        controller = new VagaController(mockDao)
    }

    def "deve retornar verdadeiro ao salvar uma vaga com sucesso"() {
        given:
            def vaga = new Vaga(
                    empresaId: 1,
                    nome: "Desenvolvedor Groovy",
                    local: "Remoto",
                    descricao: "Vaga para atuar na refatoração de sistemas",
                    competencias: [new Competencia(nome: "Groovy"), new Competencia(nome: "SQL")]
            )

        when: "o controller tenta salvar"
            def resultado = controller.salvar(vaga)

        then: "o DAO eh chamado simulando sucesso no banco"
            1 * mockDao.salvar(vaga) >> true
            resultado == true
    }

    def "deve retornar a lista de vagas ativas no sistema"() {
        given: "uma lista mockada simulando o retorno do banco"
            def vagaMock = new Vaga(id: 10, nome: "Engenheiro de Dados")

        when: "solicitada a listagem"
            def lista = controller.listar()

        then: "o DAO retorna a lista corretamente"
            1 * mockDao.listar() >> [vagaMock]
            lista.size() == 1
            lista[0].nome == "Engenheiro de Dados"
    }

    def "deve retornar verdadeiro ao atualizar uma vaga"() {
        given:
            def vagaModificada = new Vaga(id: 5, nome: "Desenvolvedor Backend Sênior")

        when: "o controller tenta atualizar"
            def resultado = controller.atualizar(vagaModificada)

        then: "o DAO executa a atualizacao com sucesso"
            1 * mockDao.atualizar(vagaModificada) >> true
            resultado == true
    }

    def "deve retornar verdadeiro ao deletar uma vaga existente"() {
        when:
            def resultado = controller.deletar(2)

        then: "o DAO executa o delete com sucesso"
            1 * mockDao.deletar(2) >> true
            resultado == true
    }
}