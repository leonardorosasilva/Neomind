package br.com.neomind.repository;

import br.com.neomind.model.FornecedorModel;

import java.util.List;
import java.util.Optional;

public interface JpaFornecedorRepository {
    FornecedorModel save(FornecedorModel fornecedor);
    FornecedorModel update(FornecedorModel fornecedor);
    FornecedorModel delete(FornecedorModel fornecedor);

    Optional<FornecedorModel> findById(long id);
    List<FornecedorModel> findByNameContaining(String name);
    List<FornecedorModel> findAll();
    List<FornecedorModel> findAllPaginated(int page, int size);
    long countAll();
}
