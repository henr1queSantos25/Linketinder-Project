package controller

import dao.IInteracaoDAO

class InteracaoController {
    private final IInteracaoDAO dao

    InteracaoController(IInteracaoDAO dao) {
        this.dao = dao
    }

    boolean curtirVaga(int candidatoId, int vagaId) {
        return dao.curtirVaga(candidatoId, vagaId)
    }

    Integer curtirCandidato(int empresaId, int candidatoId) {
        return dao.curtirCandidato(empresaId, candidatoId)
    }
}