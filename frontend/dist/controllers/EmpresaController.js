import { Memoria } from "../state/Memoria.js";
import { Validadores } from "../utils/Validadores.js";
export class EmpresaController {
    static iniciar() {
        const form = document.getElementById("form-empresa");
        if (form) {
            form.addEventListener("submit", this.salvar.bind(this));
        }
    }
    static salvar(event) {
        event.preventDefault();
        const form = event.target;
        const nomeInput = document.getElementById("emp-nome").value.trim();
        const emailInput = document.getElementById("emp-email").value.trim();
        const cnpjInput = document.getElementById("emp-cnpj").value.trim();
        const cepInput = document.getElementById("emp-cep").value.trim();
        if (!Validadores.validarNome(nomeInput))
            return alert("Erro: O nome da empresa deve conter apenas letras e espaços.");
        if (!Validadores.validarEmail(emailInput))
            return alert("Erro: Formato de e-mail corporativo inválido.");
        if (!Validadores.validarCNPJ(cnpjInput))
            return alert("Erro: CNPJ inválido. Use o formato 11.222.333/0001-44.");
        if (!Validadores.validarCEP(cepInput))
            return alert("Erro: CEP inválido. Use o formato 12345-678.");
        if (Memoria.empresas.some(e => e.cnpj === cnpjInput))
            return alert("Erro: Já existe uma empresa cadastrada com este CNPJ.");
        if (Memoria.empresas.some(e => e.email === emailInput))
            return alert("Erro: Já existe uma empresa com este e-mail.");
        const novaEmpresa = {
            nome: nomeInput, email: emailInput, cnpj: cnpjInput,
            pais: document.getElementById("emp-pais").value.trim(),
            estado: document.getElementById("emp-estado").value.trim(),
            cep: cepInput,
            descricao: document.getElementById("emp-descricao").value.trim(),
        };
        Memoria.empresas.push(novaEmpresa);
        alert("Empresa cadastrada com sucesso!");
        form.reset();
    }
}
//# sourceMappingURL=EmpresaController.js.map