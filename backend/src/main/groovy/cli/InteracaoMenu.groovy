package view

import controller.CandidatoController
import controller.EmpresaController
import controller.InteracaoController
import controller.VagaController
import model.Candidato

class InteracaoMenu {
    private final ConsoleInput input
    private final CandidatoController candidatoController
    private final EmpresaController empresaController
    private final VagaController vagaController
    private final InteracaoController interacaoController

    InteracaoMenu(
            ConsoleInput input,
            CandidatoController candidatoController,
            EmpresaController empresaController,
            VagaController vagaController,
            InteracaoController interacaoController
    ) {
        this.input = input
        this.candidatoController = candidatoController
        this.empresaController = empresaController
        this.vagaController = vagaController
        this.interacaoController = interacaoController
    }

    void curtirVaga() {
        println("\n--- MURAL DE VAGAS ---")
        listarVagasResumidas()

        println("\nPara curtir uma vaga, precisamos de o identificar.")
        int candidatoId = input.lerInteiro("Digite o seu ID de Candidato (0 para cancelar): ")
        if (candidatoId == 0) return

        if (!candidatoController.listar().any { it.id == candidatoId }) {
            println("\n[ERRO] Candidato com ID ${candidatoId} não encontrado no sistema.")
            return
        }

        int vagaId = input.lerInteiro("Digite o ID da Vaga que deseja curtir: ")
        if (!vagaController.listar().any { it.id == vagaId }) {
            println("\n[ERRO] Vaga com ID ${vagaId} não encontrada.")
            return
        }

        boolean matchEncontrado = interacaoController.curtirVaga(candidatoId, vagaId)

        println("\n[SUCESSO] Você curtiu a Vaga ID ${vagaId}!")

        if (matchEncontrado) {
            println("=====================================================")
            println("           PARABÉNS! TEMOS UM MATCH!   ")
            println(" A empresa dona desta vaga também já tinha curtido")
            println(" o seu perfil. Vocês já podem conversar!")
            println("=====================================================")
        }
    }

    void curtirCandidato() {
        println("\n--- BANCO DE TALENTOS (RECRUTAMENTO ÀS CEGAS) ---")
        List<Candidato> lista = candidatoController.listar()
        if (lista.isEmpty()) {
            println("Nenhum candidato disponível.")
            return
        }

        lista.each { c ->
            println("ID do Candidato: ${c.id}")
            println("Skills: ${c.competencias.collect { it.nome }.join(', ')}")
            println("Descrição: ${c.descricao}")
            println("-" * 40)
        }

        println("\nPara curtir um perfil, precisamos de identificar a sua empresa.")
        int empresaId = input.lerInteiro("Digite o seu ID de Empresa (0 para cancelar): ")
        if (empresaId == 0) return

        if (!empresaController.listar().any { it.id == empresaId }) {
            println("\n[ERRO] Empresa com ID ${empresaId} não encontrada.")
            return
        }

        int candidatoId = input.lerInteiro("Digite o ID do Candidato que deseja curtir: ")
        if (!lista.any { it.id == candidatoId }) {
            println("\n[ERRO] Candidato com ID ${candidatoId} não encontrado.")
            return
        }

        Integer vagaMatchId = interacaoController.curtirCandidato(empresaId, candidatoId)

        println("\n[SUCESSO] Você demonstrou interesse no Candidato ID ${candidatoId}!")

        if (vagaMatchId != null) {
            println("=====================================================")
            println("           MATCH CONFIRMADO!   ")
            println(" Este candidato já havia curtido a sua Vaga (ID: ${vagaMatchId}).")
            println(" O perfil completo do candidato foi desbloqueado.")
            println("=====================================================")
        }
    }

    private void listarVagasResumidas() {
        List lista = vagaController.listar()
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
}
