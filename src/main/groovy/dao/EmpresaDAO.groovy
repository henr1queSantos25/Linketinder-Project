package dao

import model.Candidato
import model.Empresa
import util.ConexaoBanco
import util.JdbcBinder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.util.logging.Level
import java.util.logging.Logger

class EmpresaDAO implements ICrudDAO<Empresa>{

    private static final Logger LOGGER = Logger.getLogger(EmpresaDAO.name)

    boolean salvar(Empresa e) {
        Connection conn = ConexaoBanco.conectar()
        try {
            String sql = "INSERT INTO empresas (nome, cnpj, email_corporativo, descricao, pais, cep, senha) VALUES (?, ?, ?, ?, ?, ?, ?)"
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            JdbcBinder.bind(stmt, e.nome, e.cnpj, e.emailCorporativo, e.descricao, e.pais, e.cep, e.senha)
            stmt.executeUpdate()

            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                e.id = rs.getInt(1)
            }
            return true
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Falha ao salvar empresa.", ex)
            return false
        } finally {
            conn?.close()
        }
    }


    List<Empresa> listar() {
        List<Empresa> lista = []
        Connection conn = ConexaoBanco.conectar()
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM empresas")
            ResultSet rs = stmt.executeQuery()
            while (rs.next()) {
                lista.add(new Empresa(
                        id: rs.getInt("id"), nome: rs.getString("nome"), cnpj: rs.getString("cnpj"),
                        emailCorporativo: rs.getString("email_corporativo"), descricao: rs.getString("descricao"),
                        pais: rs.getString("pais"), cep: rs.getString("cep"), senha: rs.getString("senha")
                ))
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Falha ao listar empresas.", ex)
        } finally {
            conn?.close()
        }
        return lista
    }


    boolean atualizar(Empresa e) {
        Connection conn = ConexaoBanco.conectar()
        try {
            String sql = "UPDATE empresas SET nome=?, cnpj=?, email_corporativo=?, descricao=?, pais=?, cep=?, senha=? WHERE id=?"
            PreparedStatement stmt = conn.prepareStatement(sql)

            JdbcBinder.bind(stmt, e.nome, e.cnpj, e.emailCorporativo, e.descricao, e.pais, e.cep, e.senha, e.id)

            return stmt.executeUpdate() > 0
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Falha ao atualizar empresa com id ${e?.id}.", ex)
            return false
        } finally {
            conn?.close()
        }
    }


    boolean deletar(int id) {
        Connection conn = ConexaoBanco.conectar()
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM empresas WHERE id = ?")
            JdbcBinder.bind(stmt, id)
            return stmt.executeUpdate() > 0
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Falha ao deletar empresa com id ${id}.", ex)
            return false
        } finally {
            conn?.close()
        }
    }
}