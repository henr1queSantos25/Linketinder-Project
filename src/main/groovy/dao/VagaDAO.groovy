package dao

import model.Competencia
import model.Vaga
import util.ConexaoBanco
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class VagaDAO {

    boolean salvar(Vaga v) {
        Connection conn = ConexaoBanco.conectar()
        if (!conn) return false
        try {
            conn.setAutoCommit(false)
            String sql = "INSERT INTO vagas (empresa_id, nome, descricao, local) VALUES (?, ?, ?, ?)"
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            stmt.setInt(1, v.empresaId)
            stmt.setString(2, v.nome)
            stmt.setString(3, v.descricao)
            stmt.setString(4, v.local)
            stmt.executeUpdate()

            ResultSet rs = stmt.getGeneratedKeys()
            if (rs.next()) {
                v.id = rs.getInt(1)

                for (Competencia comp : v.competencias) {
                    Competencia compSalva = CompetenciaDAO.obterOuCriar(comp.nome, conn)
                    if (compSalva != null) {
                        PreparedStatement stmtRel = conn.prepareStatement("INSERT INTO vagas_competencias (vaga_id, competencia_id) VALUES (?, ?)")
                        stmtRel.setInt(1, v.id)
                        stmtRel.setInt(2, compSalva.id)
                        stmtRel.executeUpdate()
                    }
                }
            }
            conn.commit()
            return true
        } catch (Exception e) {
            conn.rollback()
            println("[ERRO] Falha ao cadastrar vaga: ${e.message}")
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
        } catch (Exception e) { println(e.message) } finally { conn?.close() }
        return lista
    }


    boolean atualizar(Vaga v) {
        Connection conn = ConexaoBanco.conectar()
        try {
            conn.setAutoCommit(false)
            String sql = "UPDATE vagas SET empresa_id=?, nome=?, descricao=?, local=? WHERE id=?"
            PreparedStatement stmt = conn.prepareStatement(sql)
            stmt.setInt(1, v.empresaId); stmt.setString(2, v.nome); stmt.setString(3, v.descricao)
            stmt.setString(4, v.local); stmt.setInt(5, v.id)
            stmt.executeUpdate()

            PreparedStatement stmtDel = conn.prepareStatement("DELETE FROM vagas_competencias WHERE vaga_id = ?")
            stmtDel.setInt(1, v.id)
            stmtDel.executeUpdate()

            for (Competencia comp : v.competencias) {
                Competencia compSalva = CompetenciaDAO.obterOuCriar(comp.nome, conn)
                if (compSalva != null) {
                    PreparedStatement stmtRel = conn.prepareStatement("INSERT INTO vagas_competencias (vaga_id, competencia_id) VALUES (?, ?)")
                    stmtRel.setInt(1, v.id); stmtRel.setInt(2, compSalva.id); stmtRel.executeUpdate()
                }
            }
            conn.commit()
            return true
        } catch (Exception e) {
            conn.rollback()
            println("[ERRO CRÍTICO] Falha ao atualizar vaga: ${e.message}")
            return false
        } finally { conn.setAutoCommit(true); conn?.close() }
    }


    boolean deletar(int id) {
        Connection conn = ConexaoBanco.conectar()
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM vagas WHERE id = ?")
            stmt.setInt(1, id)
            return stmt.executeUpdate() > 0
        } catch (Exception e) { return false } finally { conn?.close() }
    }
}