package controller

import dao.InteracaoDAO

class InteracaoController {
    private InteracaoDAO dao = new InteracaoDAO()

    boolean curtirVaga(int candidatoId, int vagaId) {
        return dao.curtirVaga(candidatoId, vagaId)
    }

    Integer curtirCandidato(int empresaId, int candidatoId) {
        return dao.curtirCandidato(empresaId, candidatoId)
    }
}