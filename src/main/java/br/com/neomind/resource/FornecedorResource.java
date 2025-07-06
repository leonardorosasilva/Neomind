package br.com.neomind.resource;

import br.com.neomind.model.FornecedorModel;
import br.com.neomind.model.FornecedorRepository;
import br.com.neomind.service.FornecedorService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.service.spi.InjectService;

import java.util.ArrayList;
import java.util.List;

@Path("/fornecedores")
@Produces(MediaType.APPLICATION_JSON) // response
@Consumes(MediaType.APPLICATION_JSON) // request
public class FornecedorResource {

    FornecedorService fornecedorService;

    @GET
    public Response getFornecedores(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("5" ) int size) {

        List<FornecedorModel> fornecedores = fornecedorService.listFornecedoresPaginated(page, size);
        long countFornecedores = fornecedorService.countFornecedores();
        return Response.ok(fornecedores).header("X-Total-Count", countFornecedores).build();
    }

    @GET
    @Path("/{id}")
    public Response getFornecedorById(@PathParam("id") Long id) {
        FornecedorModel fornecedorModel = fornecedorService.getFornecedorById(id);
        return Response.ok(fornecedorModel).build();
    }

    @POST
    public Response createFornecedor(FornecedorModel fornecedorModel) {
        FornecedorRepository fornecedorModel1 = fornecedorService.create(fornecedorModel);
        return Response.ok(fornecedorModel1).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateFornecedor(FornecedorModel fornecedorModel, @PathParam("id") Long id) {
        FornecedorModel fornecedorModel1 = fornecedorService.getFornecedorById(id);
        fornecedorService.update(fornecedorModel);
        return Response.ok(fornecedorModel1).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteFornecedor(@PathParam("id") Long id) {
        FornecedorModel fornecedorModel = fornecedorService.getFornecedorById(id);
        fornecedorService.delete(fornecedorModel);
        return Response.ok().build();
    }

    @POST
    @Path("/import")
    public Response importFornecedores(List<FornecedorModel> fornecedores) {
        int imported = fornecedorService.importFornecedoresFromJson(fornecedores);
        return Response.ok(imported + "fornecedor(es) importado(s) com sucesso").build();
    }
}
