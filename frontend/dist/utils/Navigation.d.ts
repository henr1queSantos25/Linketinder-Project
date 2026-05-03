export declare class Navigation {
    private static onRouteChanged?;
    private static readonly TELAS_PUBLICAS;
    private static readonly TELAS_CANDIDATO;
    private static readonly TELAS_EMPRESA;
    static iniciar(onRouteChanged?: (idTela: string) => void): void;
    static navegarPara(idTela: string): void;
    static atualizarMenu(): void;
    private static podeAcessar;
    private static obterUsuarioLogado;
}
//# sourceMappingURL=Navigation.d.ts.map