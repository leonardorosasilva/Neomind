package br.com.neomind.config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FlywayConfig {

    @PostConstruct
    public void migrate() {
        // Desabilitar temporariamente - migrations já executadas
        System.out.println("Flyway migration desabilitada para Jetty");
    }
}