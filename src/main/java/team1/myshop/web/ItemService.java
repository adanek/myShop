package team1.myshop.web;

import org.apache.logging.log4j.LogManager;
import team1.myshop.contracts.UserRights;
import team1.myshop.web.model.Item;
import team1.myshop.web.helper.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

import static javax.servlet.http.HttpServletResponse.*;

@Path("/items")
public class ItemService extends ServiceBase {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Item> getItems() {

        this.initialize();

        Collection<data.model.Item> items = dh.getAllItems();

        return Item.parse(items);
    }

    @GET
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Item getItemById(@PathParam("itemId") int itemId) {

        this.initialize();

        data.model.Item item = dh.getItemByID(itemId);

        return Item.parse(item);
    }

    @GET
    @Path("/category/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Item> getItemsFromCategory(@PathParam("category") int category) {

        this.initialize();

        Collection<data.model.Item> items = dh.getItemsFromCategory(category);

        return Item.parse(items);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Item createItem(String itemString, @Context HttpServletRequest request,
                           @Context HttpServletResponse response) {

        this.initialize();

        // check user rights
        if (!auth.userHasRight(request, UserRights.CAN_CREATE_ITEM)) {
            logger.info("User tried to create an item, but did not have the right to");
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return null;
        }

        Item item = JsonParser.parse(itemString, Item.class);
        if (item == null) {
            http.cancelRequest(response, SC_BAD_REQUEST);
            return null;
        }

        assert item != null;
        data.model.Item it = dh.createItem(item.title, item.description, item.categoryID, item.authorID);
        if (it == null) {
            http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
            return null;
        }

        assert it != null;
        response.setHeader("Location", "api/items/" + it.getId());
        response.setStatus(SC_CREATED);

        return Item.parse(it);
    }

    @PUT
    @Path("/{item}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Item changeItem(@PathParam("item") int itemID, String itemString, @Context HttpServletRequest request,
                           @Context HttpServletResponse response) {

        this.initialize();

        // check user rights
        if (!auth.userHasRight(request, UserRights.CAN_EDIT_ITEM)) {
            logger.info("User tried to create an item, but did not have the right to");
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return null;
        }

        // parse body data
        Item it = JsonParser.parse(itemString, Item.class);
        if (it == null) {
            http.cancelRequest(response, SC_BAD_REQUEST);
            return null;
        }

        // Parameter müssen übereinstimmen
        assert it != null;
        if (it.id != itemID) {
            http.cancelRequest(response, SC_BAD_REQUEST);
            return null;
        }

        data.model.Item item = dh.changeItem(itemID, it.title, it.description, it.categoryID);
        return Item.parse(item);
    }

    @DELETE
    @Path("/{item}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteItem(@PathParam("item") int item, @Context HttpServletRequest request,
                           @Context HttpServletResponse response) {

        this.initialize();

        // check user rights
        // check user rights
        if (!auth.userHasRight(request, UserRights.CAN_DELETE_ITEM)) {
            logger.info("User tried to delete an item, but did not have the right to");
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return;
        }

        // delete item
        dh.deleteItem(item);

        response.setStatus(SC_NO_CONTENT);
    }

    @Override
    public void initializeLogger() {
        this.setLogger(LogManager.getLogger(UserService.class));
    }
}
