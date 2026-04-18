export class Navigation {
    
    static iniciar(onRouteChanged?: (idTela: string) => void): void {
        const botoes = document.querySelectorAll("#menu-principal button[data-target]");
        
        botoes.forEach(botao => {
            botao.addEventListener("click", (event) => {
                const target = (event.currentTarget as HTMLButtonElement).getAttribute("data-target");
                if (target) {
                    Navigation.navegarPara(target);
                    if (onRouteChanged) onRouteChanged(target);
                }
            });
        });
    }

    private static navegarPara(idTela: string): void {
        const secoes = document.querySelectorAll("main section");
        secoes.forEach(secao => secao.classList.add("hidden"));

        const telaAlvo = document.getElementById(idTela);
        if (telaAlvo) {
            telaAlvo.classList.remove("hidden");
        }
    }
}