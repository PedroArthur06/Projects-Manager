CREATE DATABASE IF NOT EXISTS projects_manager;

USE projects_manager;

-- Tabela de Projetos
CREATE TABLE projects (
    id_project INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    status VARCHAR(50),
    budget DECIMAL(15, 2)
);

-- Tabela de Membros
CREATE TABLE members (
    id_member INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    position VARCHAR(100),
    department VARCHAR(100),
    date_joined DATE
);

-- Tabela de Tarefas
CREATE TABLE tasks (
    id_task INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    priority INT,
    status VARCHAR(50),
    id_project INT,
    id_member INT,
    FOREIGN KEY (id_project) REFERENCES projects(id_project) 
        ON DELETE CASCADE
);

-- Tabela Associativa
CREATE TABLE project_members (
    id_project INT,
    id_member INT,
    PRIMARY KEY (id_project, id_member),
    FOREIGN KEY (id_project) REFERENCES projects(id_project) ON DELETE CASCADE, 
    FOREIGN KEY (id_member) REFERENCES members(id_member) ON DELETE CASCADE 
);