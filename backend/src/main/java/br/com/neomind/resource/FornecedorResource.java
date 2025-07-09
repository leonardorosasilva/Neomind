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
            List<FornecedorModel> fornecedores = fornecedorService.listarTodos();
            return Response.ok(fornecedores)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarFornecedorPorId(@PathParam("id") Long id) {
        try {
            return fornecedorService.buscarPorId(id)
                    .map(fornecedor -> Response.ok(fornecedor)
                            .header("Access-Control-Allow-Origin", "*")
                            .build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"error\":\"Fornecedor não encontrado\"}")
                            .build());
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    public Response criarFornecedor(@Valid FornecedorModel fornecedor) {
        try {
            FornecedorModel novoFornecedor = fornecedorService.criarFornecedor(fornecedor);
            
            URI uri = UriBuilder.fromResource(FornecedorResource.class)
                    .path(String.valueOf(novoFornecedor.getId()))
                    .build();

            return Response.created(uri)
                    .entity(novoFornecedor)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
                    
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarFornecedor(@PathParam("id") Long id, @Valid FornecedorModel fornecedor) {
        try {
            FornecedorModel fornecedorAtualizado = fornecedorService.atualizarFornecedor(id, fornecedor);
            return Response.ok(fornecedorAtualizado)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
                    
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarFornecedor(@PathParam("id") Long id) {
        try {
            fornecedorService.deletarFornecedor(id);
            return Response.ok()
                    .entity("{\"message\":\"Fornecedor deletado com sucesso\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
                    
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @OPTIONS
    @Path("/{path:.*}")
    public Response options() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .build();
    }
}