# Linketinder - MVP

## Sobre o Projeto
O Linketinder é um Produto Mínimo Viável (MVP) de um sistema de recrutamento estratégico. Inspirado na dinâmica de conexões de aplicativos de relacionamento e no detalhamento de perfil de redes profissionais, o sistema tem como objetivo principal facilitar o encontro entre empresas e candidatos com base no alinhamento de competências e skills.

Desenvolvido inteiramente em Groovy, o projeto aplica conceitos sólidos de Programação Orientada a Objetos (POO), TDD (Test-Driven Development) e adota uma arquitetura inspirada no padrão MVC (Model, View, Controller). Isso garante a separação clara de responsabilidades, facilitando a manutenção, elaboração de testes unitários e futuras expansões do código. O gerenciamento de dependências e a automação de builds do projeto são realizados através do Gradle.

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
- Testes Unitários: Cobertura automatizada das regras de negócio (validação e armazenamento) para assegurar a consistência dos dados.

## Tecnologias e Ferramentas
- Groovy
- Gradle (Build Tool)
- Spock Framework (Testes Unitários)

## Pré-requisitos
Para compilar e executar este projeto localmente, é necessário possuir os seguintes componentes configurados em seu ambiente:
- Java Development Kit (JDK) 8 ou superior.
- Groovy SDK (versão 3.0 ou superior).
- IDE recomendada: IntelliJ IDEA.

## Como Executar a Aplicação
1. Clone o repositório para o seu ambiente local utilizando o comando:
   `git clone https://github.com/henr1queSantos25/Linketinder-Project`
2. Abra o diretório raiz do projeto na sua IDE.
3. Aguarde a sincronização automática do Gradle.
4. Navegue até o diretório `src/main/groovy/`.
5. Localize o arquivo `Main.groovy`, clique com o botão direito e selecione a opção `Run 'Main.main()'`.
6. Utilize o console/terminal da IDE para interagir com o menu.

## Como Executar os Testes
Os testes unitários do projeto foram desenvolvidos utilizando o Spock Framework para garantir a integridade dos Controllers. Para executá-los:

- **Através da IDE (IntelliJ)**: Navegue até a pasta `src/test/groovy`, clique com o botão direito sobre ela (ou sobre uma classe de teste específica) e selecione `Run 'Tests in...'`.
- **Através do Terminal (via Gradle)**: Na raiz do projeto, execute um dos seguintes comandos:
   - Linux/Mac: `./gradlew test`
   - Windows: `gradlew.bat test`

## Desenvolvido por

**Henrique Oliveira dos Santos**  
[LinkedIn](https://www.linkedin.com/in/dev-henriqueo-santos/)