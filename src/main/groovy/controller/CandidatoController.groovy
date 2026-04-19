package controller

import dao.CandidatoDAO
import dao.ICrudDAO
import model.Candidato

class CandidatoController {
    private final ICrudDAO<Candidato> dao

    CandidatoController(ICrudDAO<Candidato> dao) {
        this.dao = dao
    }

    boolean salvar(Candidato c) {
        return dao.salvar(c)
    }

    List<Candidato> listar() {
        return dao.listar()
    }

    boolean atualizar(Candidato c) {
        return dao.atualizar(c)
    }

    boolean deletar(int id) {
        return dao.deletar(id)
    }
}