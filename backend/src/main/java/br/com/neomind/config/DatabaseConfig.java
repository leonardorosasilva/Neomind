// filepath: /home/leonardo/Desktop/Neomind - Processo Seletivo/backend/src/main/java/br/com/neomind/config/DatabaseConfig.java
package br.com.neomind.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseConfig {
    
    private static EntityManagerFactory entityManagerFactory;
    
    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("neostore-pu");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar EntityManagerFactory", e);
        }
    }
    
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    public static void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}