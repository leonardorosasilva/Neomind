package br.com.neomind.resource;

import br.com.neomind.model.FornecedorModel;
import br.com.neomind.service.FornecedorService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

@Path("/fornecedores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FornecedorResource {
    
    private final FornecedorService fornecedorService;
    
    public FornecedorResource() {
        this.fornecedorService = new FornecedorService();
    }

    @GET
    public Response listarFornecedores() {
        try {
            System.out.println("🌐 GET /fornecedores - Listando fornecedores");
            List<FornecedorModel> fornecedores = fornecedorService.listarTodos();
            System.out.println("📊 Retornando " + fornecedores.size() + " fornecedor(es)");
            
            return Response.ok(fornecedores)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                    .build();
        } catch (Exception e) {
            System.err.println("❌ Erro ao listar fornecedores: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }
    }

    @POST
    public Response criarFornecedor(@Valid FornecedorModel fornecedor) {
        try {
            System.out.println("🌐 POST /fornecedores - Criando fornecedor: " + fornecedor.getname());
            FornecedorModel novoFornecedor = fornecedorService.criarFornecedor(fornecedor);
            
            URI uri = UriBuilder.fromResource(FornecedorResource.class)
                    .path(String.valueOf(novoFornecedor.getId()))
                    .build();

            System.out.println("✅ Fornecedor criado com sucesso! ID: " + novoFornecedor.getId());
            return Response.created(uri)
                    .entity(novoFornecedor)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                    .build();
                    
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar fornecedor: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarFornecedor(@PathParam("id") Long id, @Valid FornecedorModel fornecedor) {
        try {
            System.out.println("🌐 PUT /fornecedores/" + id + " - Atualizando fornecedor");
            FornecedorModel fornecedorAtualizado = fornecedorService.atualizarFornecedor(id, fornecedor);
            return Response.ok(fornecedorAtualizado)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                    .build();
                    
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar fornecedor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarFornecedor(@PathParam("id") Long id) {
        try {
            System.out.println("🌐 DELETE /fornecedores/" + id + " - Deletando fornecedor");
            fornecedorService.deletarFornecedor(id);
            return Response.ok()
                    .entity("{\"message\":\"Fornecedor deletado com sucesso\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                    .build();
                    
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception e) {
            System.err.println("❌ Erro ao deletar fornecedor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }
    }

    @OPTIONS
    @Path("/import")
    public Response options() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With, Accept, Origin")
                .header("Access-Control-Max-Age", "3600")
                .build();
    }

    // importar fornecedores via json
   @POST
    @Path("/import")
    public Response importarFornecedores(List<FornecedorModel> fornecedores) {
        try {
            System.out.println("🌐 POST /fornecedores/import - Importando " + fornecedores.size() + " fornecedor(es)");
            
            if (fornecedores == null || fornecedores.isEmpty()) {
                throw new IllegalArgumentException("Lista de fornecedores está vazia");
            }
            
            List<FornecedorModel> fornecedoresImportados = fornecedorService.importarFornecedores(fornecedores);
            
            System.out.println("✅ " + fornecedoresImportados.size() + " fornecedor(es) importado(s) com sucesso!");
            
            return Response.ok()
                    .entity("{\"message\":\"" + fornecedoresImportados.size() + " fornecedor(es) importado(s) com sucesso\", \"fornecedores\":" + fornecedoresImportados.size() + ", \"total_enviados\":" + fornecedores.size() + "}")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                    .build();
                    
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação na importação: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception e) {
            System.err.println("❌ Erro ao importar fornecedores: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }
    }
}