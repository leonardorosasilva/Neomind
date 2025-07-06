package br.com.neomind.service;

import br.com.neomind.model.FornecedorModel;
import br.com.neomind.model.FornecedorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.ws.rs.BadRequestException;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import br.com.neomind.util.CnpjValidator;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class FornecedorService {
    @Inject
    private FornecedorRepository fornecedorRepository;
    @Inject
    private Validator validator;

    private void validateFornecedor(FornecedorModel fornecedor, Boolean isUpdate) {
        Set<ConstraintViolation<FornecedorModel>> violations = validator.validate(fornecedor);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder("Erro ao validar fornecedor: ");
            for (ConstraintViolation<FornecedorModel> violation : violations) {
                errors.append(violation.getMessage());
            }

            throw new BadRequestException(errors.toString());
        }

        if(!CnpjValidator.isValid(fornecedor.getCnpj())){
            throw new BadRequestException("CNPJ invalido");
        }

        fornecedorRepository.findAll().forEach(e -> {
            if(isUpdate && e.getId().equals(fornecedor.getId())){
                return;
            }

            if(e.getEmail().equals(fornecedor.getEmail())){
                throw new BadRequestException("Email " + fornecedor.getEmail() + "ja cadastrado" );
            }

            if(e.getCnpj().equals(fornecedor.getCnpj())){
                throw new BadRequestException("CNPJ " + e.getCnpj() + "já cadastrado");
            }

        });

    }


    @Transactional
    public FornecedorModel create(FornecedorModel fornecedorModel) {
        validateFornecedor(fornecedorModel, true);
        return fornecedorRepository.save(fornecedorModel);
    }

    @Transactional
    public FornecedorRepository update(FornecedorModel fornecedorModel) {
        if(fornecedorRepository.findById(fornecedorModel.getId()).isPresent()){
            validateFornecedor(fornecedorModel, false);
            fornecedorRepository.save(fornecedorModel);
            return fornecedorRepository;
        } else{
            throw new BadRequestException("Fornecedor não encontrado");
        }
    }

    @Transactional
    public FornecedorRepository delete(FornecedorModel fornecedorModel) {
        if(fornecedorRepository.findById(fornecedorModel.getId()).isPresent()){
            fornecedorRepository.delete(fornecedorModel);
            return fornecedorRepository;
        } else{
            throw new BadRequestException("Erro ao excluir o fornecedor");
        }
    }

    public List<FornecedorModel> listFornecedoresPaginated(int page, int pageSize) {
        if (page < 1) {
            throw new BadRequestException("O número da página deve ser maior ou igual a 1.");
        }
        if (pageSize < 1) {
            throw new BadRequestException("O tamanho da página deve ser maior ou igual a 1.");
        }
        return fornecedorRepository.findAllPaginated(page, pageSize);
    }

    public long countFornecedores() {
        return fornecedorRepository.countAll();
    }

    public FornecedorModel getFornecedorById(Long id) {
        return fornecedorRepository.findById(id).orElse(null);
    }

    @Transactional
    public int importFornecedoresFromJson(List<FornecedorModel> fornecedoresToImport) {
        int importedCount = 0;
        for (FornecedorModel fornecedor : fornecedoresToImport) {
            try {
                validateFornecedor(fornecedor, false);
                fornecedorRepository.save(fornecedor);
                importedCount++;
            } catch (BadRequestException e) {
                System.err.println("Erro ao importar fornecedor " + fornecedor.getName() + ": " + e.getMessage());
            }
        }
        return importedCount;
    }
}
