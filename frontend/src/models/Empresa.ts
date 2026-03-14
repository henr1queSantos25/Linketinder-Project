import type { IPessoa } from "./Pessoa.js";

export interface IEmpresa extends IPessoa {
    cnpj: string;
    pais: string;
}