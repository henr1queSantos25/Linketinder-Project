export class Validadores {
    static validarNome(nome) {
        const regex = /^[a-zA-ZÀ-ÿ\s]+$/;
        return regex.test(nome);
    }
    static validarEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }
    static validarCPF(cpf) {
        const regex = /^\d{3}\.\d{3}\.\d{3}-\d{2}$/;
        return regex.test(cpf);
    }
    static validarCNPJ(cnpj) {
        const regex = /^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/;
        return regex.test(cnpj);
    }
    static validarTelefone(telefone) {
        const regex = /^\(\d{2}\)\s\d{4,5}-\d{4}$/;
        return regex.test(telefone);
    }
    static validarLinkedin(link) {
        const regex = /^https:\/\/(www\.)?linkedin\.com\/.*$/;
        return regex.test(link);
    }
    static validarCEP(cep) {
        const regex = /^\d{5}-\d{3}$/;
        return regex.test(cep);
    }
    static validarTags(tags) {
        const regex = /^[a-zA-ZÀ-ÿ0-9\s]+(,[a-zA-ZÀ-ÿ0-9\s]+)*$/;
        return regex.test(tags);
    }
}
//# sourceMappingURL=Validadores.js.map