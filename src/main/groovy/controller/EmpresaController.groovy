package controller

import dao.EmpresaDAO
import model.Empresa

class EmpresaController {
    private EmpresaDAO dao = new EmpresaDAO()

    boolean salvar(Empresa e) {
        return dao.salvar(e)
    }

    List<Empresa> listar() {
        return dao.listarTodas()
    }

    boolean atualizar(Empresa e) {
        return dao.atualizar(e)
    }

    boolean deletar(int id) {
        return dao.deletar(id)
    }
}