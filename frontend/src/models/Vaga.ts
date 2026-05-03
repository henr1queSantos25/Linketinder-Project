export interface IVaga {
    id?: number;
    empresaId: number;
    nome: string;
    descricao: string;
    local: string;
    competencias: string[];
}