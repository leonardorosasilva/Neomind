package br.com.neomind.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "fornecedor")
public class FornecedorModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Nome não pode ser vazio")
    private String name;

    @Column(unique = true, nullable = false)
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email não pode ser vazio")
    private String email;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "CNPJ não pode ser vazio")
    private String cnpj; // vamos formatar com mascara

    private String description;

    public FornecedorModel() {
    }

    public FornecedorModel(String name, String email, String cnpj, String description) {
        this.name = name;
        this.email = email;
        this.cnpj = cnpj;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
