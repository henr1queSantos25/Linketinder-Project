import { Memoria } from "../state/Memoria.js";
import { Validadores } from "../utils/Validadores.js";
export class VagaController {
    static iniciar() {
        const form = document.getElementById("form-vaga");
        if (form) {
            form.addEventListener("submit", this.salvar.bind(this));
        }
        const tbody = document.querySelector("#tabela-vagas tbody");
        if (tbody) {
            tbody.addEventListener("click", this.lidarComCliqueTabela.bind(this));
        }
    }
    static salvar(event) {
        event.preventDefault();
        const form = event.target;
        const cnpjInput = document.getElementById("vaga-cnpj").value.trim();
        const tituloInput = document.getElementById("vaga-titulo").value.trim();
        const localInput = document.getElementById("vaga-local").value.trim();
        const descricaoInput = document.getElementById("vaga-descricao").value.trim();
        const compStr = document.getElementById("vaga-competencias").value;
        if (!Validadores.validarCNPJ(cnpjInput))
            return alert("Erro: CNPJ inválido.");
        if (!Validadores.validarTags(compStr))
            return alert("Erro: Competências inválidas.");
        if (!Memoria.empresas.some(e => e.cnpj === cnpjInput)) {
            return alert("Erro: Nenhuma empresa cadastrada com este CNPJ.");
        }
        const competenciasArray = Array.from(new Set(compStr.split(",").map(c => c.trim().toUpperCase()).filter(c => c !== "")));
        const novaVaga = {
            idEmpresa: cnpjInput, titulo: tituloInput, local: localInput,
            descricao: descricaoInput, competencias: competenciasArray
        };
        Memoria.vagas.push(novaVaga);
        alert("Vaga publicada com sucesso!");
        form.reset();
    }
    static renderizar() {
        const tbody = document.querySelector("#tabela-vagas tbody");
        if (!tbody)
            return;
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
    static lidarComCliqueTabela(event) {
        const target = event.target;
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
//# sourceMappingURL=VagaController.js.map