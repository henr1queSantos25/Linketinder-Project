import type { ICandidato } from "../models/Candidato.js";
import type { IEmpresa } from "../models/Empresa.js";

export class Memoria {
    static candidatos: ICandidato[] = [];
    static empresas: IEmpresa[] = [];
}