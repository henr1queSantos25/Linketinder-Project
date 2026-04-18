import { Memoria } from "../state/Memoria.js";
import type { IEmpresa } from "../models/Empresa.js";
import { Validadores } from "../utils/Validadores.js";

export class EmpresaController {
    static iniciar(): void {
        const form = document.getElementById("form-empresa") as HTMLFormElement;
        if (form) {
            form.addEventListener("submit", this.salvar.bind(this));
        }
    }

    private static salvar(event: Event): void {
        event.preventDefault();
        
        const form = event.target as HTMLFormElement;
        const nomeInput = (document.getElementById("emp-nome") as HTMLInputElement).value.trim();
        const emailInput = (document.getElementById("emp-email") as HTMLInputElement).value.trim();
        const cnpjInput = (document.getElementById("emp-cnpj") as HTMLInputElement).value.trim();
        const cepInput = (document.getElementById("emp-cep") as HTMLInputElement).value.trim();

        if (!Validadores.validarNome(nomeInput)) return alert("Erro: O nome da empresa deve conter apenas letras e espaços.");
        if (!Validadores.validarEmail(emailInput)) return alert("Erro: Formato de e-mail corporativo inválido.");
        if (!Validadores.validarCNPJ(cnpjInput)) return alert("Erro: CNPJ inválido. Use o formato 11.222.333/0001-44.");
        if (!Validadores.validarCEP(cepInput)) return alert("Erro: CEP inválido. Use o formato 12345-678.");

        if (Memoria.empresas.some(e => e.cnpj === cnpjInput)) return alert("Erro: Já existe uma empresa cadastrada com este CNPJ.");
        if (Memoria.empresas.some(e => e.email === emailInput)) return alert("Erro: Já existe uma empresa com este e-mail.");

        const novaEmpresa: IEmpresa = {
            nome: nomeInput, email: emailInput, cnpj: cnpjInput,
            pais: (document.getElementById("emp-pais") as HTMLInputElement).value.trim(),
            estado: (document.getElementById("emp-estado") as HTMLInputElement).value.trim(),
            cep: cepInput,
            descricao: (document.getElementById("emp-descricao") as HTMLTextAreaElement).value.trim(),
        };

        Memoria.empresas.push(novaEmpresa);
        alert("Empresa cadastrada com sucesso!");
        form.reset();
    }
}