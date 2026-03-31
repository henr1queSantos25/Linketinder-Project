package model

class Vaga {
    Integer id
    Integer empresaId
    String nome
    String descricao
    String local
    List<Competencia> competencias = []
}