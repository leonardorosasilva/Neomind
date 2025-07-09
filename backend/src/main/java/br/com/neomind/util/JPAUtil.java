package br.com.neomind.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static EntityManagerFactory emf;
    
    static {
        try {
            System.out.println("🔄 Inicializando EntityManagerFactory...");
            emf = Persistence.createEntityManagerFactory("default");
            System.out.println("✅ EntityManagerFactory inicializado com sucesso!");
            System.out.println("💾 Banco H2 configurado em: ./database/neostore.mv.db");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha na inicialização do JPA", e);
        }
    }

    public static EntityManager getEntityManager() {
        if (emf == null) {
            throw new RuntimeException("EntityManagerFactory não foi inicializado");
        }
        return emf.createEntityManager();
    }
    
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}