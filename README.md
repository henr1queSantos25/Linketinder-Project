# Linketinder 

## Sobre o Projeto
O Linketinder é um Produto Mínimo Viável (MVP) de um sistema de recrutamento estratégico. Inspirado na dinâmica de conexões de aplicações de relacionamento e no detalhamento de perfil de redes profissionais, o sistema tem como objetivo principal facilitar o encontro entre empresas e candidatos com base no alinhamento de competências.

O projeto está dividido em duas camadas (Backend em Groovy e Frontend em TypeScript) que, nesta fase do MVP, operam de forma independente para validar as regras de negócio, a estrutura de dados e a interface do utilizador.

## Arquitetura e Tecnologias

### Backend
Desenvolvido em Groovy, aplicando conceitos sólidos de Programação Orientada a Objetos (POO), TDD (Test-Driven Development) e inspirado no padrão MVC.
- **Groovy**: Linguagem principal.
- **Gradle**: Ferramenta de automação de builds e gestão de dependências.
- **Spock Framework**: Utilizado para testes unitários e validação de regras de negócio.

### Frontend
Desenvolvido como uma Single Page Application (SPA) simples, estruturada para garantir tipagem forte e validação de dados no lado do cliente.
- **TypeScript**: Superset de JavaScript para garantir tipagem estática e interfaces rigorosas (Models).
- **HTML5 & CSS3**: Estruturação visual e estilização em ecrãs dinâmicos.
- **Node.js & NPM**: Gestão de pacotes e servidor de desenvolvimento local.
- **Chart.js**: Biblioteca utilizada para a renderização de gráficos de dados.

## Funcionalidades
- **Gestão de Perfis**: Cadastro de novos candidatos e empresas através de formulários com validações rigorosas de unicidade (CPF, CNPJ e E-mail) e tratamento de dados (remoção de espaços, formatação de maiúsculas e impedimento de valores inválidos).
- **Listagens Dinâmicas**: Apresentação dos dados em tabelas interativas na interface gráfica.
- **Regra de Anonimato**: Na visão da empresa, os nomes dos candidatos são ocultados (ex: "Candidato Anónimo 1"). O mesmo se aplica às vagas na visão do candidato.
- **Interatividade Visual (Tooltips)**: Exibição de informações detalhadas (idade, descrição, estado) ao posicionar o mouse sobre as linhas das tabelas.
- **Análise de Dados**: Geração de um gráfico de barras dinâmico (Chart.js) na visão da empresa, ilustrando o número de candidatos disponíveis por cada competência.
- **Manutenção de Registos**: Capacidade de eliminar registos específicos do sistema diretamente pela interface.

## Pré-requisitos
Para compilar e executar este projeto localmente, é necessário possuir no seu ambiente:
- **Backend**: Java Development Kit (JDK) 8+, Groovy SDK (3.0+) e IntelliJ IDEA.
- **Frontend**: Node.js (versão 14+) e NPM instalados.

## Como Executar o Projeto

### 1. Executar o Backend
1. Clone este repositório no seu ambiente local.
2. Abra o diretório raiz do projeto no IntelliJ IDEA e aguarde a sincronização do Gradle.
3. Navegue até ao diretório `src/main/groovy/`.
4. Localize o ficheiro `Main.groovy`, clique com o botão direito e selecione `Run 'Main.main()'`.
5. Utilize o terminal da IDE para interagir com o menu CLI.
   *(Para rodar os testes unitários do backend, navegue até `src/test/groovy`, clique com o botão direito e selecione `Run 'Tests in...'`).*

### 2. Executar o Frontend
1. Abra um terminal e navegue para dentro da pasta `frontend` do projeto.
2. Instale as dependências necessárias executando o comando:
   `npm install`
3. Inicie o servidor de desenvolvimento em tempo real executando:
   `npm run dev`
4. O seu navegador padrão deverá abrir automaticamente (normalmente no endereço `http://127.0.0.1:8080`). Caso não abra, aceda ao endereço manualmente para utilizar a interface do Linketinder.

## Desenvolvido por

Henrique Oliveira dos Santos  
[LinkedIn](https://www.linkedin.com/in/dev-henriqueo-santos/)
