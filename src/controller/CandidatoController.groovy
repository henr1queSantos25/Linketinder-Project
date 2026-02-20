package controller

import model.Candidato
import repository.Memoria

class CandidatoController {


    boolean salvar(Candidato candidato) {
        boolean jaExiste = Memoria.candidatos.any {it.cpf == candidato.cpf}

        if (!jaExiste) {
            Memoria.candidatos.add(candidato)
            return true
        }
        return false
    }
}