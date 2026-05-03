import { ApiService } from "../services/ApiService.js";
import { Validadores } from "../utils/Validadores.js";

export class CandidatoController {
    static iniciar(): void {
        const form = document.getElementById("form-candidato") as HTMLFormElement;
        if (form) {
            form.addEventListener("submit", this.salvar.bind(this));
        }
    }

    private static async salvar(event: Event): Promise<void> {
        event.preventDefault();
        const form = event.target as HTMLFormElement;
        
        const nomeInput = (document.getElementById("cand-nome") as HTMLInputElement).value.trim();
        const emailInput = (document.getElementById("cand-email") as HTMLInputElement).value.trim();
        const senhaInput = (document.getElementById("cand-senha") as HTMLInputElement).value;
        const cpfInput = (document.getElementById("cand-cpf") as HTMLInputElement).value.trim();
        const dataNascimentoInput = (document.getElementById("cand-data-nascimento") as HTMLInputElement).value;
        const paisInput = (document.getElementById("cand-pais") as HTMLInputElement).value.trim();
        const cepInput = (document.getElementById("cand-cep") as HTMLInputElement).value.trim();
        const compStr = (document.getElementById("cand-competencias") as HTMLInputElement).value;

        if (!Validadores.validarNome(nomeInput)) return alert("Erro: O nome deve conter apenas letras e espaços.");
        if (!Validadores.validarEmail(emailInput)) return alert("Erro: Formato de e-mail inválido.");
        if (!Validadores.validarCPF(cpfInput)) return alert("Erro: CPF inválido.");
        if (!dataNascimentoInput) return alert("Erro: Data de nascimento inválida.");
        if (!paisInput) return alert("Erro: País inválido.");
        if (!Validadores.validarCEP(cepInput)) return alert("Erro: CEP inválido.");
        if (!Validadores.validarTags(compStr)) return alert("Erro: As competências devem ser separadas por vírgula.");

        const partesNome = nomeInput.split(" ");
        const primeiroNome = partesNome[0];
        const sobrenome = partesNome.slice(1).join(" ") || "";

        const competenciasArray = Array.from(new Set(
            compStr.split(",").map(c => c.trim().toUpperCase()).filter(c => c !== "")
        ));

        const payload = {
            nome: primeiroNome,
            sobrenome: sobrenome,
            email: emailInput,
            senha: senhaInput,
            cpf: cpfInput,
            dataNascimento: dataNascimentoInput,
            cep: cepInput,
            pais: paisInput,
            descricao: (document.getElementById("cand-descricao") as HTMLTextAreaElement).value.trim(),
            competencias: competenciasArray
        };

        try {
            await ApiService.post('/candidatos', payload);
            
            alert("Candidato cadastrado com sucesso no Banco de Dados!");
            form.reset();
            
            document.dispatchEvent(new Event("dadosAtualizados"));
        } catch (error: any) {
            alert(`Erro ao cadastrar: ${error.message}`);
        }
    }

    static async renderizar(): Promise<void> {
        const tbody = document.querySelector("#tabela-candidatos tbody");
        if (!tbody) return;
        tbody.innerHTML = "<tr><td colspan='2'>Carregando dados do servidor...</td></tr>";

        try {
            const candidatos = await ApiService.get('/candidatos');
            tbody.innerHTML = "";

            if (candidatos.length === 0) {
                tbody.innerHTML = "<tr><td colspan='2'>Nenhum candidato encontrado.</td></tr>";
                return;
            }

            candidatos.forEach((candidato: any, index: number) => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td class="td-padrao">${candidato.competencias.join(", ")}</td>
                    <td class="td-padrao">${candidato.descricao ?? ""}</td>
                `;
                tbody.appendChild(tr);
            });
        } catch (error: any) {
            tbody.innerHTML = `<tr><td colspan='2' style="color: red;">Erro ao buscar dados: ${error.message}</td></tr>`;
        }
    }
}