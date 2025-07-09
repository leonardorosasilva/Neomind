package br.com.neomind.util;

import br.com.neomind.model.FornecedorModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class DataInitializer {
    
    public static void inicializarDados() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            
            // Verificar se já existem dados
            Long count = em.createQuery("SELECT COUNT(f) FROM FornecedorModel f", Long.class)
                          .getSingleResult();
            
            if (count == 0) {
                System.out.println("Criando dados de teste...");
                
                // Criar fornecedores de exemplo
                FornecedorModel fornecedor1 = new FornecedorModel(
                    "Tech Solutions LTDA",
                    "contato@techsolutions.com.br",
                    "12345678000190",
                    "Fornecedor de equipamentos de tecnologia"
                );
                
                FornecedorModel fornecedor2 = new FornecedorModel(
                    "Papelaria Central",
                    "vendas@papelariacentral.com.br",
                    "98765432000180",
                    "Fornecedor de materiais de escritório"
                );
                
                FornecedorModel fornecedor3 = new FornecedorModel(
                    "Móveis & Cia",
                    "comercial@moveisecia.com.br",
                    "11223344000170",
                    "Fornecedor de móveis para escritório"
                );
                
                em.persist(fornecedor1);
                em.persist(fornecedor2);
                em.persist(fornecedor3);
                
                transaction.commit();
                System.out.println("Dados de teste criados com sucesso!");
            } else {
                System.out.println("Dados já existem, não criando novos dados de teste.");
                transaction.rollback();
            }
            
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erro ao criar dados de teste: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}