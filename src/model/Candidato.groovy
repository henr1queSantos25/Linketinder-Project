package model

class Candidato extends Pessoa {
    String cpf
    Integer idade

    // Construtor
    Candidato(String nome, String email, String cpf, Integer idade, String estado, String cep, String descricao, List<String> competencias) {
        this.nome = nome
        this.email = email
        this.cpf = cpf
        this.idade = idade
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias
    }
}
