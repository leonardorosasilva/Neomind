package br.com.neomind.repository;

import br.com.neomind.model.FornecedorModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import br.com.neomind.util.JPAUtil;

import java.util.List;
import java.util.Optional;

public class FornecedorRepository {

    public List<FornecedorModel> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<FornecedorModel> query = em.createQuery(
                "SELECT f FROM FornecedorModel f ORDER BY f.name ASC", 
                FornecedorModel.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<FornecedorModel> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            FornecedorModel fornecedor = em.find(FornecedorModel.class, id);
            return Optional.ofNullable(fornecedor);
        } finally {
            em.close();
        }
    }

    public FornecedorModel salvar(FornecedorModel fornecedor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            
            if (fornecedor.getId() == null) {
                // Criar novo
                em.persist(fornecedor);
            } else {
                // Atualizar existente
                fornecedor = em.merge(fornecedor);
            }
            
            transaction.commit();
            return fornecedor;
            
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao salvar fornecedor: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public FornecedorModel atualizar(FornecedorModel fornecedor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            
            // Verificar se o fornecedor existe
            FornecedorModel existente = em.find(FornecedorModel.class, fornecedor.getId());
            if (existente == null) {
                throw new IllegalArgumentException("Fornecedor com ID " + fornecedor.getId() + " não encontrado");
            }
            
            // Atualizar campos
            existente.setname(fornecedor.getname());
            existente.setEmail(fornecedor.getEmail());
            existente.setCnpj(fornecedor.getCnpj());
            existente.setdescription(fornecedor.getdescription());
            
            // Merge e commit
            existente = em.merge(existente);
            transaction.commit();
            
            return existente;
            
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao atualizar fornecedor: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void deletar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            
            FornecedorModel fornecedor = em.find(FornecedorModel.class, id);
            if (fornecedor == null) {
                throw new IllegalArgumentException("Fornecedor com ID " + id + " não encontrado");
            }
            
            em.remove(fornecedor);
            transaction.commit();
            
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao deletar fornecedor: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<FornecedorModel> buscarPorname(String name) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<FornecedorModel> query = em.createQuery(
                "SELECT f FROM FornecedorModel f WHERE LOWER(f.name) LIKE LOWER(:name) ORDER BY f.name ASC", 
                FornecedorModel.class
            );
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<FornecedorModel> buscarPorEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<FornecedorModel> query = em.createQuery(
                "SELECT f FROM FornecedorModel f WHERE LOWER(f.email) = LOWER(:email)", 
                FornecedorModel.class
            );
            query.setParameter("email", email);
            List<FornecedorModel> resultados = query.getResultList();
            return resultados.isEmpty() ? Optional.empty() : Optional.of(resultados.get(0));
        } finally {
            em.close();
        }
    }

    public Optional<FornecedorModel> buscarPorCnpj(String cnpj) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Limpar CNPJ para busca (remover formatação)
            String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
            
            TypedQuery<FornecedorModel> query = em.createQuery(
                "SELECT f FROM FornecedorModel f WHERE f.cnpj = :cnpj", 
                FornecedorModel.class
            );
            query.setParameter("cnpj", cnpjLimpo);
            List<FornecedorModel> resultados = query.getResultList();
            return resultados.isEmpty() ? Optional.empty() : Optional.of(resultados.get(0));
        } finally {
            em.close();
        }
    }

    public List<FornecedorModel> listarPaginado(int pagina, int tamanhoPagina) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            int primeiroResultado = (pagina - 1) * tamanhoPagina;
            
            TypedQuery<FornecedorModel> query = em.createQuery(
                "SELECT f FROM FornecedorModel f ORDER BY f.name ASC", 
                FornecedorModel.class
            );
            query.setFirstResult(primeiroResultado);
            query.setMaxResults(tamanhoPagina);
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long contarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(f) FROM FornecedorModel f", 
                Long.class
            );
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}