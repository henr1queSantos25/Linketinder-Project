package repository

import model.Candidato
import model.Empresa

class Memoria {

    static List<Candidato> candidatos = [
            new Candidato(
                    nome: 'Henrique Oliveira Santos',
                    email: 'hos@email.com',
                    cpf: '111.111.111-11',
                    idade: 19,
                    estado: 'BA',
                    cep: '45836-000',
                    descricao: 'Focado em backend e sistemas embarcados. Gosto de resolver problemas complexos.',
                    competencias: ['Java', 'Spring Framework', 'Linux', 'IoT']
            ),
            new Candidato(
                    nome: 'Igor',
                    email: 'igor@email.com',
                    cpf: '222.222.222-22',
                    idade: 23,
                    estado: 'BA',
                    cep: '45000-000',
                    descricao: 'Perfil híbrido com excelente visão de negócios e comunicação.',
                    competencias: ['Business', 'Comunicação', 'Gestão Ágil', 'Groovy']
            ),
            new Candidato(
                    nome: 'Ana Silva',
                    email: 'ana@email.com',
                    cpf: '333.333.333-33',
                    idade: 25,
                    estado: 'SP',
                    cep: '01000-000',
                    descricao: 'Desenvolvedora Frontend apaixonada por UI/UX.',
                    competencias: ['Angular', 'TypeScript', 'HTML', 'CSS']
            ),
            new Candidato(
                    nome: 'Carlos Souza',
                    email: 'carlos@email.com',
                    cpf: '444.444.444-44',
                    idade: 30,
                    estado: 'MG',
                    cep: '30000-000',
                    descricao: 'Engenheiro de Dados focado em arquiteturas escaláveis.',
                    competencias: ['Python', 'SQL', 'Machine Learning']
            ),
            new Candidato(
                    nome: 'Beatriz Costa',
                    email: 'bia@email.com',
                    cpf: '555.555.555-55',
                    idade: 28,
                    estado: 'RJ',
                    cep: '20000-000',
                    descricao: 'Especialista em Cloud Computing e infraestrutura.',
                    competencias: ['AWS', 'Linux', 'Terraform']
            )
    ]

    static List<Empresa> empresas = [
            new Empresa(
                    nome: 'Arroz-Gostoso',
                    email: 'rh@arrozgostoso.com',
                    cnpj: '11.111.111/0001-11',
                    pais: 'Brasil',
                    estado: 'GO',
                    cep: '74000-000',
                    descricao: 'A maior produtora de grãos do centro-oeste.',
                    competencias: ['Gestão', 'Logística', 'Business']
            ),
            new Empresa(
                    nome: 'Império do Boliche',
                    email: 'contato@imperioboliche.com',
                    cnpj: '22.222.222/0001-22',
                    pais: 'Brasil',
                    estado: 'SP',
                    cep: '01000-000',
                    descricao: 'Rede de entretenimento familiar.',
                    competencias: ['Atendimento', 'Administração', 'Comunicação']
            ),
            new Empresa(
                    nome: 'ZG Soluções',
                    email: 'vagas@zg.com.br',
                    cnpj: '33.333.333/0001-33',
                    pais: 'Brasil',
                    estado: 'GO',
                    cep: '74000-000',
                    descricao: 'Tecnologia focada em facilitar o faturamento hospitalar.',
                    competencias: ['Groovy', 'Java', 'Spring Framework', 'SQL']
            ),
            new Empresa(
                    nome: 'Tech Embarcados',
                    email: 'jobs@techembarcados.com',
                    cnpj: '44.444.444/0001-44',
                    pais: 'Brasil',
                    estado: 'SC',
                    cep: '88000-000',
                    descricao: 'Inovação em IoT e Edge Computing para a indústria 4.0.',
                    competencias: ['C++', 'Linux', 'IoT', 'Python']
            ),
            new Empresa(
                    nome: 'DataCorp',
                    email: 'rh@datacorp.com',
                    cnpj: '55.555.555/0001-55',
                    pais: 'Brasil',
                    estado: 'MG',
                    cep: '30000-000',
                    descricao: 'Consultoria especializada em Big Data e IA.',
                    competencias: ['Python', 'Machine Learning', 'SQL']
            )
    ]
}
