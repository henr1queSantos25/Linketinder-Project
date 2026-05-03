export class Navigation {
    private static onRouteChanged?: (idTela: string) => void;

    private static readonly TELAS_PUBLICAS = new Set([
        "tela-home",
        "tela-login",
        "tela-cadastro-candidato",
        "tela-cadastro-empresa"
    ]);

    private static readonly TELAS_CANDIDATO = new Set([
        "tela-lista-vagas"
    ]);

    private static readonly TELAS_EMPRESA = new Set([
        "tela-cadastro-vaga",
        "tela-lista-candidatos"
    ]);
    
    static iniciar(onRouteChanged?: (idTela: string) => void): void {
        Navigation.onRouteChanged = onRouteChanged;
        const botoes = document.querySelectorAll("#menu-principal button[data-target]");
        
        botoes.forEach(botao => {
            botao.addEventListener("click", (event) => {
                const target = (event.currentTarget as HTMLButtonElement).getAttribute("data-target");
                if (target) {
                    Navigation.navegarPara(target);
                }
            });
        });

        Navigation.atualizarMenu();

        const usuarioLogado = Navigation.obterUsuarioLogado();
        if (!usuarioLogado) {
            Navigation.navegarPara("tela-login");
        }
    }

    static navegarPara(idTela: string): void {
        const destino = Navigation.podeAcessar(idTela) ? idTela : "tela-login";
        if (destino !== idTela) {
            alert("Faca login para acessar esta tela.");
        }

        const secoes = document.querySelectorAll("main section");
        secoes.forEach(secao => secao.classList.add("hidden"));

        const telaAlvo = document.getElementById(destino);
        if (telaAlvo) {
            telaAlvo.classList.remove("hidden");
        }

        if (Navigation.onRouteChanged) {
            Navigation.onRouteChanged(destino);
        }
    }

    static atualizarMenu(): void {
        const botoes = document.querySelectorAll("#menu-principal button[data-target]");
        botoes.forEach(botao => {
            const target = botao.getAttribute("data-target");
            if (!target) return;

            const permitido = Navigation.podeAcessar(target);
            (botao as HTMLButtonElement).disabled = !permitido;
        });
    }

    private static podeAcessar(idTela: string): boolean {
        if (Navigation.TELAS_PUBLICAS.has(idTela)) return true;

        const usuarioLogado = Navigation.obterUsuarioLogado();
        if (!usuarioLogado || !usuarioLogado.tipo) return false;

        if (usuarioLogado.tipo === "CANDIDATO") {
            return Navigation.TELAS_CANDIDATO.has(idTela);
        }

        if (usuarioLogado.tipo === "EMPRESA") {
            return Navigation.TELAS_EMPRESA.has(idTela);
        }

        return false;
    }

    private static obterUsuarioLogado(): { tipo?: string } | null {
        const raw = sessionStorage.getItem("usuarioLogado");
        if (!raw) return null;

        try {
            return JSON.parse(raw) as { tipo?: string };
        } catch {
            return null;
        }
    }
}