package view

import java.time.LocalDate
import java.time.format.DateTimeParseException

class ConsoleInput {
    private final Scanner scanner

    ConsoleInput() {
        this(new Scanner(System.in))
    }

    ConsoleInput(Scanner scanner) {
        this.scanner = scanner
    }

    String lerString(String mensagem) {
        print(mensagem)
        return scanner.nextLine().trim()
    }

    int lerInteiro(String mensagem) {
        while (true) {
            print(mensagem)
            try {
                return scanner.nextLine().toInteger()
            } catch (NumberFormatException e) {
                println("[ERRO] Por favor, digite apenas números válidos.")
            }
        }
    }

    LocalDate lerData(String mensagem) {
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
