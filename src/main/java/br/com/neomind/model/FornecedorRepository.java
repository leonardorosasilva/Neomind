package br.com.neomind.model;

import br.com.neomind.repository.JpaFornecedorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FornecedorRepository implements JpaFornecedorRepository {  
    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public FornecedorModel save(FornecedorModel fornecedor) {
        em.persist(fornecedor);
        return null;
    }

    @Override
    @Transactional
    public FornecedorModel update(FornecedorModel fornecedor) {
        return em.merge(fornecedor);
    }

    @Override
    @Transactional
    public FornecedorModel delete(FornecedorModel fornecedor) {
        FornecedorModel fornecedorModel = em.find(FornecedorModel.class, fornecedor.getId());
        if (fornecedorModel != null) {
            em.remove(fornecedorModel);
        } else {
            return null;
        }
        return fornecedorModel;
    }

    @Override
    public Optional<FornecedorModel> findById(long id) {
        return Optional.ofNullable(em.find(FornecedorModel.class, id)) ;
    }

    @Override
    public List<FornecedorModel> findByNameContaining(String name) {
        return null;
    }

    @Override
    public List<FornecedorModel> findAll() {
        return em.createQuery("SELECT f FROM FornecedorModel f", FornecedorModel.class).getResultList() ;
    }

    @Override
    public List<FornecedorModel> findAllPaginated(int page, int size) {
        int firstResult = (page - 1) * size; // page é baseado em 1, não em 0

        return em.createQuery("SELECT f FROM FornecedorModel f ORDER BY f.name ASC", FornecedorModel.class) // Adicione um ORDER BY para consistência
                .setFirstResult(firstResult)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public long countAll() {
        return em.createQuery("SELECT COUNT(f) FROM FornecedorModel f", Long.class).getSingleResult();    }
}
