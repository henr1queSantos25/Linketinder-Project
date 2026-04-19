package controller

import dao.EmpresaDAO
import dao.ICrudDAO
import model.Empresa
import spock.lang.Specification

class EmpresaControllerTest extends Specification {

    EmpresaController controller
    ICrudDAO mockDao

    def setup() {
        mockDao = Mock(ICrudDAO)
        controller = new EmpresaController(mockDao)
    }

    def "deve retornar verdadeiro ao salvar uma nova empresa com sucesso"() {
        given:
            def empresa = new Empresa(
                    nome: "Tech Inovações",
                    emailCorporativo: "contato@techinovacoes.com",
                    cnpj: "12.345.678/0001-90",
                    pais: "Brasil",
                    cep: "88000-000",
                    descricao: "Focada em tecnologia de ponta"
            )

        when: "tentar salvar a empresa"
            def resultado = controller.salvar(empresa)

        then: "o DAO deve ser chamado 1 vez e o controller deve retornar true"
            1 * mockDao.salvar(empresa) >> true
            resultado == true
    }

    def "deve retornar falso ao falhar na gravacao da empresa"() {
        given: "uma empresa que vai causar erro (ex: CNPJ duplicado)"
            def empresaDuplicada = new Empresa(nome: "Global Corp Filial", cnpj: "00.000.000/0001-00")

        when: "tentar salvar a empresa"
            def resultado = controller.salvar(empresaDuplicada)

        then: "o DAO deve retornar falso e o controller repassa a falha"
            1 * mockDao.salvar(empresaDuplicada) >> false
            resultado == false
    }
}