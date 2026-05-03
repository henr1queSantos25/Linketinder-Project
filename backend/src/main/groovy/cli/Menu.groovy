package view

import controller.CandidatoController
import controller.EmpresaController
import controller.InteracaoController
import controller.VagaController
import dao.CandidatoDAO
import dao.EmpresaDAO
import dao.ICrudDAO
import dao.IInteracaoDAO
import dao.InteracaoDAO
import dao.VagaDAO

class Menu {
    private final ConsoleInput input = new ConsoleInput()

    private final ICrudDAO candidatoDAO = new CandidatoDAO()
    private final ICrudDAO empresaDAO = new EmpresaDAO()
    private final ICrudDAO vagaDAO = new VagaDAO()
    private final IInteracaoDAO interacaoDAO = new InteracaoDAO()
    
    private final CandidatoController candidatoController = new CandidatoController(candidatoDAO)
    private final EmpresaController empresaController = new EmpresaController(empresaDAO)
    private final VagaController vagaController = new VagaController(vagaDAO)
    private final InteracaoController interacaoController = new InteracaoController(interacaoDAO)

    private final InteracaoMenu interacaoMenu = new InteracaoMenu(
            input, candidatoController, empresaController, vagaController, interacaoController
    )

    private final CandidatoMenu candidatoMenu = new CandidatoMenu(input, candidatoController, interacaoMenu)
    private final EmpresaMenu empresaMenu = new EmpresaMenu(input, empresaController, interacaoMenu)
    private final VagaMenu vagaMenu = new VagaMenu(input, vagaController, empresaController)

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

            opcao = input.lerInteiro("Escolha uma opção: ")

            switch (opcao) {
                case 1: candidatoMenu.executar(); break
                case 2: empresaMenu.executar(); break
                case 3: vagaMenu.executar(); break
                case 0: println("\nEncerrando o sistema. Até logo!"); break
                default: println("\n[AVISO] Opção não reconhecida.")
            }
        }
    }
}