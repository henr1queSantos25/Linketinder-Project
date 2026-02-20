package view

import repository.Memoria

class Menu {
    Scanner scanner = new Scanner(System.in)

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
            case 0:
                println("\nEncerrando o sistema. Até logo!")
                break
            default:
                println("\n[AVISO] Opção não reconhecida. Tente novamente.")
        }
    }

    private void listarCandidatos() {
        println("\n--- Lista de Candidatos Pré-cadastrados ---")
        Memoria.candidatos.each { candidato ->
            println("Nome: ${candidato.nome} | Idade: ${candidato.idade} | Skills: ${candidato.competencias.join(', ')}")
            println("Descrição: ${candidato.descricao}")
            println("-" * 40)
        }
    }

    private void listarEmpresas() {
        println("\n--- Lista de Empresas Pré-cadastradas ---")
        Memoria.empresas.each { empresa ->
            println("Empresa: ${empresa.nome} | País: ${empresa.pais} | Buscando: ${empresa.competencias.join(', ')}")
            println("Descrição: ${empresa.descricao}")
            println("-" * 40)
        }
    }
}