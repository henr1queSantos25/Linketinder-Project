# Linketinder - MVP

## Sobre o Projeto
O Linketinder é um Produto Mínimo Viável (MVP) de um sistema de recrutamento estratégico. Inspirado na dinâmica de conexões de aplicativos de relacionamento e no detalhamento de perfil de redes profissionais, o sistema tem como objetivo principal facilitar o encontro entre empresas e candidatos com base no alinhamento de competências e skills.

Desenvolvido inteiramente em Groovy, o projeto aplica conceitos sólidos de Programação Orientada a Objetos (POO) e adota uma arquitetura inspirada no padrão MVC (Model, View, Controller). Isso garante a separação clara de responsabilidades, facilitando a manutenção e futuras expansões do código.

## Arquitetura do Sistema
O sistema foi estruturado de forma modular através dos seguintes pacotes:
- **model**: Contém as entidades de domínio do sistema (`Pessoa`, `Candidato`, `Empresa`) e suas interfaces, aplicando os conceitos de herança e abstração.
- **repository**: Responsável pelo armazenamento estático dos dados em memória, atuando como um banco de dados simulado com registros iniciais.
- **controller**: Camada responsável por orquestrar as regras de negócio e validações, assegurando a integridade dos dados antes da persistência.
- **view**: Interface de interação direta com o usuário através de menus no terminal (CLI).

## Funcionalidades
- Visualização da lista de candidatos pré-cadastrados, incluindo dados pessoais, descrição e competências técnicas.
- Visualização da lista de empresas pré-cadastradas e as competências esperadas de futuros colaboradores.
- Cadastro dinâmico de novos candidatos, com validação de sistema para impedir a duplicidade de CPFs.
- Cadastro dinâmico de novas empresas, com validação de sistema para impedir a duplicidade de CNPJs.
- Navegação fluida via menu interativo em linha de comando.

## Pré-requisitos
Para compilar e executar este projeto localmente, é necessário possuir os seguintes componentes configurados em seu ambiente:
- Java Development Kit (JDK) 8 ou superior.
- Groovy SDK (versão 3.0 ou superior).
- IDE recomendada: IntelliJ IDEA.

## Como Executar
1. Clone o repositório para o seu ambiente local utilizando o comando:
   `git clone <https://github.com/henr1queSantos25/Linketinder-Project>`
2. Abra o diretório raiz do projeto na sua IDE (recomenda-se o IntelliJ IDEA).
3. Aguarde a indexação e certifique-se de que o SDK do Groovy está devidamente mapeado nas configurações do projeto (`File > Project Structure > Global Libraries`).
4. Navegue até o diretório `src/`.
5. Localize o arquivo `Main.groovy`, clique com o botão direito e selecione a opção `Run 'Main.main()'`.
6. Utilize o console/terminal da IDE para interagir com o menu da aplicação.

## Desenvolvido por

**Henrique Oliveira dos Santos**  
[LinkedIn](https://www.linkedin.com/in/dev-henriqueo-santos/)