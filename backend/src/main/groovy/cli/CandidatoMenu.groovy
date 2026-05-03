package view

import controller.CandidatoController
import model.Candidato
import model.Competencia

class CandidatoMenu {
    private final ConsoleInput input
    private final CandidatoController candidatoController
    private final InteracaoMenu interacaoMenu

    CandidatoMenu(ConsoleInput input, CandidatoController candidatoController, InteracaoMenu interacaoMenu) {
        this.input = input
        this.candidatoController = candidatoController
        this.interacaoMenu = interacaoMenu
    }

    void executar() {
        int opcao = -1
        while (opcao != 0) {
            println("\n--- MÓDULO: CANDIDATOS ---")
            println("1. Listar Candidatos")
            println("2. Cadastrar Candidato")
            println("3. Atualizar Candidato")
            println("4. Deletar Candidato")
            println("5. Explorar Vagas")
            println("0. Voltar")

            opcao = input.lerInteiro("Escolha: ")
            switch (opcao) {
                case 1: listarCandidatos(); break
                case 2: cadastrarCandidato(); break
                case 3: atualizarCandidato(); break
                case 4: deletarCandidato(); break
                case 5: interacaoMenu.curtirVaga(); break
                case 0: break
                default: println("[AVISO] Opção inválida.")
            }
        }
    }

    private void listarCandidatos() {
        println("\n--- Lista de Candidatos ---")
        List<Candidato> lista = candidatoController.listar()
        if (lista.isEmpty()) {
            println("Nenhum candidato encontrado.")
            return
        }

        lista.each { c ->
            println("ID: ${c.id} | Nome: ${c.nome} ${c.sobrenome} | E-mail: ${c.email}")
            println("Skills: ${c.competencias.collect { it.nome }.join(', ')}")
            println("-" * 40)
        }
    }

    private void cadastrarCandidato() {
        println("\n--- Novo Candidato ---")
        Candidato c = preencherDadosCandidato(new Candidato())
        if (candidatoController.salvar(c)) {
            println("[SUCESSO] Candidato salvo!")
        } else {
            println("[ERRO] Falha ao salvar (Verifique duplicidade).")
        }
    }

    private void atualizarCandidato() {
        listarCandidatos()
        int id = input.lerInteiro("\nDigite o ID do candidato a atualizar (0 para cancelar): ")
        if (id == 0) return

        if (!candidatoController.listar().any { it.id == id }) {
            println("\n[ERRO] Operação cancelada: ID ${id} não existe.")
            return
        }

        println("--- Preencha os novos dados ---")
        Candidato c = preencherDadosCandidato(new Candidato(id: id))
        if (candidatoController.atualizar(c)) {
            println("[SUCESSO] Candidato atualizado!")
        } else {
            println("[ERRO] Falha ao atualizar.")
        }
    }

    private void deletarCandidato() {
        listarCandidatos()
        int id = input.lerInteiro("\nDigite o ID do candidato para deletar (0 para cancelar): ")
        if (id == 0) return

        if (!candidatoController.listar().any { it.id == id }) {
            println("\n[ERRO] Operação cancelada: ID ${id} não existe.")
            return
        }

        if (candidatoController.deletar(id)) {
            println("[SUCESSO] Candidato deletado!")
        } else {
            println("[ERRO] Falha ao deletar.")
        }
    }

    private Candidato preencherDadosCandidato(Candidato c) {
        c.nome = input.lerString("Nome: ")
        c.sobrenome = input.lerString("Sobrenome: ")
        c.dataNascimento = input.lerData("Data de Nascimento (AAAA-MM-DD): ")
        c.email = input.lerString("E-mail: ")
        c.cpf = input.lerString("CPF: ")
        c.pais = input.lerString("País: ")
        c.cep = input.lerString("CEP: ")
        c.descricao = input.lerString("Descrição: ")
        c.senha = input.lerString("Senha: ")

        String compStr = input.lerString("Competências (separadas por vírgula): ")
        List<String> nomes = compStr.split(',').collect { it.trim() }.findAll { it != "" }
        c.competencias = nomes.collect { new Competencia(nome: it) }
        return c
    }
}
