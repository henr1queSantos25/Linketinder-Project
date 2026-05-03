package dao

import model.Candidato
import model.Competencia
import util.ConexaoBanco
import util.JdbcBinder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.util.logging.Level
import java.util.logging.Logger

class CandidatoDAO implements ICrudDAO<Candidato> {

    private static final Logger LOGGER = Logger.getLogger(CandidatoDAO.name)

    boolean salvar(Candidato c) {
        Connection conn = ConexaoBanco.conectar()
        if (!conn) return false
        try {
            conn.setAutoCommit(false)
            String sql = "INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            JdbcBinder.bind(stmt, c.nome, c.sobrenome, c.dataNascimento, c.email, c.cpf, c.pais, c.cep, c.descricao, c.senha)
            stmt.executeUpdate()

            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                c.id = rs.getInt(1)
                for (Competencia comp : c.competencias) {
                    Competencia compSalva = CompetenciaDAO.obterOuCriar(comp.nome, conn)
                    if (compSalva) {
                        PreparedStatement stmtRel = conn.prepareStatement("INSERT INTO candidatos_competencias (candidato_id, competencia_id) VALUES (?, ?)")
                        JdbcBinder.bind(stmtRel, c.id, compSalva.id)
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
                LOGGER.log(Level.SEVERE, "Falha ao executar rollback ao salvar candidato.", rollbackEx)
            }
            LOGGER.log(Level.SEVERE, "Falha ao salvar candidato.", e)
            return false
        } finally {
            conn.setAutoCommit(true); conn.close()
        }
    }


    List<Candidato> listar() {
        List<Candidato> lista = []
        Connection conn = ConexaoBanco.conectar()
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM candidatos")
            ResultSet rs = stmt.executeQuery()
            while (rs.next()) {
                Candidato c = new Candidato(
                        id: rs.getInt("id"), nome: rs.getString("nome"), sobrenome: rs.getString("sobrenome"),
                        dataNascimento: rs.getDate("data_nascimento").toLocalDate(), email: rs.getString("email"),
                        cpf: rs.getString("cpf"), pais: rs.getString("pais"), cep: rs.getString("cep"),
                        descricao: rs.getString("descricao"), senha: rs.getString("senha")
                )
                c.competencias = CompetenciaDAO.listarPorCandidato(c.id, conn)
                lista.add(c)
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao listar candidatos.", e)
        }
        finally {
            conn?.close()
        }
        return lista
    }


    boolean atualizar(Candidato c) {
        Connection conn = ConexaoBanco.conectar()
        try {
            conn.setAutoCommit(false)
            String sql = "UPDATE candidatos SET nome=?, sobrenome=?, data_nascimento=?, email=?, cpf=?, pais=?, cep=?, descricao=?, senha=? WHERE id=?"
            PreparedStatement stmt = conn.prepareStatement(sql)

            JdbcBinder.bind(stmt, c.nome, c.sobrenome, c.dataNascimento, c.email, c.cpf, c.pais, c.cep, c.descricao, c.senha, c.id)
            stmt.executeUpdate()

            PreparedStatement stmtDel = conn.prepareStatement("DELETE FROM candidatos_competencias WHERE candidato_id = ?")
            JdbcBinder.bind(stmtDel, c.id)
            stmtDel.executeUpdate()

            for (Competencia comp : c.competencias) {
                Competencia compSalva = CompetenciaDAO.obterOuCriar(comp.nome, conn)
                if (compSalva != null) {
                    PreparedStatement stmtRel = conn.prepareStatement("INSERT INTO candidatos_competencias (candidato_id, competencia_id) VALUES (?, ?)")
                    JdbcBinder.bind(stmtRel, c.id, compSalva.id)
                    stmtRel.executeUpdate()
                }
            }
            conn.commit()
            return true

        } catch (Exception e) {
            try {
                conn.rollback()
            } catch (Exception rollbackEx) {
                LOGGER.log(Level.SEVERE, "Falha ao executar rollback ao atualizar candidato.", rollbackEx)
            }
            LOGGER.log(Level.SEVERE, "Falha ao atualizar candidato.", e)
            return false
        } finally {
            conn.setAutoCommit(true)
            conn?.close()
        }
    }


    boolean deletar(int id) {
        Connection conn = ConexaoBanco.conectar()
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM candidatos WHERE id = ?")
            JdbcBinder.bind(stmt, id)
            return stmt.executeUpdate() > 0
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao deletar candidato com id ${id}.", e)
            return false
        } finally {
            conn?.close()
        }
    }
}