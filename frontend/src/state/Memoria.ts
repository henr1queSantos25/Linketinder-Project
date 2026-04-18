import type { ICandidato } from "../models/Candidato.js";
import type { IEmpresa } from "../models/Empresa.js";
import type { IVaga } from "../models/Vaga.js";

export class Memoria {
    static candidatos: ICandidato[] = [];
    static empresas: IEmpresa[] = [];
    static vagas: IVaga[] = [];
}