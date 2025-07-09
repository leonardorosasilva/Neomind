package br.com.neomind.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "fornecedor")
public class FornecedorModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "name não pode ser vazio")
    @Size(max = 255, message = "name deve ter no máximo 255 caracteres")
    private String name;

    @Column(unique = true, nullable = false)
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email não pode ser vazio")
    private String email;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "CNPJ não pode ser vazio")
    @Size(min = 14, max = 18, message = "CNPJ deve ter entre 14 e 18 caracteres")
    private String cnpj;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Construtores
    public FornecedorModel() {}

    public FornecedorModel(String name, String email, String cnpj, String description) {
        this.name = name;
        this.email = email;
        this.cnpj = cnpj;
        this.description = description;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FornecedorModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}