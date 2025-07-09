package br.com.neomind.service;

import br.com.neomind.model.FornecedorModel;
import br.com.neomind.repository.FornecedorRepository;
import br.com.neomind.util.DataInitializer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FornecedorService {
    
    private final FornecedorRepository fornecedorRepository;
    private final Validator validator;
    private static boolean dadosInicializados = false;

    public FornecedorService() {
        this.fornecedorRepository = new FornecedorRepository();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        
        // Inicializar dados de teste na primeira instância
        inicializarDadosSeNecessario();
    }
    
    private synchronized void inicializarDadosSeNecessario() {
        if (!dadosInicializados) {
            try {
                DataInitializer.inicializarDados();
                dadosInicializados = true;
            } catch (Exception e) {
                System.err.println("Erro ao inicializar dados: " + e.getMessage());
            }
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
}