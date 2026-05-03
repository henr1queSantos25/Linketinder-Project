import { ApiService } from "../services/ApiService.js";
import { Navigation } from "../utils/Navigation.js";

export class AuthController {
    static iniciar(): void {
        const form = document.getElementById("form-login") as HTMLFormElement;
        if (form) {
            form.addEventListener("submit", this.login.bind(this));
        }
    }

    private static async login(event: Event): Promise<void> {
        event.preventDefault();
        
        const email = (document.getElementById("login-email") as HTMLInputElement).value.trim();
        const senha = (document.getElementById("login-senha") as HTMLInputElement).value;
        const tipo = (document.getElementById("login-tipo") as HTMLSelectElement).value;

        try {
            const resposta = await ApiService.post('/login', { email, senha, tipo });
            
            alert(`Bem-vindo(a), ${resposta.nome}!`);
            const usuarioLogado = typeof resposta === "object" && resposta !== null
                ? { ...resposta, tipo }
                : { nome: email, tipo };

            sessionStorage.setItem('usuarioLogado', JSON.stringify(usuarioLogado));
            Navigation.atualizarMenu();
            
            if (tipo === 'CANDIDATO') {
                Navigation.navegarPara('tela-lista-vagas');
            } else {
                Navigation.navegarPara('tela-lista-candidatos');
                document.dispatchEvent(new Event("dadosAtualizados"));
            }
            
            (event.target as HTMLFormElement).reset();

        } catch (error: any) {
            alert(error.message); 
        }
    }
}