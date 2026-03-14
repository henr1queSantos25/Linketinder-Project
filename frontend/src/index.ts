function navegar(idTela: string): void {
    console.log(`Navegando para a tela: ${idTela}`);

}

(window as any).navegar = navegar;

console.log("Linketinder Frontend inicializado com sucesso!");