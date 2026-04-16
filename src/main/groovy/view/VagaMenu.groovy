package view

import controller.EmpresaController
import controller.VagaController
import model.Competencia
import model.Vaga

class VagaMenu {
    private final ConsoleInput input
    private final VagaController vagaController
    private final EmpresaController empresaController

    VagaMenu(ConsoleInput input, VagaController vagaController, EmpresaController empresaController) {
        this.input = input
        this.vagaController = vagaController
        this.empresaController = empresaController
    }

    void executar() {
        int opcao = -1
        while (opcao != 0) {
            println("\n--- MÓDULO: VAGAS ---")
            println("1. Listar Vagas")
            println("2. Publicar Vaga")
            println("3. Atualizar Vaga")
            println("4. Deletar Vaga")
            println("0. Voltar")

            opcao = input.lerInteiro("Escolha: ")
            switch (opcao) {
                case 1: listarVagas(); break
                case 2: cadastrarVaga(); break
                case 3: atualizarVaga(); break
                case 4: deletarVaga(); break
                case 0: break
                default: println("[AVISO] Opção inválida.")
            }
        }
    }

    private void listarVagas() {
        println("\n--- Lista de Vagas ---")
        List<Vaga> lista = vagaController.listar()
        if (lista.isEmpty()) {
            println("Nenhuma vaga encontrada.")
            return
        }

        lista.each { v ->
            println("ID: ${v.id} | Título: ${v.nome} | Local: ${v.local}")
            println("Descrição: ${v.descricao}")
            println("Skills exigidas: ${v.competencias.collect { it.nome }.join(', ')}")
            println("-" * 40)
        }
    }

    private void cadastrarVaga() {
        println("\n--- Publicar Nova Vaga ---")
        Vaga v = preencherDadosVaga(new Vaga())

        if (!empresaController.listar().any { it.id == v.empresaId }) {
            println("\n[ERRO] Operação cancelada: A Empresa com ID ${v.empresaId} não existe.")
            return
        }

        if (vagaController.salvar(v)) {
            println("[SUCESSO] Vaga salva!")
        } else {
            println("[ERRO] Falha ao salvar a vaga.")
        }
    }

    private void atualizarVaga() {
        listarVagas()
        int id = input.lerInteiro("\nDigite o ID da vaga a atualizar (0 para cancelar): ")
        if (id == 0) return

        if (!vagaController.listar().any { it.id == id }) {
            println("\n[ERRO] Operação cancelada: ID ${id} não existe.")
            return
        }

        println("--- Preencha os novos dados ---")
        Vaga v = preencherDadosVaga(new Vaga(id: id))

        if (!empresaController.listar().any { it.id == v.empresaId }) {
            println("\n[ERRO] Operação cancelada: A Empresa com ID ${v.empresaId} não existe.")
            return
        }

        if (vagaController.atualizar(v)) {
            println("[SUCESSO] Vaga atualizada!")
        } else {
            println("[ERRO] Falha ao atualizar.")
        }
    }

    private void deletarVaga() {
        listarVagas()
        int id = input.lerInteiro("\nDigite o ID da vaga para deletar (0 para cancelar): ")
        if (id == 0) return

        if (!vagaController.listar().any { it.id == id }) {
            println("\n[ERRO] Operação cancelada: ID ${id} não existe.")
            return
        }

        if (vagaController.deletar(id)) {
            println("[SUCESSO] Vaga deletada!")
        } else {
            println("[ERRO] Falha ao deletar.")
        }
    }

    private Vaga preencherDadosVaga(Vaga v) {
        v.empresaId = input.lerInteiro("ID da Empresa dona da vaga: ")
        v.nome = input.lerString("Título da Vaga: ")
        v.local = input.lerString("Local (Ex: Remoto, SP): ")
        v.descricao = input.lerString("Descrição detalhada: ")

        String compStr = input.lerString("Competências exigidas (separadas por vírgula): ")
        List<String> nomes = compStr.split(',').collect { it.trim() }.findAll { it != "" }
        v.competencias = nomes.collect { new Competencia(nome: it) }
        return v
    }
}
