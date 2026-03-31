package view

import controller.CandidatoController
import controller.EmpresaController
import controller.VagaController
import model.Candidato
import model.Competencia
import model.Empresa
import model.Vaga

import java.time.LocalDate
import java.time.format.DateTimeParseException

class Menu {
    private Scanner scanner = new Scanner(System.in)
    private CandidatoController candidatoController = new CandidatoController()
    private EmpresaController empresaController = new EmpresaController()
    private VagaController vagaController = new VagaController()

    void iniciar() {
        int opcao = -1
        while (opcao != 0) {
            println("\n==================================")
            println("       LINKETINDER - MVP          ")
            println("==================================")
            println("1. Módulo de Candidatos")
            println("2. Módulo de Empresas")
            println("3. Módulo de Vagas")
            println("0. Sair")
            println("==================================")

            opcao = lerInteiro("Escolha uma opção: ")

            switch (opcao) {
                case 1: menuCandidato(); break
                case 2: menuEmpresa(); break
                case 3: menuVaga(); break
                case 0: println("\nEncerrando o sistema. Até logo!"); break
                default: println("\n[AVISO] Opção não reconhecida.")
            }
        }
    }


    private void menuCandidato() {
        int opcao = -1
        while (opcao != 0) {
            println("\n--- MÓDULO: CANDIDATOS ---")
            println("1. Listar Candidatos")
            println("2. Cadastrar Candidato")
            println("3. Atualizar Candidato")
            println("4. Deletar Candidato")
            println("0. Voltar")

            opcao = lerInteiro("Escolha: ")
            switch (opcao) {
                case 1: listarCandidatos(); break
                case 2: cadastrarCandidato(); break
                case 3: atualizarCandidato(); break
                case 4: deletarCandidato(); break
                case 0: break
                default: println("[AVISO] Opção inválida.")
            }
        }
    }

    private void menuEmpresa() {
        int opcao = -1
        while (opcao != 0) {
            println("\n--- MÓDULO: EMPRESAS ---")
            println("1. Listar Empresas")
            println("2. Cadastrar Empresa")
            println("3. Atualizar Empresa")
            println("4. Deletar Empresa")
            println("0. Voltar")

            opcao = lerInteiro("Escolha: ")
            switch (opcao) {
                case 1: listarEmpresas(); break
                case 2: cadastrarEmpresa(); break
                case 3: atualizarEmpresa(); break
                case 4: deletarEmpresa(); break
                case 0: break
                default: println("[AVISO] Opção inválida.")
            }
        }
    }

    private void menuVaga() {
        int opcao = -1
        while (opcao != 0) {
            println("\n--- MÓDULO: VAGAS ---")
            println("1. Listar Vagas")
            println("2. Publicar Vaga")
            println("3. Atualizar Vaga")
            println("4. Deletar Vaga")
            println("0. Voltar")

            opcao = lerInteiro("Escolha: ")
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


    // =========================================================
    // OPERAÇÕES: CANDIDATO
    // =========================================================
    private void listarCandidatos() {
        println("\n--- Lista de Candidatos ---")
        List<Candidato> lista = candidatoController.listar()
        if (lista.isEmpty()) {
            println("Nenhum candidato encontrado.");
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
        int id = lerInteiro("\nDigite o ID do candidato a atualizar (0 para cancelar): ")
        if (id == 0) return

        println("--- Preencha os novos dados ---")
        Candidato c = preencherDadosCandidato(new Candidato(id: id))
        if (candidatoController.atualizar(c)) {
            println("[SUCESSO] Candidato atualizado!")
        } else {
            println("[ERRO] Falha ao atualizar.")
        }
    }

    private void deletarCandidato() {
        int id = lerInteiro("\nDigite o ID do candidato para deletar: ")
        if (candidatoController.deletar(id)) {
            println("[SUCESSO] Candidato deletado!")
        } else {
            println("[ERRO] Falha ao deletar (ID não encontrado).")
        }
    }

    private Candidato preencherDadosCandidato(Candidato c) {
        c.nome = lerString("Nome: ")
        c.sobrenome = lerString("Sobrenome: ")
        c.dataNascimento = lerData("Data de Nascimento (AAAA-MM-DD): ")
        c.email = lerString("E-mail: ")
        c.cpf = lerString("CPF: ")
        c.pais = lerString("País: ")
        c.cep = lerString("CEP: ")
        c.descricao = lerString("Descrição: ")
        c.senha = lerString("Senha: ")

        String compStr = lerString("Competências (separadas por vírgula): ")
        List<String> nomes = compStr.split(',').collect { it.trim() }.findAll { it != "" }
        c.competencias = nomes.collect { new Competencia(nome: it) }
        return c
    }


    // =========================================================
    // OPERAÇÕES: EMPRESA
    // =========================================================
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
        int id = lerInteiro("\nDigite o ID da empresa a atualizar (0 para cancelar): ")
        if (id == 0) return

        println("--- Preencha os novos dados ---")
        Empresa e = preencherDadosEmpresa(new Empresa(id: id))
        if (empresaController.atualizar(e)) {
            println("[SUCESSO] Empresa atualizada!")
        } else {
            println("[ERRO] Falha ao atualizar.")
        }
    }

    private void deletarEmpresa() {
        int id = lerInteiro("\nDigite o ID da empresa para deletar: ")
        if (empresaController.deletar(id)) {
            println("[SUCESSO] Empresa deletada!")
        } else {
            println("[ERRO] Falha ao deletar (ID não encontrado ou possui vagas atreladas).")
        }
    }

    private Empresa preencherDadosEmpresa(Empresa e) {
        e.nome = lerString("Nome: ")
        e.cnpj = lerString("CNPJ: ")
        e.emailCorporativo = lerString("E-mail Corporativo: ")
        e.pais = lerString("País: ")
        e.cep = lerString("CEP: ")
        e.descricao = lerString("Descrição: ")
        e.senha = lerString("Senha: ")
        return e
    }

    // =========================================================
    // OPERAÇÕES: VAGA
    // =========================================================
    private void listarVagas() {
        println("\n--- Lista de Vagas ---")
        List<Vaga> lista = vagaController.listar()
        if (lista.isEmpty()) {
            println("Nenhuma vaga encontrada."); return
        }

        lista.each { v ->
            println("ID: ${v.id} | Empresa ID: ${v.empresaId} | Título: ${v.nome} | Local: ${v.local}")
            println("Descrição: ${v.descricao}")
            println("Skills exigidas: ${v.competencias.collect { it.nome }.join(', ')}")
            println("-" * 40)
        }
    }

    private void cadastrarVaga() {
        println("\n--- Publicar Nova Vaga ---")
        Vaga v = preencherDadosVaga(new Vaga())
        if (vagaController.salvar(v)) {
            println("[SUCESSO] Vaga salva!")
        } else {
            println("[ERRO] Falha ao salvar (Verifique se o ID da Empresa existe).")
        }
    }

    private void atualizarVaga() {
        listarVagas()
        int id = lerInteiro("\nDigite o ID da vaga a atualizar (0 para cancelar): ")
        if (id == 0) return

        println("--- Preencha os novos dados ---")
        Vaga v = preencherDadosVaga(new Vaga(id: id))
        if (vagaController.atualizar(v)) {
            println("[SUCESSO] Vaga atualizada!")
        } else {
            println("[ERRO] Falha ao atualizar.")
        }
    }

    private void deletarVaga() {
        int id = lerInteiro("\nDigite o ID da vaga para deletar: ")
        if (vagaController.deletar(id)) {
            println("[SUCESSO] Vaga deletada!")
        } else {
            println("[ERRO] Falha ao deletar (ID não encontrado).")
        }
    }

    private Vaga preencherDadosVaga(Vaga v) {
        v.empresaId = lerInteiro("ID da Empresa dona da vaga: ")
        v.nome = lerString("Título da Vaga: ")
        v.local = lerString("Local (Ex: Remoto, SP): ")
        v.descricao = lerString("Descrição detalhada: ")

        String compStr = lerString("Competências exigidas (separadas por vírgula): ")
        List<String> nomes = compStr.split(',').collect { it.trim() }.findAll { it != "" }
        v.competencias = nomes.collect { new Competencia(nome: it) }
        return v
    }

    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================
    private String lerString(String mensagem) {
        print(mensagem)
        return scanner.nextLine().trim()
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            print(mensagem)
            try {
                return scanner.nextLine().toInteger()
            } catch (NumberFormatException e) {
                println("[ERRO] Por favor, digite apenas números válidos.")
            }
        }
    }

    private LocalDate lerData(String mensagem) {
        while (true) {
            print(mensagem)
            try {
                return LocalDate.parse(scanner.nextLine().trim())
            } catch (DateTimeParseException e) {
                println("[ERRO] Formato inválido. Utilize o padrão AAAA-MM-DD.")
            }
        }
    }
}