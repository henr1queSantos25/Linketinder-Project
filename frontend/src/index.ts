import { Memoria } from "./state/Memoria.js";
import type { ICandidato } from "./models/Candidato.js";
import type { IEmpresa } from "./models/Empresa.js";

// --- Navegação entre telas ---

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
    }
}

(window as any).navegar = navegar;

// --- CREATE ---

document.addEventListener("DOMContentLoaded", () => {
    
    const formCandidato = document.getElementById("form-candidato") as HTMLFormElement;
    if (formCandidato) {
        formCandidato.addEventListener("submit", (event) => {
            event.preventDefault();
            
            const compStr = (document.getElementById("cand-competencias") as HTMLInputElement).value;
            const competenciasArray = compStr.split(",").map(c => c.trim());

            const novoCandidato: ICandidato = {
                nome: (document.getElementById("cand-nome") as HTMLInputElement).value,
                email: (document.getElementById("cand-email") as HTMLInputElement).value,
                cpf: (document.getElementById("cand-cpf") as HTMLInputElement).value,
                idade: parseInt((document.getElementById("cand-idade") as HTMLInputElement).value),
                estado: (document.getElementById("cand-estado") as HTMLInputElement).value,
                cep: (document.getElementById("cand-cep") as HTMLInputElement).value,
                descricao: (document.getElementById("cand-descricao") as HTMLTextAreaElement).value,
                competencias: competenciasArray
            };

            Memoria.candidatos.push(novoCandidato);
            alert("Candidato cadastrado com sucesso!");
            formCandidato.reset();
            console.log("Memória atualizada (Candidatos):", Memoria.candidatos);
        });
    }

    const formEmpresa = document.getElementById("form-empresa") as HTMLFormElement;
    if (formEmpresa) {
        formEmpresa.addEventListener("submit", (event) => {
            event.preventDefault();
            
            const compStr = (document.getElementById("emp-competencias") as HTMLInputElement).value;
            const competenciasArray = compStr.split(",").map(c => c.trim());

            const novaEmpresa: IEmpresa = {
                nome: (document.getElementById("emp-nome") as HTMLInputElement).value,
                email: (document.getElementById("emp-email") as HTMLInputElement).value,
                cnpj: (document.getElementById("emp-cnpj") as HTMLInputElement).value,
                pais: (document.getElementById("emp-pais") as HTMLInputElement).value,
                estado: (document.getElementById("emp-estado") as HTMLInputElement).value,
                cep: (document.getElementById("emp-cep") as HTMLInputElement).value,
                descricao: (document.getElementById("emp-descricao") as HTMLTextAreaElement).value,
                competencias: competenciasArray
            };

            Memoria.empresas.push(novaEmpresa);
            alert("Empresa cadastrada com sucesso!");
            formEmpresa.reset();
            console.log("Memória atualizada (Empresas):", Memoria.empresas);
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
    }
};