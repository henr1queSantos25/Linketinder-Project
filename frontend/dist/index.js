import { Memoria } from "./state/Memoria.js";
// --- NAVEGAÇÃO ENTRE TELAS ---
function navegar(idTela) {
    const secoes = document.querySelectorAll("main section");
    secoes.forEach(secao => secao.classList.add("hidden"));
    const telaAlvo = document.getElementById(idTela);
    if (telaAlvo) {
        telaAlvo.classList.remove("hidden");
    }
    if (idTela === "tela-lista-vagas") {
        renderizarVagas();
    }
    else if (idTela === "tela-lista-candidatos") {
        renderizarCandidatos();
        renderizarGrafico();
    }
}
window.navegar = navegar;
// --- CREATE ---
document.addEventListener("DOMContentLoaded", () => {
    const formCandidato = document.getElementById("form-candidato");
    if (formCandidato) {
        formCandidato.addEventListener("submit", (event) => {
            event.preventDefault();
            const cpfInput = document.getElementById("cand-cpf").value.trim();
            const emailInput = document.getElementById("cand-email").value.trim();
            const idadeInput = parseInt(document.getElementById("cand-idade").value);
            if (idadeInput <= 0) {
                alert("Erro: A idade deve ser maior que zero.");
                return;
            }
            if (Memoria.candidatos.some(c => c.cpf === cpfInput)) {
                alert("Erro: Já existe um candidato cadastrado com este CPF.");
                return;
            }
            if (Memoria.candidatos.some(c => c.email === emailInput)) {
                alert("Erro: Já existe um candidato cadastrado com este e-mail.");
                return;
            }
            const compStr = document.getElementById("cand-competencias").value;
            const competenciasArray = Array.from(new Set(compStr.split(",")
                .map(c => c.trim().toUpperCase())
                .filter(c => c !== "")));
            const novoCandidato = {
                nome: document.getElementById("cand-nome").value.trim(),
                email: emailInput,
                cpf: cpfInput,
                idade: idadeInput,
                estado: document.getElementById("cand-estado").value.trim(),
                cep: document.getElementById("cand-cep").value.trim(),
                descricao: document.getElementById("cand-descricao").value.trim(),
                competencias: competenciasArray
            };
            Memoria.candidatos.push(novoCandidato);
            alert("Candidato cadastrado com sucesso!");
            formCandidato.reset();
        });
    }
    const formEmpresa = document.getElementById("form-empresa");
    if (formEmpresa) {
        formEmpresa.addEventListener("submit", (event) => {
            event.preventDefault();
            const cnpjInput = document.getElementById("emp-cnpj").value.trim();
            const emailInput = document.getElementById("emp-email").value.trim();
            if (Memoria.empresas.some(e => e.cnpj === cnpjInput)) {
                alert("Erro: Já existe uma empresa cadastrada com este CNPJ.");
                return;
            }
            if (Memoria.empresas.some(e => e.email === emailInput)) {
                alert("Erro: Já existe uma empresa cadastrada com este e-mail.");
                return;
            }
            const compStr = document.getElementById("emp-competencias").value;
            const competenciasArray = Array.from(new Set(compStr.split(",")
                .map(c => c.trim().toUpperCase())
                .filter(c => c !== "")));
            const novaEmpresa = {
                nome: document.getElementById("emp-nome").value.trim(),
                email: emailInput,
                cnpj: cnpjInput,
                pais: document.getElementById("emp-pais").value.trim(),
                estado: document.getElementById("emp-estado").value.trim(),
                cep: document.getElementById("emp-cep").value.trim(),
                descricao: document.getElementById("emp-descricao").value.trim(),
                competencias: competenciasArray
            };
            Memoria.empresas.push(novaEmpresa);
            alert("Empresa cadastrada com sucesso!");
            formEmpresa.reset();
        });
    }
});
// --- READ ---
function renderizarVagas() {
    const tbody = document.querySelector("#tabela-vagas tbody");
    if (!tbody)
        return;
    tbody.innerHTML = "";
    Memoria.empresas.forEach((empresa, index) => {
        const tr = document.createElement("tr");
        tr.title = `País: ${empresa.pais} | Estado: ${empresa.estado} | Descrição: ${empresa.descricao}`;
        tr.innerHTML = `
            <td style="padding: 8px;">Empresa Anônima ${index + 1}</td>
            <td style="padding: 8px;">${empresa.competencias.join(", ")}</td>
            <td style="padding: 8px;">
                <button onclick="deletarEmpresa(${index})">Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}
function renderizarCandidatos() {
    const tbody = document.querySelector("#tabela-candidatos tbody");
    if (!tbody)
        return;
    tbody.innerHTML = "";
    Memoria.candidatos.forEach((candidato, index) => {
        const tr = document.createElement("tr");
        tr.title = `Idade: ${candidato.idade} | Estado: ${candidato.estado} | Descrição: ${candidato.descricao}`;
        tr.innerHTML = `
            <td style="padding: 8px;">Candidato Anônimo ${index + 1}</td>
            <td style="padding: 8px;">${candidato.competencias.join(", ")}</td>
            <td style="padding: 8px;">
                <button onclick="deletarCandidato(${index})">Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}
// --- DELETE ---
window.deletarEmpresa = (index) => {
    if (confirm("Tem a certeza que deseja eliminar esta vaga do sistema?")) {
        Memoria.empresas.splice(index, 1);
        renderizarVagas();
    }
};
window.deletarCandidato = (index) => {
    if (confirm("Tem a certeza que deseja eliminar este candidato do sistema?")) {
        Memoria.candidatos.splice(index, 1);
        renderizarCandidatos();
        renderizarGrafico();
    }
};
let chartInstancia = null;
function renderizarGrafico() {
    const canvas = document.getElementById("grafico-competencias");
    if (!canvas)
        return;
    const contagem = {};
    Memoria.candidatos.forEach(candidato => {
        candidato.competencias.forEach(comp => {
            const nomeFormatado = comp.trim().toUpperCase();
            contagem[nomeFormatado] = (contagem[nomeFormatado] || 0) + 1;
        });
    });
    const labels = Object.keys(contagem); // ['JAVA', 'PYTHON']
    const data = Object.values(contagem); // [2, 1]
    if (chartInstancia) {
        chartInstancia.destroy();
    }
    chartInstancia = new Chart(canvas, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                    label: 'Número de Candidatos',
                    data: data,
                    backgroundColor: 'rgba(0, 115, 177, 0.6)',
                    borderColor: 'rgba(0, 115, 177, 1)',
                    borderWidth: 1
                }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: { stepSize: 1 }
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            return `Candidatos: ${context.parsed.y}`;
                        }
                    }
                }
            }
        }
    });
}
//# sourceMappingURL=index.js.map