CREATE DATABASE IF NOT EXISTS mercearia;
USE mercearia;

CREATE TABLE IF NOT EXISTS produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    quantidade_estoque DECIMAL(10,2) NOT NULL,
    tipo VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefone VARCHAR(30) NOT NULL,
    endereco VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    forma_pagamento VARCHAR(30) NOT NULL,

    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE IF NOT EXISTS item_venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venda_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (venda_id) REFERENCES venda(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);
