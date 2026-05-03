package dao

interface IInteracaoDAO {
    boolean curtirVaga(int candidatoId, int vagaId)
    Integer curtirCandidato(int empresaId, int candidatoId)
}