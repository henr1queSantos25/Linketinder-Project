import { Memoria } from "../state/Memoria.js";
import type { IVaga } from "../models/Vaga.js";
import { Validadores } from "../utils/Validadores.js";

export class VagaController {
    static iniciar(): void {
        const form = document.getElementById("form-vaga") as HTMLFormElement;
        if (form) {
            form.addEventListener("submit", this.salvar.bind(this));
        }

        const tbody = document.querySelector("#tabela-vagas tbody");
        if (tbody) {
            tbody.addEventListener("click", this.lidarComCliqueTabela.bind(this));
        }
    }

    private static salvar(event: Event): void {
        event.preventDefault();
        const form = event.target as HTMLFormElement;
        
        const cnpjInput = (document.getElementById("vaga-cnpj") as HTMLInputElement).value.trim();
        const tituloInput = (document.getElementById("vaga-titulo") as HTMLInputElement).value.trim();
        const localInput = (document.getElementById("vaga-local") as HTMLInputElement).value.trim();
        const descricaoInput = (document.getElementById("vaga-descricao") as HTMLTextAreaElement).value.trim();
        const compStr = (document.getElementById("vaga-competencias") as HTMLInputElement).value;

        if (!Validadores.validarCNPJ(cnpjInput)) return alert("Erro: CNPJ inválido.");
        if (!Validadores.validarTags(compStr)) return alert("Erro: Competências inválidas.");

        if (!Memoria.empresas.some(e => e.cnpj === cnpjInput)) {
            return alert("Erro: Nenhuma empresa cadastrada com este CNPJ.");
        }

        const competenciasArray = Array.from(new Set(
            compStr.split(",").map(c => c.trim().toUpperCase()).filter(c => c !== "")
        ));

        const novaVaga: IVaga = {
            idEmpresa: cnpjInput, titulo: tituloInput, local: localInput,
            descricao: descricaoInput, competencias: competenciasArray
        };

        Memoria.vagas.push(novaVaga);
        alert("Vaga publicada com sucesso!");
        form.reset();
    }

    static renderizar(): void {
        const tbody = document.querySelector("#tabela-vagas tbody");
        if (!tbody) return;
        tbody.innerHTML = ""; 

        Memoria.vagas.forEach((vaga, index) => {
            const tr = document.createElement("tr");
            tr.title = `Local: ${vaga.local} | Descrição: ${vaga.descricao}`;
            tr.innerHTML = `
                <td class="td-padrao">${vaga.titulo}</td>
                <td class="td-padrao">${vaga.competencias.join(", ")}</td>
                <td class="td-padrao">
                    <button data-acao="curtir" data-index="${index}">Curtir</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    }

    private static lidarComCliqueTabela(event: Event): void {
        const target = event.target as HTMLElement;
        if (target.tagName === "BUTTON" && target.getAttribute("data-acao") === "curtir") {
            const indexAttr = target.getAttribute("data-index");
            const index = indexAttr !== null ? Number.parseInt(indexAttr, 10) : Number.NaN;
            const vaga = Number.isInteger(index) ? Memoria.vagas[index] : undefined;

            if (!vaga) {
                alert("Não foi possível identificar a vaga selecionada.");
                return;
            }

            alert(`Você demonstrou interesse na Vaga Anônima: ${vaga.titulo}!`);
        }
    }
}