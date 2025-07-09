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
            List<FornecedorModel> resultado = query.getResultList();
            
            return resultado;
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
                
                em.persist(fornecedor);
            } else {
                
                fornecedor = em.merge(fornecedor);
            }
            
            // FORÇAR FLUSH antes do commit
            em.flush();
            
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
            
            // FORÇAR FLUSH antes do commit
            em.flush();
            
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
            
            // FORÇAR FLUSH antes do commit
            em.flush();
            
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

    // Método para verificar se está realmente salvando no banco
    public void verificarConexaoBanco() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Contar quantos registros existem
            Long count = em.createQuery("SELECT COUNT(f) FROM FornecedorModel f", Long.class)
                          .getSingleResult();
            
            
            // Listar todos para debug
            List<FornecedorModel> todos = em.createQuery("SELECT f FROM FornecedorModel f", FornecedorModel.class)
                                            .getResultList();
            
            for (FornecedorModel f : todos) {
                
            }
            
        } catch (Exception e) {
            
        } finally {
            em.close();
        }
    }
}