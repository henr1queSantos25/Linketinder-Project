package controller

import dao.ICrudDAO
import model.Empresa

class EmpresaController {
    private final ICrudDAO<Empresa> dao

    EmpresaController(ICrudDAO<Empresa> dao) {
        this.dao = dao
    }

    boolean salvar(Empresa e) {
        return dao.salvar(e)
    }

    List<Empresa> listar() {
        return dao.listar()
    }

    boolean atualizar(Empresa e) {
        return dao.atualizar(e)
    }

    boolean deletar(int id) {
        return dao.deletar(id)
    }
}