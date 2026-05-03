export class Navigation {
    static iniciar(onRouteChanged) {
        Navigation.onRouteChanged = onRouteChanged;
        const botoes = document.querySelectorAll("#menu-principal button[data-target]");
        botoes.forEach(botao => {
            botao.addEventListener("click", (event) => {
                const target = event.currentTarget.getAttribute("data-target");
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
    static navegarPara(idTela) {
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
    static atualizarMenu() {
        const botoes = document.querySelectorAll("#menu-principal button[data-target]");
        botoes.forEach(botao => {
            const target = botao.getAttribute("data-target");
            if (!target)
                return;
            const permitido = Navigation.podeAcessar(target);
            botao.disabled = !permitido;
        });
    }
    static podeAcessar(idTela) {
        if (Navigation.TELAS_PUBLICAS.has(idTela))
            return true;
        const usuarioLogado = Navigation.obterUsuarioLogado();
        if (!usuarioLogado || !usuarioLogado.tipo)
            return false;
        if (usuarioLogado.tipo === "CANDIDATO") {
            return Navigation.TELAS_CANDIDATO.has(idTela);
        }
        if (usuarioLogado.tipo === "EMPRESA") {
            return Navigation.TELAS_EMPRESA.has(idTela);
        }
        return false;
    }
    static obterUsuarioLogado() {
        const raw = sessionStorage.getItem("usuarioLogado");
        if (!raw)
            return null;
        try {
            return JSON.parse(raw);
        }
        catch (_a) {
            return null;
        }
    }
}
Navigation.TELAS_PUBLICAS = new Set([
    "tela-home",
    "tela-login",
    "tela-cadastro-candidato",
    "tela-cadastro-empresa"
]);
Navigation.TELAS_CANDIDATO = new Set([
    "tela-lista-vagas"
]);
Navigation.TELAS_EMPRESA = new Set([
    "tela-cadastro-vaga",
    "tela-lista-candidatos"
]);
//# sourceMappingURL=Navigation.js.map