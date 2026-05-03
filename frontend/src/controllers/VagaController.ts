import { ApiService } from "../services/ApiService.js";
import type { IVaga } from "../models/Vaga.js";
import { Validadores } from "../utils/Validadores.js";

export class VagaController {
    static iniciar(): void {
        const form = document.getElementById("form-vaga") as HTMLFormElement;
        if (form) {
            form.addEventListener("submit", this.salvar.bind(this));
        }
    }

    private static async salvar(event: Event): Promise<void> {
        event.preventDefault();
        const form = event.target as HTMLFormElement;
        
        const usuarioRaw = sessionStorage.getItem("usuarioLogado");
        const usuarioLogado = usuarioRaw ? JSON.parse(usuarioRaw) as { id?: number; tipo?: string } : null;
        const empresaIdInput = Number(usuarioLogado?.id);
        const nomeInput = (document.getElementById("vaga-nome") as HTMLInputElement).value.trim();
        const localInput = (document.getElementById("vaga-local") as HTMLInputElement).value.trim();
        const descricaoInput = (document.getElementById("vaga-descricao") as HTMLTextAreaElement).value.trim();
        const compStr = (document.getElementById("vaga-competencias") as HTMLInputElement).value;

        if (usuarioLogado?.tipo !== "EMPRESA") return alert("Erro: Apenas empresas podem publicar vagas.");
        if (!Number.isInteger(empresaIdInput) || empresaIdInput <= 0) return alert("Erro: ID da empresa inválido.");
        if (!Validadores.validarTags(compStr)) return alert("Erro: Competências inválidas.");

        const competenciasArray = Array.from(new Set(
            compStr.split(",").map(c => c.trim().toUpperCase()).filter(c => c !== "")
        ));

        const novaVaga: IVaga = {
            empresaId: empresaIdInput,
            nome: nomeInput,
            local: localInput,
            descricao: descricaoInput, competencias: competenciasArray
        };

        try {
            await ApiService.post('/vagas', novaVaga);
            alert("Vaga publicada com sucesso!");
            form.reset();
            await VagaController.renderizar();
        } catch (error: any) {
            alert(`Erro ao publicar vaga: ${error.message}`);
        }
    }

    static async renderizar(): Promise<void> {
        const tbody = document.querySelector("#tabela-vagas tbody");
        if (!tbody) return;
        tbody.innerHTML = "<tr><td colspan='4'>Carregando dados do servidor...</td></tr>";

        try {
            const vagas = await ApiService.get('/vagas');
            tbody.innerHTML = "";

            if (vagas.length === 0) {
                tbody.innerHTML = "<tr><td colspan='4'>Nenhuma vaga encontrada.</td></tr>";
                return;
            }

            vagas.forEach((vaga: any) => {
                const tr = document.createElement("tr");
                const competencias = Array.isArray(vaga.competencias) ? vaga.competencias : [];
                tr.title = `Local: ${vaga.local ?? ""} | Descrição: ${vaga.descricao ?? ""}`;
                tr.innerHTML = `
                    <td class="td-padrao">${vaga.nome ?? ""}</td>
                    <td class="td-padrao">${vaga.local ?? ""}</td>
                    <td class="td-padrao">${vaga.descricao ?? ""}</td>
                    <td class="td-padrao">${competencias.join(", ")}</td>
                `;
                tbody.appendChild(tr);
            });
        } catch (error: any) {
            tbody.innerHTML = `<tr><td colspan='4' style="color: red;">Erro ao buscar dados: ${error.message}</td></tr>`;
        }
    }
}