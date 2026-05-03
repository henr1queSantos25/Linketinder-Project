package model

import java.time.LocalDate

class Candidato extends Pessoa {
    String sobrenome
    LocalDate dataNascimento
    String email
    String cpf
    List<Competencia> competencias = []
}