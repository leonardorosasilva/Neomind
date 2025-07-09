-- V1__Create_fornecedor_table.sql
CREATE TABLE fornecedor (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para melhor performance
CREATE INDEX idx_fornecedor_name ON fornecedor(name);
CREATE INDEX idx_fornecedor_cnpj ON fornecedor(cnpj);
CREATE INDEX idx_fornecedor_email ON fornecedor(email);

-- Comentários na tabela
COMMENT ON TABLE fornecedor IS 'Tabela de fornecedores do sistema';
COMMENT ON COLUMN fornecedor.id IS 'Identificador único do fornecedor';
COMMENT ON COLUMN fornecedor.name IS 'Nome da empresa fornecedora';
COMMENT ON COLUMN fornecedor.cnpj IS 'CNPJ da empresa (formato: XX.XXX.XXX/XXXX-XX)';
COMMENT ON COLUMN fornecedor.email IS 'Email de contato do fornecedor';
COMMENT ON COLUMN fornecedor.description IS 'Descrição detalhada do fornecedor';