package controller

import dao.ICrudDAO
import model.Vaga

class VagaController {
    private final ICrudDAO<Vaga> dao

    VagaController(ICrudDAO<Vaga> dao) {
        this.dao = dao
    }

    boolean salvar(Vaga v) {
        return dao.salvar(v)
    }

    List<Vaga> listar() {
        return dao.listar()
    }

    boolean atualizar(Vaga v) {
        return dao.atualizar(v)
    }

    boolean deletar(int id) {
        return dao.deletar(id)
    }
}