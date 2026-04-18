package dao

import model.Candidato
import model.Competencia
import util.ConexaoBanco
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class CandidatoDAO {

    boolean salvar(Candidato c) {
        Connection conn = ConexaoBanco.conectar()
        if (!conn) return false
        try {
            conn.setAutoCommit(false)
            String sql = "INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            stmt.setString(1, c.nome)
            stmt.setString(2, c.sobrenome)
            stmt.setDate(3, java.sql.Date.valueOf(c.dataNascimento))
            stmt.setString(4, c.email)
            stmt.setString(5, c.cpf)
            stmt.setString(6, c.pais)
            stmt.setString(7, c.cep)
            stmt.setString(8, c.descricao)
            stmt.setString(9, c.senha)
            stmt.executeUpdate()

            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                c.id = rs.getInt(1)
                for (Competencia comp : c.competencias) {
                    Competencia compSalva = CompetenciaDAO.obterOuCriar(comp.nome, conn)
                    if (compSalva) {
                        PreparedStatement stmtRel = conn.prepareStatement("INSERT INTO candidatos_competencias (candidato_id, competencia_id) VALUES (?, ?)")
                        stmtRel.setInt(1, c.id)
                        stmtRel.setInt(2, compSalva.id)
                        stmtRel.executeUpdate()
                    }
                }
            }
            conn.commit()
            return true
        } catch (Exception e) {
            conn.rollback()
            return false
        } finally {
            conn.setAutoCommit(true); conn.close()
        }
    }


    List<Candidato> listarTodos() {
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
        } catch (Exception e) {}
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

            stmt.setString(1, c.nome)
            stmt.setString(2, c.sobrenome)
            stmt.setDate(3, java.sql.Date.valueOf(c.dataNascimento))
            stmt.setString(4, c.email)
            stmt.setString(5, c.cpf)
            stmt.setString(6, c.pais)
            stmt.setString(7, c.cep)
            stmt.setString(8, c.descricao)
            stmt.setString(9, c.senha)
            stmt.setInt(10, c.id)
            stmt.executeUpdate()

            PreparedStatement stmtDel = conn.prepareStatement("DELETE FROM candidatos_competencias WHERE candidato_id = ?")
            stmtDel.setInt(1, c.id)
            stmtDel.executeUpdate()

            for (Competencia comp : c.competencias) {
                Competencia compSalva = CompetenciaDAO.obterOuCriar(comp.nome, conn)
                if (compSalva != null) {
                    PreparedStatement stmtRel = conn.prepareStatement("INSERT INTO candidatos_competencias (candidato_id, competencia_id) VALUES (?, ?)")
                    stmtRel.setInt(1, c.id)
                    stmtRel.setInt(2, compSalva.id)
                    stmtRel.executeUpdate()
                }
            }
            conn.commit()
            return true

        } catch (Exception e) {
            conn.rollback()
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
            stmt.setInt(1, id)
            return stmt.executeUpdate() > 0
        } catch (Exception e) {
            return false
        } finally {
            conn?.close()
        }
    }
}