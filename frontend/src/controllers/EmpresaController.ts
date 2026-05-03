import { ApiService } from "../services/ApiService.js";
import type { IEmpresa } from "../models/Empresa.js";
import { Validadores } from "../utils/Validadores.js";

export class EmpresaController {
    static iniciar(): void {
        const form = document.getElementById("form-empresa") as HTMLFormElement;
        if (form) {
            form.addEventListener("submit", this.salvar.bind(this));
        }
    }

    private static async salvar(event: Event): Promise<void> {
        event.preventDefault();
        
        const form = event.target as HTMLFormElement;
        const nomeInput = (document.getElementById("emp-nome") as HTMLInputElement).value.trim();
        const emailCorporativoInput = (document.getElementById("emp-email") as HTMLInputElement).value.trim();
        const senhaInput = (document.getElementById("emp-senha") as HTMLInputElement).value.trim();
        const cnpjInput = (document.getElementById("emp-cnpj") as HTMLInputElement).value.trim();
        const cepInput = (document.getElementById("emp-cep") as HTMLInputElement).value.trim();

        if (!Validadores.validarNome(nomeInput)) return alert("Erro: O nome da empresa deve conter apenas letras e espaços.");
        if (!Validadores.validarEmail(emailCorporativoInput)) return alert("Erro: Formato de e-mail corporativo inválido.");
        if (!Validadores.validarCNPJ(cnpjInput)) return alert("Erro: CNPJ inválido. Use o formato 11.222.333/0001-44.");
        if (!Validadores.validarCEP(cepInput)) return alert("Erro: CEP inválido. Use o formato 12345-678.");

        const novaEmpresa: IEmpresa = {
            nome: nomeInput,
            emailCorporativo: emailCorporativoInput,
            senha: senhaInput,
            cnpj: cnpjInput,
            pais: (document.getElementById("emp-pais") as HTMLInputElement).value.trim(),
            cep: cepInput,
            descricao: (document.getElementById("emp-descricao") as HTMLTextAreaElement).value.trim(),
        };

        try {
            const resposta = await ApiService.post('/empresas', novaEmpresa);
            const idMensagem = resposta?.id ? ` ID: ${resposta.id}` : "";
            alert(`Empresa cadastrada com sucesso!`);
            form.reset();
        } catch (error: any) {
            alert(`Erro ao cadastrar: ${error.message}`);
        }
    }
}