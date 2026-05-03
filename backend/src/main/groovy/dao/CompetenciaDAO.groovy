package dao

import model.Competencia
import util.ConexaoBanco
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.util.logging.Level
import java.util.logging.Logger

class CompetenciaDAO {

    private static final Logger LOGGER = Logger.getLogger(CompetenciaDAO.name)

    static Competencia obterOuCriar(String nomeCompetencia, Connection conn) {
        String nomeFormatado = nomeCompetencia.trim()
        try {
            String sqlSelect = "SELECT id, nome FROM competencias WHERE UPPER(nome) = UPPER(?)"
            PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)
            stmtSelect.setString(1, nomeFormatado)
            ResultSet rs = stmtSelect.executeQuery()

            if (rs.next()) {
                return new Competencia(id: rs.getInt("id"), nome: rs.getString("nome"))
            }

            String sqlInsert = "INSERT INTO competencias (nome) VALUES (?)"
            PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)
            stmtInsert.setString(1, nomeFormatado)
            stmtInsert.executeUpdate()

            ResultSet rsInsert = stmtInsert.getGeneratedKeys()
            if (rsInsert.next()) {
                return new Competencia(id: rsInsert.getInt(1), nome: nomeFormatado)
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao obter ou criar competencia: ${nomeCompetencia}", e)
        }
        return null
    }


    static List<Competencia> listarPorCandidato(int candidatoId, Connection conn) {
        List<Competencia> comps = []
        String sql = "SELECT c.id, c.nome FROM competencias c JOIN candidatos_competencias cc ON c.id = cc.competencia_id WHERE cc.candidato_id = ?"
        try {
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setInt(1, candidatoId)
            ResultSet rs = stmt.executeQuery()
            while(rs.next()) {
                comps.add(new Competencia(id: rs.getInt("id"), nome: rs.getString("nome")))
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao listar competencias do candidato com id ${candidatoId}.", e)
        }
        return comps
    }

    static List<Competencia> listarPorVaga(int vagaId, Connection conn) {
        List<Competencia> comps = []
        String sql = "SELECT c.id, c.nome FROM competencias c JOIN vagas_competencias vc ON c.id = vc.competencia_id WHERE vc.vaga_id = ?"
        try {
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setInt(1, vagaId)
            ResultSet rs = stmt.executeQuery()
            while(rs.next()) {
                comps.add(new Competencia(id: rs.getInt("id"), nome: rs.getString("nome")))
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao listar competencias da vaga com id ${vagaId}.", e)
        }
        return comps
    }


    boolean atualizar(Competencia comp) {
        Connection conn = ConexaoBanco.conectar()
        try {
            String sql = "UPDATE competencias SET nome = ? WHERE id = ?"
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setString(1, comp.nome)
            stmt.setInt(2, comp.id)
            return stmt.executeUpdate() > 0
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao atualizar competencia com id ${comp?.id}.", e)
            return false
        } finally {
            conn?.close()
        }
    }


    boolean deletar(int id) {
        Connection conn = ConexaoBanco.conectar()
        try {
            String sql = "DELETE FROM competencias WHERE id = ?"
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setInt(1, id)
            return stmt.executeUpdate() > 0
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Falha ao deletar competencia com id ${id}.", e)
            return false
        } finally {
            conn?.close()
        }
    }
}