package br.com.neomind.config;

import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;

import br.com.neomind.repository.FornecedorRepository;
import br.com.neomind.resource.FornecedorResource;
import br.com.neomind.service.FornecedorService;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {
    @Override
public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = new HashSet<>();
    classes.add(FornecedorResource.class);
    classes.add(FornecedorService.class);
    classes.add(FornecedorRepository.class);
    classes.add(JacksonFeature.class);
    return classes;
}
}