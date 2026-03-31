package controller

import dao.VagaDAO
import model.Vaga

class VagaController {
    private VagaDAO dao = new VagaDAO()

    boolean salvar(Vaga v) {
        return dao.salvar(v)
    }

    List<Vaga> listar() {
        return dao.listarTodas()
    }

    boolean atualizar(Vaga v) {
        return dao.atualizar(v)
    }

    boolean deletar(int id) {
        return dao.deletar(id)
    }
}