import { Memoria } from "./state/Memoria.js";
import type { ICandidato } from "./models/Candidato.js";
import type { IEmpresa } from "./models/Empresa.js";
import { Validadores } from "./utils/Validadores.js";


// --- NAVEGAÇÃO ENTRE TELAS ---

function navegar(idTela: string): void {
    const secoes = document.querySelectorAll("main section");
    secoes.forEach(secao => secao.classList.add("hidden"));

    const telaAlvo = document.getElementById(idTela);
    if (telaAlvo) {
        telaAlvo.classList.remove("hidden");
    }

    if (idTela === "tela-lista-vagas") {
        renderizarVagas();
    } else if (idTela === "tela-lista-candidatos") {
        renderizarCandidatos();
        renderizarGrafico();
    }
}

(window as any).navegar = navegar;

// --- CREATE ---

document.addEventListener("DOMContentLoaded", () => {
    
    const formCandidato = document.getElementById("form-candidato") as HTMLFormElement;
    if (formCandidato) {
        formCandidato.addEventListener("submit", (event) => {
            event.preventDefault(); 
            
            const nomeInput = (document.getElementById("cand-nome") as HTMLInputElement).value.trim();
            const emailInput = (document.getElementById("cand-email") as HTMLInputElement).value.trim();
            const cpfInput = (document.getElementById("cand-cpf") as HTMLInputElement).value.trim();
            const telefoneInput = (document.getElementById("cand-telefone") as HTMLInputElement).value.trim();
            const linkedinInput = (document.getElementById("cand-linkedin") as HTMLInputElement).value.trim();
            const cepInput = (document.getElementById("cand-cep") as HTMLInputElement).value.trim();
            const compStr = (document.getElementById("cand-competencias") as HTMLInputElement).value;
            const idadeInput = parseInt((document.getElementById("cand-idade") as HTMLInputElement).value);

            // Validações Regex 
            if (!Validadores.validarNome(nomeInput)) return alert("Erro: O nome deve conter apenas letras e espaços.");
            if (!Validadores.validarEmail(emailInput)) return alert("Erro: Formato de e-mail inválido.");
            if (!Validadores.validarCPF(cpfInput)) return alert("Erro: CPF inválido. Use o formato 111.222.333-44.");
            if (!Validadores.validarTelefone(telefoneInput)) return alert("Erro: Telefone inválido. Use o formato (11) 98888-7777.");
            if (!Validadores.validarLinkedin(linkedinInput)) return alert("Erro: Link do LinkedIn inválido.");
            if (!Validadores.validarCEP(cepInput)) return alert("Erro: CEP inválido. Use o formato 12345-678.");
            if (!Validadores.validarTags(compStr)) return alert("Erro: As competências devem ser separadas por vírgula e não conter caracteres especiais.");

            if (idadeInput <= 0) return alert("Erro: A idade deve ser maior que zero.");
            if (Memoria.candidatos.some(c => c.cpf === cpfInput)) return alert("Erro: Já existe um candidato cadastrado com este CPF.");
            if (Memoria.candidatos.some(c => c.email === emailInput)) return alert("Erro: Já existe um candidato cadastrado com este e-mail.");
            
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
            formCandidato.reset();
        });
    }

    const formEmpresa = document.getElementById("form-empresa") as HTMLFormElement;
    if (formEmpresa) {
        formEmpresa.addEventListener("submit", (event) => {
            event.preventDefault();
            
            const nomeInput = (document.getElementById("emp-nome") as HTMLInputElement).value.trim();
            const emailInput = (document.getElementById("emp-email") as HTMLInputElement).value.trim();
            const cnpjInput = (document.getElementById("emp-cnpj") as HTMLInputElement).value.trim();
            const cepInput = (document.getElementById("emp-cep") as HTMLInputElement).value.trim();
            const compStr = (document.getElementById("emp-competencias") as HTMLInputElement).value;

            // Validações Regex
            if (!Validadores.validarNome(nomeInput)) return alert("Erro: O nome da empresa deve conter apenas letras e espaços.");
            if (!Validadores.validarEmail(emailInput)) return alert("Erro: Formato de e-mail corporativo inválido.");
            if (!Validadores.validarCNPJ(cnpjInput)) return alert("Erro: CNPJ inválido. Use o formato 11.222.333/0001-44.");
            if (!Validadores.validarCEP(cepInput)) return alert("Erro: CEP inválido. Use o formato 12345-678.");
            if (!Validadores.validarTags(compStr)) return alert("Erro: As competências desejadas estão num formato inválido.");

            if (Memoria.empresas.some(e => e.cnpj === cnpjInput)) return alert("Erro: Já existe uma empresa cadastrada com este CNPJ.");
            if (Memoria.empresas.some(e => e.email === emailInput)) return alert("Erro: Já existe uma empresa cadastrada com este e-mail.");

            const competenciasArray = Array.from(new Set(
                compStr.split(",").map(c => c.trim().toUpperCase()).filter(c => c !== "")
            ));

            const novaEmpresa: IEmpresa = {
                nome: nomeInput, email: emailInput, cnpj: cnpjInput,
                pais: (document.getElementById("emp-pais") as HTMLInputElement).value.trim(),
                estado: (document.getElementById("emp-estado") as HTMLInputElement).value.trim(),
                cep: cepInput,
                descricao: (document.getElementById("emp-descricao") as HTMLTextAreaElement).value.trim(),
                competencias: competenciasArray
            };

            Memoria.empresas.push(novaEmpresa);
            alert("Empresa cadastrada com sucesso!");
            formEmpresa.reset();
        });
    }
});

// --- READ ---

function renderizarVagas(): void {
    const tbody = document.querySelector("#tabela-vagas tbody");
    if (!tbody) return;
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

function renderizarCandidatos(): void {
    const tbody = document.querySelector("#tabela-candidatos tbody");
    if (!tbody) return;
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

(window as any).deletarEmpresa = (index: number) => {
    if (confirm("Tem a certeza que deseja eliminar esta vaga do sistema?")) {
        Memoria.empresas.splice(index, 1);
        renderizarVagas(); 
    }
};

(window as any).deletarCandidato = (index: number) => {
    if (confirm("Tem a certeza que deseja eliminar este candidato do sistema?")) {
        Memoria.candidatos.splice(index, 1);
        renderizarCandidatos();
        renderizarGrafico();
    }
};

// --- GRÁFICO DE COMPETÊNCIAS ---
declare const Chart: any;
let chartInstancia: any = null;

function renderizarGrafico(): void {
    const canvas = document.getElementById("grafico-competencias") as HTMLCanvasElement;
    if (!canvas) return;

    const contagem: Record<string, number> = {};
    
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
                        label: function(context: any) {
                            return `Candidatos: ${context.parsed.y}`;
                        }
                    }
                }
            }
        }
    });
}