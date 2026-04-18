import { Memoria } from "../state/Memoria.js";
import type { ICandidato } from "../models/Candidato.js";
import { Validadores } from "../utils/Validadores.js";

export class CandidatoController {
    static iniciar(): void {
        const form = document.getElementById("form-candidato") as HTMLFormElement;
        if (form) {
            form.addEventListener("submit", this.salvar.bind(this));
        }

        const tbody = document.querySelector("#tabela-candidatos tbody");
        if (tbody) {
            tbody.addEventListener("click", this.lidarComCliqueTabela.bind(this));
        }
    }

    private static salvar(event: Event): void {
        event.preventDefault();
        const form = event.target as HTMLFormElement;
        
        const nomeInput = (document.getElementById("cand-nome") as HTMLInputElement).value.trim();
        const emailInput = (document.getElementById("cand-email") as HTMLInputElement).value.trim();
        const cpfInput = (document.getElementById("cand-cpf") as HTMLInputElement).value.trim();
        const telefoneInput = (document.getElementById("cand-telefone") as HTMLInputElement).value.trim();
        const linkedinInput = (document.getElementById("cand-linkedin") as HTMLInputElement).value.trim();
        const cepInput = (document.getElementById("cand-cep") as HTMLInputElement).value.trim();
        const compStr = (document.getElementById("cand-competencias") as HTMLInputElement).value;
        const idadeInput = parseInt((document.getElementById("cand-idade") as HTMLInputElement).value);

        if (!Validadores.validarNome(nomeInput)) return alert("Erro: O nome deve conter apenas letras e espaços.");
        if (!Validadores.validarEmail(emailInput)) return alert("Erro: Formato de e-mail inválido.");
        if (!Validadores.validarCPF(cpfInput)) return alert("Erro: CPF inválido.");
        if (!Validadores.validarTelefone(telefoneInput)) return alert("Erro: Telefone inválido.");
        if (!Validadores.validarLinkedin(linkedinInput)) return alert("Erro: Link do LinkedIn inválido.");
        if (!Validadores.validarCEP(cepInput)) return alert("Erro: CEP inválido.");
        if (!Validadores.validarTags(compStr)) return alert("Erro: As competências devem ser separadas por vírgula.");
        if (idadeInput <= 0) return alert("Erro: A idade deve ser maior que zero.");

        if (Memoria.candidatos.some(c => c.cpf === cpfInput)) return alert("Erro: Já existe um candidato com este CPF.");
        if (Memoria.candidatos.some(c => c.email === emailInput)) return alert("Erro: Já existe um candidato com este e-mail.");
        
        const competenciasArray = Array.from(new Set(
            compStr.split(",").map(c => c.trim().toUpperCase()).filter(c => c !== "")
        ));

        const novoCandidato: ICandidato = {
            nome: nomeInput, email: emailInput, cpf: cpfInput,
            telefone: telefoneInput, linkedin: linkedinInput, idade: idadeInput,
            estado: (document.getElementById("cand-estado") as HTMLInputElement).value.trim(),
            cep: cepInput,
            descricao: (document.getElementById("cand-descricao") as HTMLTextAreaElement).value.trim(),
            competencias: competenciasArray
        };

        Memoria.candidatos.push(novoCandidato);
        alert("Candidato cadastrado com sucesso!");
        form.reset();
        
        // Avisa a aplicação que os dados mudaram (útil para atualizar o gráfico)
        document.dispatchEvent(new Event("dadosAtualizados"));
    }

    static renderizar(): void {
        const tbody = document.querySelector("#tabela-candidatos tbody");
        if (!tbody) return;
        tbody.innerHTML = "";

        Memoria.candidatos.forEach((candidato, index) => {
            const tr = document.createElement("tr");
            tr.title = `Idade: ${candidato.idade} | Estado: ${candidato.estado}`;
            tr.innerHTML = `
                <td class="td-padrao">Candidato Anônimo ${index + 1}</td>
                <td class="td-padrao">${candidato.competencias.join(", ")}</td>
                <td class="td-padrao">
                    <button data-acao="deletar" data-index="${index}">Deletar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    }

    private static lidarComCliqueTabela(event: Event): void {
        const target = event.target as HTMLElement;
        if (target.tagName === "BUTTON" && target.getAttribute("data-acao") === "deletar") {
            const index = parseInt(target.getAttribute("data-index")!);
            if (confirm("Tem a certeza que deseja deletar este candidato do sistema?")) {
                Memoria.candidatos.splice(index, 1);
                this.renderizar();
                document.dispatchEvent(new Event("dadosAtualizados"));
            }
        }
    }
}