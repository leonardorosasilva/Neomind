// filepath: /home/leonardo/Desktop/Neomind - Processo Seletivo/backend/src/main/java/br/com/neomind/config/CorsFilter.java
package br.com.neomind.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Intercepta requisições OPTIONS e responde imediatamente
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            requestContext.abortWith(Response.ok()
                    .header("Access-Control-Allow-Origin", "http://localhost:5173")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Headers", 
                            "origin, content-type, accept, authorization, x-requested-with")
                    .header("Access-Control-Allow-Methods", 
                            "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .build());
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        
        // Adiciona headers CORS para todas as outras requisições
        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "http://localhost:5173");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization, x-requested-with");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}