# NeoStore - Sistema de Gerenciamento de Fornecedores

Sistema completo para gerenciamento de fornecedores com frontend React/TypeScript e backend Java/JAX-RS.

## 🚀 Funcionalidades

- ✅ Listagem de fornecedores com paginação
- ✅ Cadastro e edição de fornecedores
- ✅ Exclusão de fornecedores
- ✅ Busca CNPJ
- ✅ Importação de fornecedores via JSON
- ✅ Validação de dados
- ✅ Interface responsiva

## 🛠️ Tecnologias Utilizadas

### Backend
- Java 17+
- JAX-RS (Jersey)
- Maven
- Bean Validation
- CORS configurado

### Frontend
- React 18
- TypeScript
- Tailwind CSS
- Vite

## 📋 Pré-requisitos

- **Java 17** ou superior
- **Node.js 18** ou superior
- **npm** ou **yarn**
- **Maven** (para o backend)

## 🔧 Instalação e Execução

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd "Neomind - Processo Seletivo"
```

### 2. Configuração do Backend

```bash
# Navegue para o diretório do backend
cd backend

# Compile o projeto
mvn clean compile

# Execute a aplicação
mvn jetty:run
```

O backend estará rodando em: `http://localhost:8080`

**Endpoints disponíveis:**
- `GET /neostore/api/fornecedores/` - Listar todos os fornecedores
- `POST /neostore/api/fornecedores/` - Criar novo fornecedor
- `PUT /neostore/api/fornecedores/{id}` - Atualizar fornecedor
- `DELETE /neostore/api/fornecedores/{id}` - Deletar fornecedor
- `POST /neostore/api/fornecedores/import` - Importar fornecedores via JSON

### 3. Configuração do Frontend

```bash
# Em um novo terminal, navegue para o diretório do frontend
cd frontend/neostore

# Instale as dependências
npm install

# Execute a aplicação
npm run dev
```

O frontend estará rodando em: `http://localhost:5173`

## 📊 Estrutura do Projeto

```
Neomind - Processo Seletivo/
├── backend/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── br/
│   │               └── com/
│   │                   └── neomind/
│   │                       ├── Main.java
│   │                       ├── model/
│   │                       │   └── FornecedorModel.java
│   │                       ├── service/
│   │                       │   └── FornecedorService.java
│   │                       └── resource/
│   │                           └── FornecedorResource.java
│   └── pom.xml
└── frontend/
    └── neostore/
        ├── src/
        │   ├── components/
        │   │   └── FornecedorForm.tsx
        │   ├── pages/
        │   │   └── listagemFornecedores.tsx
        │   ├── types/
        │   │   └── fornecedor.ts
        │   └── App.tsx
        ├── package.json
        └── vite.config.ts
```

## 📝 Como Usar

### Adicionar Fornecedor

1. Clique no botão "Adicionar Fornecedor"
2. Preencha os campos obrigatórios:
   - Nome
   - CNPJ (formato: XX.XXX.XXX/XXXX-XX)
   - Email
   - Descrição (opcional)
3. Clique em "Salvar"

### Importar Fornecedores via JSON

1. Clique no botão "Importar JSON"
2. Cole o JSON no formato:
```json
[
  {
    "name": "Empresa Exemplo LTDA",
    "cnpj": "12.345.678/0001-90",
    "email": "contato@exemplo.com",
    "description": "Empresa especializada em tecnologia"
  }
]
```
3. Ou selecione um arquivo .json
4. Clique em "Importar"

### Buscar Fornecedores

- Digite no campo de busca para filtrar por:
  - Nome
  - Email
  - CNPJ (com ou sem formatação)
  - Descrição

### Editar/Excluir Fornecedor

- **Editar**: Clique no ícone de lápis ou na linha da tabela
- **Excluir**: Clique no ícone de lixeira e confirme

## 🐛 Resolução de Problemas

### Backend não inicia
- Verifique se o Java 17+ está instalado: `java -version`
- Verifique se a porta 8080 está disponível
- Execute: `mvn clean compile` antes de iniciar

### Frontend não carrega
- Verifique se o Node.js 18+ está instalado: `node -version`
- Execute: `npm install` para instalar dependências
- Verifique se a porta 5173 está disponível

### Erro de CORS
- Certifique-se de que o backend está rodando na porta 8080
- O CORS já está configurado no backend para aceitar requisições do frontend

### Erro ao importar JSON
- Verifique se o JSON está no formato correto
- Todos os campos obrigatórios devem estar presentes
- CNPJ deve estar no formato válido

## 📚 API Documentation

### Modelo de Dados - Fornecedor

```json
{
  "id": 1,
  "name": "Empresa Exemplo LTDA",
  "cnpj": "12.345.678/0001-90",
  "email": "contato@exemplo.com",
  "description": "Empresa especializada em tecnologia"
}
```

### Validações

- **Nome**: Obrigatório, máximo 100 caracteres
- **CNPJ**: Obrigatório, formato válido brasileiro
- **Email**: Obrigatório, formato válido
- **Descrição**: Opcional, máximo 500 caracteres

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto é parte do processo seletivo da Neomind.

---

**Desenvolvido por**: Leonardo  
**Data**: Julho 2025
