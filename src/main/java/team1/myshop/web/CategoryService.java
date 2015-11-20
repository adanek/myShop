package team1.myshop.web;

import team1.myshop.contracts.UserRights;
import team1.myshop.web.model.Category;
import team1.myshop.web.helper.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@Path("/categories")
public class CategoryService extends ServiceBase {

    public CategoryService() {
        super();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Category> getCategories(@Context HttpServletResponse response) {

        this.initialize();
        Collection<data.model.Category> categories = dh.getAllCategories();

        if (categories == null) {
            http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
        }

        return Category.parse(categories);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Category createCategory(String catString, @Context HttpServletRequest request,
                                   @Context HttpServletResponse response) {

        this.initialize();
        // check user rights
        auth.ensureUserRight(request, response, UserRights.CAN_CREATE_CATEGORY);

        Category cat = JsonParser.parse(catString, Category.class);

        // parse body data
        if (cat == null) {
            http.cancelRequest(response, SC_BAD_REQUEST);
        }

        // Save the new category
        data.model.Category category = dh.createCategory(cat != null ? cat.name : null);

        if (category == null) {
            http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
        }

        // Set response headers
        assert cat != null;
        response.setHeader("Location", "api/categories/" + cat.id);
        response.setStatus(HttpServletResponse.SC_CREATED);

        return Category.parse(category);
    }

    @PUT
    @Path("/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Category changeCategory(@PathParam("category") int category, String catString,
                                   @Context HttpServletRequest request, @Context HttpServletResponse response) {

        this.initialize();

        // check user rights
        auth.ensureUserRight(request, response, UserRights.CAN_EDIT_CATEGORY);

        Category cat = JsonParser.parse(catString, Category.class);

        // Validate data
        if (cat == null || cat.id != category) {
            http.cancelRequest(response, SC_BAD_REQUEST);
        }

        // Save category
        assert cat != null;
        data.model.Category categoryDB = dh.changeCategory(category, cat.name);

        return Category.parse(categoryDB);
    }

    @DELETE
    @Path("/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteCategory(@PathParam("category") int category, @Context HttpServletRequest request,
                               @Context HttpServletResponse response) {
        this.initialize();

        // check user rights
        auth.ensureUserRight(request, response, UserRights.CAN_DELETE_CATEGORY);

        // delete category
        dh.deleteCategory(category);

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
