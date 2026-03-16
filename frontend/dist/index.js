import { Memoria } from "./state/Memoria.js";
import { Validadores } from "./utils/Validadores.js";
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
            const nomeInput = document.getElementById("cand-nome").value.trim();
            const emailInput = document.getElementById("cand-email").value.trim();
            const cpfInput = document.getElementById("cand-cpf").value.trim();
            const telefoneInput = document.getElementById("cand-telefone").value.trim();
            const linkedinInput = document.getElementById("cand-linkedin").value.trim();
            const cepInput = document.getElementById("cand-cep").value.trim();
            const compStr = document.getElementById("cand-competencias").value;
            const idadeInput = parseInt(document.getElementById("cand-idade").value);
            // Validações Regex 
            if (!Validadores.validarNome(nomeInput))
                return alert("Erro: O nome deve conter apenas letras e espaços.");
            if (!Validadores.validarEmail(emailInput))
                return alert("Erro: Formato de e-mail inválido.");
            if (!Validadores.validarCPF(cpfInput))
                return alert("Erro: CPF inválido. Use o formato 111.222.333-44.");
            if (!Validadores.validarTelefone(telefoneInput))
                return alert("Erro: Telefone inválido. Use o formato (11) 98888-7777.");
            if (!Validadores.validarLinkedin(linkedinInput))
                return alert("Erro: Link do LinkedIn inválido.");
            if (!Validadores.validarCEP(cepInput))
                return alert("Erro: CEP inválido. Use o formato 12345-678.");
            if (!Validadores.validarTags(compStr))
                return alert("Erro: As competências devem ser separadas por vírgula e não conter caracteres especiais.");
            if (idadeInput <= 0)
                return alert("Erro: A idade deve ser maior que zero.");
            if (Memoria.candidatos.some(c => c.cpf === cpfInput))
                return alert("Erro: Já existe um candidato cadastrado com este CPF.");
            if (Memoria.candidatos.some(c => c.email === emailInput))
                return alert("Erro: Já existe um candidato cadastrado com este e-mail.");
            const competenciasArray = Array.from(new Set(compStr.split(",").map(c => c.trim().toUpperCase()).filter(c => c !== "")));
            const novoCandidato = {
                nome: nomeInput, email: emailInput, cpf: cpfInput,
                telefone: telefoneInput, linkedin: linkedinInput, idade: idadeInput,
                estado: document.getElementById("cand-estado").value.trim(),
                cep: cepInput,
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
            const nomeInput = document.getElementById("emp-nome").value.trim();
            const emailInput = document.getElementById("emp-email").value.trim();
            const cnpjInput = document.getElementById("emp-cnpj").value.trim();
            const cepInput = document.getElementById("emp-cep").value.trim();
            const compStr = document.getElementById("emp-competencias").value;
            // Validações Regex
            if (!Validadores.validarNome(nomeInput))
                return alert("Erro: O nome da empresa deve conter apenas letras e espaços.");
            if (!Validadores.validarEmail(emailInput))
                return alert("Erro: Formato de e-mail corporativo inválido.");
            if (!Validadores.validarCNPJ(cnpjInput))
                return alert("Erro: CNPJ inválido. Use o formato 11.222.333/0001-44.");
            if (!Validadores.validarCEP(cepInput))
                return alert("Erro: CEP inválido. Use o formato 12345-678.");
            if (!Validadores.validarTags(compStr))
                return alert("Erro: As competências desejadas estão num formato inválido.");
            if (Memoria.empresas.some(e => e.cnpj === cnpjInput))
                return alert("Erro: Já existe uma empresa cadastrada com este CNPJ.");
            if (Memoria.empresas.some(e => e.email === emailInput))
                return alert("Erro: Já existe uma empresa cadastrada com este e-mail.");
            const competenciasArray = Array.from(new Set(compStr.split(",").map(c => c.trim().toUpperCase()).filter(c => c !== "")));
            const novaEmpresa = {
                nome: nomeInput, email: emailInput, cnpj: cnpjInput,
                pais: document.getElementById("emp-pais").value.trim(),
                estado: document.getElementById("emp-estado").value.trim(),
                cep: cepInput,
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