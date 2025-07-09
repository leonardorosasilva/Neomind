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
            System.out.println("🔍 Listando " + resultado.size() + " fornecedor(es) do banco");
            return resultado;
        } finally {
            em.close();
        }
    }

    public Optional<FornecedorModel> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            FornecedorModel fornecedor = em.find(FornecedorModel.class, id);
            System.out.println("🔍 Buscando fornecedor ID " + id + ": " + (fornecedor != null ? "ENCONTRADO" : "NÃO ENCONTRADO"));
            return Optional.ofNullable(fornecedor);
        } finally {
            em.close();
        }
    }

    public FornecedorModel salvar(FornecedorModel fornecedor) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            System.out.println("💾 Iniciando transação para salvar fornecedor: " + fornecedor.getname());
            
            transaction.begin();
            
            if (fornecedor.getId() == null) {
                System.out.println("➕ Persistindo novo fornecedor");
                em.persist(fornecedor);
            } else {
                System.out.println("🔄 Atualizando fornecedor existente");
                fornecedor = em.merge(fornecedor);
            }
            
            // FORÇAR FLUSH antes do commit
            em.flush();
            
            transaction.commit();
            System.out.println("✅ Fornecedor salvo com sucesso! ID: " + fornecedor.getId());
            
            return fornecedor;
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar fornecedor: " + e.getMessage());
            if (transaction.isActive()) {
                transaction.rollback();
                System.err.println("🔄 Rollback executado");
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
            System.out.println("🔄 Iniciando transação para atualizar fornecedor ID: " + fornecedor.getId());
            
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
            
            System.out.println("✅ Fornecedor atualizado com sucesso! ID: " + existente.getId());
            return existente;
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar fornecedor: " + e.getMessage());
            if (transaction.isActive()) {
                transaction.rollback();
                System.err.println("🔄 Rollback executado");
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
            System.out.println("🗑️ Iniciando transação para deletar fornecedor ID: " + id);
            
            transaction.begin();
            
            FornecedorModel fornecedor = em.find(FornecedorModel.class, id);
            if (fornecedor == null) {
                throw new IllegalArgumentException("Fornecedor com ID " + id + " não encontrado");
            }
            
            em.remove(fornecedor);
            
            // FORÇAR FLUSH antes do commit
            em.flush();
            
            transaction.commit();
            
            System.out.println("✅ Fornecedor deletado com sucesso! ID: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao deletar fornecedor: " + e.getMessage());
            if (transaction.isActive()) {
                transaction.rollback();
                System.err.println("🔄 Rollback executado");
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
            System.out.println("🗃️ Total de fornecedores no banco: " + count);
            
            // Listar todos para debug
            List<FornecedorModel> todos = em.createQuery("SELECT f FROM FornecedorModel f", FornecedorModel.class)
                                            .getResultList();
            System.out.println("📋 Fornecedores no banco:");
            for (FornecedorModel f : todos) {
                System.out.println("  - ID: " + f.getId() + " | Nome: " + f.getname() + " | Email: " + f.getEmail());
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao verificar banco: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}