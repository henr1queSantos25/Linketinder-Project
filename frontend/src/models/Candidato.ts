import type { IPessoa } from "./Pessoa.js";

export interface ICandidato extends IPessoa {
    sobrenome: string;
    dataNascimento: string;
    email: string;
    cpf: string;
    competencias: string[];
}