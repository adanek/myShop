package team1.myshop.web;

import org.apache.logging.log4j.LogManager;
import team1.myshop.contracts.UserRights;
import team1.myshop.web.model.Category;
import team1.myshop.web.helper.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collection;


import static javax.servlet.http.HttpServletResponse.*;

@Path("/categories")
public class CategoryService extends ServiceBase {

    public CategoryService() {
        super();
    }

    @Override
    public void initializeLogger() {
        this.setLogger(LogManager.getLogger(CategoryService.class));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Category> getCategories(@Context HttpServletResponse response) {

        this.initialize();
        Collection<data.model.Category> categories = dh.getAllCategories();

        if (categories == null) {
            http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
            return null;
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
        if(!auth.userHasRight(request, UserRights.CAN_CREATE_CATEGORY)){
            logger.info("User wants to create a category, but did not have the right to");
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return null;
        }


        Category cat = JsonParser.parse(catString, Category.class);

        // parse body data
        if (cat == null) {
            http.cancelRequest(response, SC_BAD_REQUEST);
            return null;
        }

        // Save the new category
        assert cat != null;
        data.model.Category category = dh.createCategory(cat.name);

        if (category == null) {
            http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
            return null;
        }

        // Set response headers
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
        if(!auth.userHasRight(request, UserRights.CAN_EDIT_CATEGORY)){
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return null;
        }

        Category cat = JsonParser.parse(catString, Category.class);

        // Validate data
        if (cat == null || cat.id != category) {
            http.cancelRequest(response, SC_BAD_REQUEST);
            return null;
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

        logger.debug("User tries to delete category "+category );

        // check user rights
        if (!auth.userHasRight(request, UserRights.CAN_DELETE_CATEGORY)) {
            logger.info("User tried to delete category " + category + " but did not have the right" + category);
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return;
        }

        // delete category
        logger.debug("User has the right to delete category " + category);
        dh.deleteCategory(category);
        logger.debug("User has deleted category " + category);

        response.setStatus(SC_NO_CONTENT);
    }
}
