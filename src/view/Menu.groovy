package view

import controller.CandidatoController
import controller.EmpresaController
import model.Candidato
import model.Empresa
import repository.Memoria

class Menu {
    Scanner scanner = new Scanner(System.in)

    CandidatoController candidatoController = new CandidatoController()
    EmpresaController empresaController = new EmpresaController()

    void iniciar() {
        int opcao = -1
        while (opcao != 0) {
            mostrarOpcoes()
            print("Escolha uma opção: ")

            try {
                opcao = scanner.nextLine().toInteger()
                processarOpcao(opcao)
            } catch (NumberFormatException e) {
                println("\n[ERRO] Entrada inválida. Por favor, digite apenas números.")
            }
        }
    }

    private void mostrarOpcoes() {
        println("\n==================================")
        println("       LINKETINDER - MVP          ")
        println("==================================")
        println("1. Listar Candidatos")
        println("2. Listar Empresas")
        println("3. Cadastrar Novo Candidato")
        println("4. Cadastrar Nova Empresa")
        println("0. Sair")
        println("==================================")
    }

    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                listarCandidatos()
                break
            case 2:
                listarEmpresas()
                break
            case 3:
                cadastrarCandidato()
                break
            case 4:
                cadastrarEmpresa()
                break
            case 0:
                println("\nEncerrando o sistema. Até logo!")
                break
            default:
                println("\n[AVISO] Opção não reconhecida. Tente novamente.")
        }
    }

    private void listarCandidatos() {
        println("\n--- Lista de Candidatos ---")
        Memoria.candidatos.each { candidato ->
            println("Nome: ${candidato.nome} | Idade: ${candidato.idade} | Skills: ${candidato.competencias.join(', ')}")
            println("Descrição: ${candidato.descricao}")
            println("-" * 40)
        }
    }

    private void listarEmpresas() {
        println("\n--- Lista de Empresas ---")
        Memoria.empresas.each { empresa ->
            println("Empresa: ${empresa.nome} | País: ${empresa.pais} | Buscando: ${empresa.competencias.join(', ')}")
            println("Descrição: ${empresa.descricao}")
            println("-" * 40)
        }
    }

    private void cadastrarCandidato() {
        println("\n--- Cadastro de Novo Candidato ---")
        print("Nome: ")
        String nome = scanner.nextLine()
        print("E-mail: ")
        String email = scanner.nextLine()
        print("CPF: ")
        String cpf = scanner.nextLine()

        int idade = 0
        while (true) {
            print("Idade: ")
            try {
                idade = scanner.nextLine().toInteger()
                break
            } catch (NumberFormatException e) {
                println("[ERRO] Por favor, digite um número inteiro para a idade.")
            }
        }

        print("Estado: ")
        String estado = scanner.nextLine()
        print("CEP: ")
        String cep = scanner.nextLine()
        print("Descrição pessoal: ")
        String descricao = scanner.nextLine()
        print("Competências (separe por vírgula, ex: Java, Python, SQL): ")
        String compStr = scanner.nextLine()
        List<String> competencias = compStr.split(',').collect { it.trim() }

        Candidato novoCandidato = new Candidato(
                nome: nome, email: email, cpf: cpf, idade: idade,
                estado: estado, cep: cep, descricao: descricao, competencias: competencias
        )

        if (candidatoController.salvar(novoCandidato)) {
            println("\n[SUCESSO] Candidato cadastrado com sucesso!")
        } else {
            println("\n[ERRO] Já existe um candidato cadastrado com este CPF!")
        }
    }

    private void cadastrarEmpresa() {
        println("\n--- Cadastro de Nova Empresa ---")
        print("Nome da Empresa: ")
        String nome = scanner.nextLine()
        print("E-mail corporativo: ")
        String email = scanner.nextLine()
        print("CNPJ: ")
        String cnpj = scanner.nextLine()
        print("País: ")
        String pais = scanner.nextLine()
        print("Estado: ")
        String estado = scanner.nextLine()
        print("CEP: ")
        String cep = scanner.nextLine()
        print("Descrição da empresa: ")
        String descricao = scanner.nextLine()
        print("Competências desejadas (separe por vírgula): ")
        String compStr = scanner.nextLine()

        List<String> competencias = compStr.split(',').collect { it.trim() }

        Empresa novaEmpresa = new Empresa(
                nome: nome, email: email, cnpj: cnpj, pais: pais,
                estado: estado, cep: cep, descricao: descricao, competencias: competencias
        )

        if (empresaController.salvar(novaEmpresa)) {
            println("\n[SUCESSO] Empresa cadastrada com sucesso!")
        } else {
            println("\n[ERRO] Já existe uma empresa cadastrada com este CNPJ!")
        }
    }
}