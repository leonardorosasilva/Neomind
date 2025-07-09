package br.com.neomind.service;

import br.com.neomind.model.FornecedorModel;
import br.com.neomind.repository.FornecedorRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FornecedorService {
    
    private final FornecedorRepository fornecedorRepository;
    private final Validator validator;

    public FornecedorService() {
        this.fornecedorRepository = new FornecedorRepository();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    private boolean validarCNPJ(String cnpj) {
        // Remove caracteres especiais
        cnpj = cnpj.replaceAll("[^0-9]", "");
        
        // Verifica se tem 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }
        
        try {
            // Cálculo do primeiro dígito verificador
            int soma = 0;
            int peso = 5;
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = (peso == 2) ? 9 : peso - 1;
            }
            
            int primeiroDigito = soma % 11;
            primeiroDigito = (primeiroDigito < 2) ? 0 : 11 - primeiroDigito;
            
            // Verifica o primeiro dígito
            if (Character.getNumericValue(cnpj.charAt(12)) != primeiroDigito) {
                return false;
            }
            
            // Cálculo do segundo dígito verificador
            soma = 0;
            peso = 6;
            for (int i = 0; i < 13; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = (peso == 2) ? 9 : peso - 1;
            }
            
            int segundoDigito = soma % 11;
            segundoDigito = (segundoDigito < 2) ? 0 : 11 - segundoDigito;
            
            // Verifica o segundo dígito
            return Character.getNumericValue(cnpj.charAt(13)) == segundoDigito;
            
        } catch (Exception e) {
            return false;
        }
    }

    private void validarFornecedor(FornecedorModel fornecedor, boolean isUpdate) {
        // Validação Bean Validation
        Set<ConstraintViolation<FornecedorModel>> violations = validator.validate(fornecedor);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder("Erro de validação: ");
            for (ConstraintViolation<FornecedorModel> violation : violations) {
                errors.append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(errors.toString());
        }

        // Validação de CNPJ
        if (fornecedor.getCnpj() != null && !fornecedor.getCnpj().trim().isEmpty()) {
            if (!validarCNPJ(fornecedor.getCnpj())) {
                throw new IllegalArgumentException("CNPJ '" + fornecedor.getCnpj() + "' é inválido");
            }
        }

        // Validação de duplicatas
        List<FornecedorModel> todosFornecedores = fornecedorRepository.listarTodos();
        
        for (FornecedorModel existente : todosFornecedores) {
            // Se for update e for o mesmo fornecedor, pula
            if (isUpdate && existente.getId().equals(fornecedor.getId())) {
                continue;
            }

            // Verifica email duplicado
            if (existente.getEmail().equalsIgnoreCase(fornecedor.getEmail())) {
                throw new IllegalArgumentException("Email '" + fornecedor.getEmail() + "' já está cadastrado");
            }

            // Verifica CNPJ duplicado
            if (existente.getCnpj().replaceAll("[^0-9]", "")
                    .equals(fornecedor.getCnpj().replaceAll("[^0-9]", ""))) {
                throw new IllegalArgumentException("CNPJ '" + fornecedor.getCnpj() + "' já está cadastrado");
            }
        }
    }

    public List<FornecedorModel> importarFornecedores(List<FornecedorModel> fornecedores) {
        List<FornecedorModel> fornecedoresImportados = new ArrayList<>();
        List<String> erros = new ArrayList<>();
        
        for (int i = 0; i < fornecedores.size(); i++) {
            FornecedorModel fornecedor = fornecedores.get(i);
            try {
                // Validar cada fornecedor antes de criar
                if (fornecedor.getname() == null || fornecedor.getname().trim().isEmpty()) {
                    throw new IllegalArgumentException("Nome do fornecedor é obrigatório");
                }
                
                if (fornecedor.getEmail() == null || fornecedor.getEmail().trim().isEmpty()) {
                    throw new IllegalArgumentException("Email do fornecedor é obrigatório");
                }
                
                if (fornecedor.getCnpj() == null || fornecedor.getCnpj().trim().isEmpty()) {
                    throw new IllegalArgumentException("CNPJ do fornecedor é obrigatório");
                }
                
                FornecedorModel novoFornecedor = criarFornecedor(fornecedor);
                fornecedoresImportados.add(novoFornecedor);
                
            } catch (Exception e) {
                String erro = "Erro no fornecedor " + (i + 1) + " (" + 
                             (fornecedor.getname() != null ? fornecedor.getname() : "Nome não informado") + 
                             "): " + e.getMessage();
                System.err.println("❌ " + erro);
                erros.add(erro);
            }
        }
        
        // Se houve erros, lança exceção com detalhes
        if (!erros.isEmpty()) {
            String mensagemErro = "Erros encontrados na importação:\n" + String.join("\n", erros);
            if (fornecedoresImportados.isEmpty()) {
                throw new IllegalArgumentException(mensagemErro);
            } else {
                System.err.println("⚠️ Importação parcial: " + fornecedoresImportados.size() + 
                                 " de " + fornecedores.size() + " fornecedores importados\n" + mensagemErro);
            }
        }
        
        return fornecedoresImportados;
    }

    

    public List<FornecedorModel> listarTodos() {
        return fornecedorRepository.listarTodos();
    }

    public Optional<FornecedorModel> buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }
        return fornecedorRepository.buscarPorId(id);
    }

    public FornecedorModel criarFornecedor(FornecedorModel fornecedor) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não pode ser nulo");
        }

        // Limpar CNPJ (remover formatação)
        if (fornecedor.getCnpj() != null) {
            fornecedor.setCnpj(fornecedor.getCnpj().replaceAll("[^0-9]", ""));
        }

        validarFornecedor(fornecedor, false);
        return fornecedorRepository.salvar(fornecedor);
    }

    public FornecedorModel atualizarFornecedor(Long id, FornecedorModel fornecedor) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }
        
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não pode ser nulo");
        }

        // Verificar se existe
        Optional<FornecedorModel> existente = fornecedorRepository.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Fornecedor com ID " + id + " não encontrado");
        }

        // Definir o ID no fornecedor para validação
        fornecedor.setId(id);

        // Limpar CNPJ
        if (fornecedor.getCnpj() != null) {
            fornecedor.setCnpj(fornecedor.getCnpj().replaceAll("[^0-9]", ""));
        }

        validarFornecedor(fornecedor, true);
        return fornecedorRepository.atualizar(fornecedor);
    }

    public void deletarFornecedor(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<FornecedorModel> existente = fornecedorRepository.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Fornecedor com ID " + id + " não encontrado");
        }

        fornecedorRepository.deletar(id);
    }
    
    public void verificarBanco() {
        System.out.println("🔍 === VERIFICAÇÃO DO BANCO ===");
        fornecedorRepository.verificarConexaoBanco();
        System.out.println("🔍 === FIM VERIFICAÇÃO ===");
    }
}