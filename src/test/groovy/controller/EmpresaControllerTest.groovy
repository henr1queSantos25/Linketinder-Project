package controller

import model.Empresa
import repository.Memoria
import spock.lang.Specification

class EmpresaControllerTest extends Specification {

    EmpresaController controller

    def setup() {
        controller = new EmpresaController()
        Memoria.empresas.clear()
    }

    def "deve salvar uma nova empresa com sucesso quando o CNPJ for unico"() {
        given: "uma empresa valida com cnpj novo"
        def empresa = new Empresa(
                nome: "Tech Inovações",
                email: "contato@techinovacoes.com",
                cnpj: "12.345.678/0001-90",
                pais: "Brasil",
                estado: "SC",
                cep: "88000-000",
                descricao: "Focada em tecnologia de ponta",
                competencias: ["Groovy", "Spring"]
        )

        when: "tentar salvar a empresa"
        def resultado = controller.salvar(empresa)

        then: "o retorno deve ser verdadeiro"
        resultado == true

        and: "a empresa deve estar na memoria"
        Memoria.empresas.size() == 1
        Memoria.empresas[0].cnpj == "12.345.678/0001-90"
    }

    def "nao deve salvar uma empresa se o CNPJ ja estiver cadastrado"() {
        given: "uma empresa ja existente na memoria"
        def empresaExistente = new Empresa(nome: "Global Corp", cnpj: "00.000.000/0001-00")
        controller.salvar(empresaExistente)

        and: "uma nova empresa com o mesmo CNPJ"
        def empresaDuplicada = new Empresa(nome: "Global Corp Filial", cnpj: "00.000.000/0001-00")

        when: "tentar salvar a empresa duplicada"
        def resultado = controller.salvar(empresaDuplicada)

        then: "o retorno deve ser falso"
        resultado == false

        and: "a memoria nao deve adicionar a duplicata"
        Memoria.empresas.size() == 1
        Memoria.empresas[0].nome == "Global Corp"
    }
}