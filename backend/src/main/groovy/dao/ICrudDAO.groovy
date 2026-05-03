package dao

interface ICrudDAO<T> {
    boolean salvar(T entidade)
    List<T> listar()
    boolean atualizar(T entidade)
    boolean deletar(int id)
}