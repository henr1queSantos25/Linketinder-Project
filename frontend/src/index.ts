import { Navigation } from "./utils/Navigation.js";
import { EmpresaController } from "./controllers/EmpresaController.js";
import { VagaController } from "./controllers/VagaController.js";
import { CandidatoController } from "./controllers/CandidatoController.js";
import { AuthController } from "./controllers/AuthController.js";
import { ApiService } from "./services/ApiService.js";


declare const Chart: any;
let chartInstancia: any = null;

document.addEventListener("DOMContentLoaded", () => {
    EmpresaController.iniciar();
    VagaController.iniciar();
    CandidatoController.iniciar();
    AuthController.iniciar();

    Navigation.iniciar((idTela) => {
        if (idTela === "tela-lista-vagas") {
            VagaController.renderizar();
        } else if (idTela === "tela-lista-candidatos") {
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

async function renderizarGrafico(): Promise<void> {
    const canvas = document.getElementById("grafico-competencias") as HTMLCanvasElement;
    if (!canvas) return;

    const contagem: Record<string, number> = {};
    try {
        const candidatos = await ApiService.get('/candidatos');
        candidatos.forEach((candidato: any) => {
            const competencias = Array.isArray(candidato.competencias) ? candidato.competencias : [];
            competencias.forEach((comp: string) => {
                const nome = comp.trim().toUpperCase();
                if (!nome) return;
                contagem[nome] = (contagem[nome] || 0) + 1;
            });
        });
    } catch (error) {
        console.error("Erro ao carregar grafico de competencias", error);
    }

    if (chartInstancia) chartInstancia.destroy();

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