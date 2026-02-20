package controller

import model.Empresa
import repository.Memoria

class EmpresaController {

    boolean salvar(Empresa empresa) {
        boolean jaExiste = Memoria.empresas.any { it.cnpj == empresa.cnpj }

        if (!jaExiste) {
            Memoria.empresas.add(empresa)
            return true
        }
        return false
    }
}