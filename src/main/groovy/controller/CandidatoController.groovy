package controller

import dao.CandidatoDAO
import model.Candidato

class CandidatoController {
    private CandidatoDAO dao = new CandidatoDAO()

    boolean salvar(Candidato c) {
        return dao.salvar(c)
    }

    List<Candidato> listar() {
        return dao.listarTodos()
    }

    boolean atualizar(Candidato c) {
        return dao.atualizar(c)
    }

    boolean deletar(int id) {
        return dao.deletar(id)
    }
}