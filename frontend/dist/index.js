import { Memoria } from "./state/Memoria.js";
// --- Lógica de Navegação ---
function navegar(idTela) {
    const secoes = document.querySelectorAll("main section");
    secoes.forEach(secao => secao.classList.add("hidden"));
    const telaAlvo = document.getElementById(idTela);
    if (telaAlvo) {
        telaAlvo.classList.remove("hidden");
    }
}
window.navegar = navegar;
// --- Lógica de Cadastro ---
document.addEventListener("DOMContentLoaded", () => {
    const formCandidato = document.getElementById("form-candidato");
    if (formCandidato) {
        formCandidato.addEventListener("submit", (event) => {
            event.preventDefault();
            const compStr = document.getElementById("cand-competencias").value;
            const competenciasArray = compStr.split(",").map(c => c.trim());
            const novoCandidato = {
                nome: document.getElementById("cand-nome").value,
                email: document.getElementById("cand-email").value,
                cpf: document.getElementById("cand-cpf").value,
                idade: parseInt(document.getElementById("cand-idade").value),
                estado: document.getElementById("cand-estado").value,
                cep: document.getElementById("cand-cep").value,
                descricao: document.getElementById("cand-descricao").value,
                competencias: competenciasArray
            };
            Memoria.candidatos.push(novoCandidato);
            alert("Candidato cadastrado com sucesso!");
            formCandidato.reset();
            console.log("Memória atualizada (Candidatos):", Memoria.candidatos);
        });
    }
    const formEmpresa = document.getElementById("form-empresa");
    if (formEmpresa) {
        formEmpresa.addEventListener("submit", (event) => {
            event.preventDefault();
            const compStr = document.getElementById("emp-competencias").value;
            const competenciasArray = compStr.split(",").map(c => c.trim());
            const novaEmpresa = {
                nome: document.getElementById("emp-nome").value,
                email: document.getElementById("emp-email").value,
                cnpj: document.getElementById("emp-cnpj").value,
                pais: document.getElementById("emp-pais").value,
                estado: document.getElementById("emp-estado").value,
                cep: document.getElementById("emp-cep").value,
                descricao: document.getElementById("emp-descricao").value,
                competencias: competenciasArray
            };
            Memoria.empresas.push(novaEmpresa);
            alert("Empresa cadastrada com sucesso!");
            formEmpresa.reset();
            console.log("Memória atualizada (Empresas):", Memoria.empresas);
        });
    }
});
//# sourceMappingURL=index.js.map