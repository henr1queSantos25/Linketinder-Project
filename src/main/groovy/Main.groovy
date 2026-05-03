import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import api.*
import dao.*
import controller.*

class Main {
    static void main(String[] args) {
        ICrudDAO candidatoDAO = new CandidatoDAO()
        ICrudDAO empresaDAO = new EmpresaDAO()
        ICrudDAO vagaDAO = new VagaDAO()

        CandidatoController candidatoCtrl = new CandidatoController(candidatoDAO)
        EmpresaController empresaCtrl = new EmpresaController(empresaDAO)
        VagaController vagaCtrl = new VagaController(vagaDAO)

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0)

        server.createContext("/api/candidatos", new CandidatoHandler(candidatoCtrl))
        server.createContext("/api/empresas", new EmpresaHandler(empresaCtrl))
        server.createContext("/api/vagas", new VagaHandler(vagaCtrl))
        server.createContext("/api/login", new AuthHandler(candidatoCtrl, empresaCtrl))

        server.setExecutor(null)
        server.start()

        println("==================================================")
        println(" Servidor REST iniciado na porta 8080")
        println(" Endpoints ativos:")
        println(" - POST e GET  -> /api/candidatos")
        println(" - POST e GET  -> /api/empresas")
        println(" - POST e GET  -> /api/vagas")
        println(" - POST        -> /api/login")
        println("==================================================")
    }
}