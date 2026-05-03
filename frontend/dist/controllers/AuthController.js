import { ApiService } from "../services/ApiService.js";
import { Navigation } from "../utils/Navigation.js";
export class AuthController {
    static iniciar() {
        const form = document.getElementById("form-login");
        if (form) {
            form.addEventListener("submit", this.login.bind(this));
        }
    }
    static async login(event) {
        event.preventDefault();
        const email = document.getElementById("login-email").value.trim();
        const senha = document.getElementById("login-senha").value;
        const tipo = document.getElementById("login-tipo").value;
        try {
            const resposta = await ApiService.post('/login', { email, senha, tipo });
            alert(`Bem-vindo(a), ${resposta.nome}!`);
            const usuarioLogado = typeof resposta === "object" && resposta !== null
                ? Object.assign(Object.assign({}, resposta), { tipo }) : { nome: email, tipo };
            sessionStorage.setItem('usuarioLogado', JSON.stringify(usuarioLogado));
            Navigation.atualizarMenu();
            if (tipo === 'CANDIDATO') {
                Navigation.navegarPara('tela-lista-vagas');
            }
            else {
                Navigation.navegarPara('tela-lista-candidatos');
                document.dispatchEvent(new Event("dadosAtualizados"));
            }
            event.target.reset();
        }
        catch (error) {
            alert(error.message);
        }
    }
}
//# sourceMappingURL=AuthController.js.map