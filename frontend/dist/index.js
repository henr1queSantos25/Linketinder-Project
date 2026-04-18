import { Navigation } from "./utils/Navigation.js";
import { EmpresaController } from "./controllers/EmpresaController.js";
import { VagaController } from "./controllers/VagaController.js";
import { CandidatoController } from "./controllers/CandidatoController.js";
import { Memoria } from "./state/Memoria.js";
let chartInstancia = null;
document.addEventListener("DOMContentLoaded", () => {
    EmpresaController.iniciar();
    VagaController.iniciar();
    CandidatoController.iniciar();
    Navigation.iniciar((idTela) => {
        if (idTela === "tela-lista-vagas") {
            VagaController.renderizar();
        }
        else if (idTela === "tela-lista-candidatos") {
            CandidatoController.renderizar();
            renderizarGrafico();
        }
    });
    document.addEventListener("dadosAtualizados", () => {
        const telaAlvo = document.getElementById("tela-lista-candidatos");
        if (telaAlvo && !telaAlvo.classList.contains("hidden")) {
            renderizarGrafico();
        }
    });
});
function renderizarGrafico() {
    const canvas = document.getElementById("grafico-competencias");
    if (!canvas)
        return;
    const contagem = {};
    Memoria.candidatos.forEach(candidato => {
        candidato.competencias.forEach(comp => {
            const nome = comp.trim().toUpperCase();
            contagem[nome] = (contagem[nome] || 0) + 1;
        });
    });
    if (chartInstancia)
        chartInstancia.destroy();
    chartInstancia = new Chart(canvas, {
        type: 'bar',
        data: {
            labels: Object.keys(contagem),
            datasets: [{
                    label: 'Número de Candidatos',
                    data: Object.values(contagem),
                    backgroundColor: 'rgba(0, 115, 177, 0.6)',
                    borderColor: 'rgba(0, 115, 177, 1)',
                    borderWidth: 1
                }]
        },
        options: { scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }
    });
}
//# sourceMappingURL=index.js.map