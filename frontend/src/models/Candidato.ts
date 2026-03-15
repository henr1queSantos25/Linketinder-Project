import type { IPessoa } from "./Pessoa.js";

export interface ICandidato extends IPessoa {
    cpf: string;
    idade: number;
    telefone: string;
    linkedin: string;
}