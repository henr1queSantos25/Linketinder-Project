package dao

import model.Competencia
import model.Vaga
import util.ConexaoBanco
import util.JdbcBinder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.util.logging.Level
import java.util.logging.Logger

class VagaDAO {

    private static final Logger LOGGER = Logger.getLogger(VagaDAO.name)

    boolean salvar(Vaga v) {
        Connection conn = ConexaoBanco.conectar()
        if (!conn) return false
        try {
            conn.setAutoCommit(false)
            String sql = "INSERT INTO vagas (empresa_id, nome, descricao, local) VALUES (?, ?, ?, ?)"
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            JdbcBinder.bind(stmt, v.empresaId, v.nome, v.descricao, v.local)
            stmt.executeUpdate()

            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                v.id = rs.getInt(1)

                for (Competencia comp : v.competencias) {
                    Competencia compSalva = CompetenciaDAO.obterOuCriar(comp.nome, conn)
                    if (compSalva != null) {
                        PreparedStatement stmtRel = conn.prepareStatement("INSERT INTO vagas_competencias (vaga_id, competencia_id) VALUES (?, ?)")
                        JdbcBinder.bind(stmtRel, v.id, compSalva.id)
                        stmtRel.executeUpdate()
                    }
                }
            }
            conn.commit()
            return true
        } catch (Exception e) {
            try {
                conn.rollback()
            } catch (Exception rollbackEx) {
                LOGGER.log(Level.SEVERE, "Falha ao executar rollback ao salvar vaga.", rollbackEx)
            }
            LOGGER.log(Level.SEVERE, "Falha ao salvar vaga.", e)
            return false
        } finally { conn.setAutoCommit(true); conn?.close() }
    }


    List<Vaga> listarTodas() {
        List<Vaga> lista = []
        Connection conn = ConexaoBanco.conectar()
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vagas")
            ResultSet rs = stmt.executeQuery()
            while (rs.next()) {
                Vaga v = new Vaga(
                        id: rs.getInt("id"), empresaId: rs.getInt("empresa_id"),
                        nome: rs.getString("nome"), descricao: rs.getString("descricao"), local: rs.getString("local")
                )

                v.competencias = CompetenciaDAO.listarPorVaga(v.id, conn)
                lista.add(v)
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao listar vagas.", e)
        }
        finally {
            conn?.close()
        }
        return lista
    }


    boolean atualizar(Vaga v) {
        Connection conn = ConexaoBanco.conectar()
        try {
            conn.setAutoCommit(false)
            String sql = "UPDATE vagas SET empresa_id=?, nome=?, descricao=?, local=? WHERE id=?"
            PreparedStatement stmt = conn.prepareStatement(sql)

            JdbcBinder.bind(stmt, v.empresaId, v.nome, v.descricao, v.local, v.id)
            stmt.executeUpdate()

            PreparedStatement stmtDel = conn.prepareStatement("DELETE FROM vagas_competencias WHERE vaga_id = ?")
            JdbcBinder.bind(stmtDel, v.id)
            stmtDel.executeUpdate()

            for (Competencia comp : v.competencias) {
                Competencia compSalva = CompetenciaDAO.obterOuCriar(comp.nome, conn)
                if (compSalva != null) {
                    PreparedStatement stmtRel = conn.prepareStatement("INSERT INTO vagas_competencias (vaga_id, competencia_id) VALUES (?, ?)")
                        JdbcBinder.bind(stmtRel, v.id, compSalva.id)
                        stmtRel.executeUpdate()
                }
            }
            conn.commit()
            return true
        } catch (Exception e) {
            try {
                conn.rollback()
            } catch (Exception rollbackEx) {
                LOGGER.log(Level.SEVERE, "Falha ao executar rollback ao atualizar vaga.", rollbackEx)
            }
            LOGGER.log(Level.SEVERE, "Falha ao atualizar vaga com id ${v?.id}.", e)
            return false
        } finally {
            conn.setAutoCommit(true); conn?.close()
        }
    }


    boolean deletar(int id) {
        Connection conn = ConexaoBanco.conectar()
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM vagas WHERE id = ?")
            JdbcBinder.bind(stmt, id)
            return stmt.executeUpdate() > 0
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao deletar vaga com id ${id}.", e)
            return false
        } finally {
            conn?.close()
        }
    }
}