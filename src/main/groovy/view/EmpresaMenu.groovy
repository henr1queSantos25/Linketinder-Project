package view

import controller.EmpresaController
import model.Empresa

class EmpresaMenu {
    private final ConsoleInput input
    private final EmpresaController empresaController
    private final InteracaoMenu interacaoMenu

    EmpresaMenu(ConsoleInput input, EmpresaController empresaController, InteracaoMenu interacaoMenu) {
        this.input = input
        this.empresaController = empresaController
        this.interacaoMenu = interacaoMenu
    }

    void executar() {
        int opcao = -1
        while (opcao != 0) {
            println("\n--- MÓDULO: EMPRESAS ---")
            println("1. Listar Empresas")
            println("2. Cadastrar Empresa")
            println("3. Atualizar Empresa")
            println("4. Deletar Empresa")
            println("5. Buscar Candidatos")
            println("0. Voltar")

            opcao = input.lerInteiro("Escolha: ")
            switch (opcao) {
                case 1: listarEmpresas(); break
                case 2: cadastrarEmpresa(); break
                case 3: atualizarEmpresa(); break
                case 4: deletarEmpresa(); break
                case 5: interacaoMenu.curtirCandidato(); break
                case 0: break
                default: println("[AVISO] Opção inválida.")
            }
        }
    }

    private void listarEmpresas() {
        println("\n--- Lista de Empresas ---")
        List<Empresa> lista = empresaController.listar()
        if (lista.isEmpty()) {
            println("Nenhuma empresa encontrada.")
            return
        }

        lista.each { e ->
            println("ID: ${e.id} | Empresa: ${e.nome} | CNPJ: ${e.cnpj} | E-mail: ${e.emailCorporativo}")
            println("-" * 40)
        }
    }

    private void cadastrarEmpresa() {
        println("\n--- Nova Empresa ---")
        Empresa e = preencherDadosEmpresa(new Empresa())
        if (empresaController.salvar(e)) {
            println("[SUCESSO] Empresa salva!")
        } else {
            println("[ERRO] Falha ao salvar (Verifique duplicidade).")
        }
    }

    private void atualizarEmpresa() {
        listarEmpresas()
        int id = input.lerInteiro("\nDigite o ID da empresa a atualizar (0 para cancelar): ")
        if (id == 0) return

        if (!empresaController.listar().any { it.id == id }) {
            println("\n[ERRO] Operação cancelada: ID ${id} não existe.")
            return
        }

        println("--- Preencha os novos dados ---")
        Empresa e = preencherDadosEmpresa(new Empresa(id: id))
        if (empresaController.atualizar(e)) {
            println("[SUCESSO] Empresa atualizada!")
        } else {
            println("[ERRO] Falha ao atualizar.")
        }
    }

    private void deletarEmpresa() {
        listarEmpresas()
        int id = input.lerInteiro("\nDigite o ID da empresa para deletar (0 para cancelar): ")
        if (id == 0) return

        if (!empresaController.listar().any { it.id == id }) {
            println("\n[ERRO] Operação cancelada: ID ${id} não existe.")
            return
        }

        if (empresaController.deletar(id)) {
            println("[SUCESSO] Empresa deletada!")
        } else {
            println("[ERRO] Falha ao deletar.")
        }
    }

    private Empresa preencherDadosEmpresa(Empresa e) {
        e.nome = input.lerString("Nome: ")
        e.cnpj = input.lerString("CNPJ: ")
        e.emailCorporativo = input.lerString("E-mail Corporativo: ")
        e.pais = input.lerString("País: ")
        e.cep = input.lerString("CEP: ")
        e.descricao = input.lerString("Descrição: ")
        e.senha = input.lerString("Senha: ")
        return e
    }
}
