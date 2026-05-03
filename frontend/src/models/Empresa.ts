import type { IPessoa } from "./Pessoa.js";

export interface IEmpresa extends IPessoa {
    cnpj: string;
    emailCorporativo: string;
}