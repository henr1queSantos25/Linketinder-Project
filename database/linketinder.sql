-- 1. CRIAÇÃO DAS TABELAS PRINCIPAIS
CREATE TABLE candidatos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    pais VARCHAR(50) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    descricao TEXT,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE empresas (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    email_corporativo VARCHAR(100) UNIQUE NOT NULL,
    descricao TEXT,
    pais VARCHAR(50) NOT NULL,
    cep VARCHAR(10) NOT NULL,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE competencias (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE vagas (
    id SERIAL PRIMARY KEY,
    empresa_id INT REFERENCES empresas(id) ON DELETE CASCADE,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    local VARCHAR(100) NOT NULL
);


-- 2. CRIAÇÃO DAS TABELAS ASSOCIATIVAS (N:N)
CREATE TABLE candidatos_competencias (
    candidato_id INT REFERENCES candidatos(id) ON DELETE CASCADE,
    competencia_id INT REFERENCES competencias(id) ON DELETE CASCADE,
    PRIMARY KEY (candidato_id, competencia_id)
);

CREATE TABLE vagas_competencias (
    vaga_id INT REFERENCES vagas(id) ON DELETE CASCADE,
    competencia_id INT REFERENCES competencias(id) ON DELETE CASCADE,
    PRIMARY KEY (vaga_id, competencia_id)
);

CREATE TABLE curtidas_candidatos (
    candidato_id INT REFERENCES candidatos(id) ON DELETE CASCADE,
    vaga_id INT REFERENCES vagas(id) ON DELETE CASCADE,
    PRIMARY KEY (candidato_id, vaga_id)
);

CREATE TABLE curtidas_empresas (
    empresa_id INT REFERENCES empresas(id) ON DELETE CASCADE,
    candidato_id INT REFERENCES candidatos(id) ON DELETE CASCADE,
    PRIMARY KEY (empresa_id, candidato_id)
);


-- 3. INSERÇÃO DE DADOS
INSERT INTO competencias (nome) VALUES
    ('Python'), ('Java'), ('Groovy'), ('Angular'), ('Spring Framework');


INSERT INTO candidatos (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha) VALUES
    ('Sandubinha', 'Silva', '1995-05-20', 'sandubinha@email.com', '111.111.111-11', 'Brasil', '12345-678', 'Desenvolvedor apaixonado por backend', 'senha12345'),
    ('Maria', 'Oliveira', '1998-08-15', 'maria@email.com', '222.222.222-22', 'Brasil', '23456-789', 'Especialista em frontend', 'senha12345'),
    ('João', 'Santos', '1990-11-10', 'joao@email.com', '333.333.333-33', 'Brasil', '34567-890', 'Engenheiro de dados experiente', 'senha12345'),
    ('Ana', 'Costa', '1992-02-25', 'ana@email.com', '444.444.444-44', 'Brasil', '45678-901', 'Desenvolvedora Fullstack', 'senha12345'),
    ('Pedro', 'Almeida', '1997-07-30', 'pedro@email.com', '555.555.555-55', 'Brasil', '56789-012', 'Focado em mobile e performance', 'senha12345');


INSERT INTO empresas (nome, cnpj, email_corporativo, descricao, pais, cep, senha) VALUES
    ('Pastelsoft', '11.111.111/0001-11', 'rh@pastelsoft.com', 'Especializada em ERPs para restaurantes', 'Brasil', '11111-111', 'senha12345'),
    ('TechBurger', '22.222.222/0001-22', 'vagas@techburger.com', 'Inovação em fast food', 'Brasil', '22222-222', 'senha12345'),
    ('DataPizza', '33.333.333/0001-33', 'jobs@datapizza.com', 'Análise de dados para pizzarias', 'Brasil', '33333-333', 'senha12345'),
    ('SushiCode', '44.444.444/0001-44', 'carreiras@sushicode.com', 'Software para culinária oriental', 'Brasil', '44444-444', 'senha12345'),
    ('BoloTech', '55.555.555/0001-55', 'rh@bolotech.com', 'Tecnologia para confeitarias', 'Brasil', '55555-555', 'senha12345');


INSERT INTO vagas (empresa_id, nome, descricao, local) VALUES
    (1, 'Desenvolvedor Backend Pleno', 'Vaga para trabalhar com Spring na Pastelsoft', 'São Paulo - SP'),
    (1, 'Desenvolvedor Frontend Sênior', 'Vaga para atuar com Angular na Pastelsoft', 'Remoto'),
    (2, 'Analista de Dados', 'Trabalhar com Python e SQL', 'Rio de Janeiro - RJ'),
    (3, 'Engenheiro de Software', 'Foco em Groovy e Java', 'Belo Horizonte - MG'),
    (4, 'Desenvolvedor Fullstack', 'Java no back, Angular no front', 'Remoto');


INSERT INTO candidatos_competencias (candidato_id, competencia_id) VALUES
    (1, 2), (1, 5), -- Sandubinha sabe Java (2) e Spring (5)
    (2, 4),                                  -- Maria sabe Angular (4)
    (3, 1),                                  -- João sabe Python (1)
    (4, 2), (4, 4), -- Ana sabe Java (2) e Angular (4)
    (5, 3);                                  -- Pedro sabe Groovy (3)


INSERT INTO vagas_competencias (vaga_id, competencia_id) VALUES
    (1, 2), (1, 5), -- Vaga Backend (Pastelsoft) precisa de Java e Spring
    (2, 4),                              -- Vaga Frontend (Pastelsoft) precisa de Angular
    (3, 1),                              -- Vaga Analista precisa de Python
    (4, 2), (4, 3), -- Vaga Engenheiro precisa de Java e Groovy
    (5, 2), (5, 4); -- Vaga Fullstack precisa de Java e Angular


-- 4. LÓGICA DE MATCH
CREATE TABLE matches (
    id SERIAL PRIMARY KEY,
    candidato_id INT REFERENCES candidatos(id) ON DELETE CASCADE,
    empresa_id INT REFERENCES empresas(id) ON DELETE CASCADE,
    vaga_id INT REFERENCES vagas(id) ON DELETE CASCADE,
    data_match TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (candidato_id, vaga_id)
);


INSERT INTO empresas (nome, cnpj, email_corporativo, descricao, pais, cep, senha) VALUES
    ('TrollSoft', '99.999.999/0001-99', 'tech@trollsoft.com', 'Inovação e sistemas', 'Brasil', '99999-999', 'senha12345');


INSERT INTO vagas (empresa_id, nome, descricao, local) VALUES
    (6, 'Engenheiro de Software Sênior', 'Vaga que exige muitas competências', 'Remoto');


INSERT INTO curtidas_candidatos (candidato_id, vaga_id) VALUES
    (1, 6); -- O Sandubinha curtiu a vaga de Backend da TrollSoft


INSERT INTO curtidas_empresas (empresa_id, candidato_id) VALUES
    (6, 1); -- A TrollSoft curtiu o Sandubinha (MATCH)


INSERT INTO matches (candidato_id, empresa_id, vaga_id) VALUES
    (1, 6, 6);