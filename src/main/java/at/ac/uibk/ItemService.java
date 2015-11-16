package at.ac.uibk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.media.sound.ModelAbstractChannelMixer;

import core.Category;
import core.Item;
import data.handler.DataHandler;
import data.model.SavedUser;

@Path("/items")
public class ItemService {

	private DataHandler handler;

	public ItemService() {
		//handler = new DataHandler();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Item> getItems() {

//		data.model.Category cat1 = new data.model.Category();
//		cat1.setId(1);
//		cat1.setName("Sport");
//
//        data.model.Category cat2 = new data.model.Category();
//        cat2.setId(2);
//        cat2.setName("IT");
//
//		SavedUser author = new SavedUser();
//		author.setId(3);
//		author.setAlias("Pati");
//
//		List<data.model.Item> items = new ArrayList<data.model.Item>();
//
//		data.model.Item item1 = new data.model.Item();
//		item1.setId(1);
//		item1.setCategory(cat1);
//		item1.setCreationDate(new Date());
//		item1.setChangeDate(new Date());
//		item1.setAuthor(author);
//		item1.setDescription("Sportschuh");
//		item1.setTitle("Adidas Boost");
//		items.add(item1);
//
//		data.model.Item item2 = new data.model.Item();
//		item2.setId(2);
//		item2.setCategory(cat2);
//		item2.setCreationDate(new Date());
//		item2.setChangeDate(new Date());
//		item2.setAuthor(author);
//		item2.setDescription("Laptop");
//		item2.setTitle("Lenovo Thinkpad");
//		items.add(item2);
//
//		return mapItemData(items);
		
    	handler = new DataHandler();
		
		Collection<data.model.Item> items = handler.getAllItems();
		
    	//close db connection
    	handler.closeDatabaseConnection();
		
		return mapItemData(items);

	}

	@GET
	@Path("/{itemId}")
	@Produces(MediaType.APPLICATION_JSON)
	public core.Item getItemById(@PathParam("itemId") int itemId){

//		data.model.Category cat = new data.model.Category();
//		cat.setId(1);
//		cat.setName("Sport");
//
//		SavedUser author = new SavedUser();
//		author.setId(3);
//		author.setAlias("Pati");
//
//		data.model.Item item = new data.model.Item();
//		item.setId(itemId);
//		item.setCategory(cat);
//		item.setCreationDate(new Date());
//		item.setChangeDate(new Date());
//		item.setAuthor(author);
//		item.setDescription("Sportschuh");
//		item.setTitle("Adidas Boost");

    	handler = new DataHandler();
		
		data.model.Item item = handler.getItemByID(itemId);
		
    	//close db connection
    	handler.closeDatabaseConnection();
		
		return mapSingleItem(item);
	}

	@GET
	@Path("/category/{category}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Item> getItemsFromCategory(@PathParam("category") int category) {

//		data.model.Category cat = new data.model.Category();
//		cat.setId(category);
//		if (category == 1) {
//			cat.setName("Sport");
//		} else {
//			cat.setName("IT");
//		}
//
//		SavedUser author = new SavedUser();
//		author.setId(3);
//		author.setAlias("Pati");
//
//		List<data.model.Item> items = new ArrayList<data.model.Item>();
//
//		if(category == 1) {
//            data.model.Item item1 = new data.model.Item();
//            item1.setId(1);
//            item1.setCategory(cat);
//            item1.setCreationDate(new Date());
//            item1.setChangeDate(new Date());
//            item1.setAuthor(author);
//            item1.setDescription("Sportschuh");
//            item1.setTitle("Adidas Boost");
//            items.add(item1);
//        } else {
//            data.model.Item item2 = new data.model.Item();
//            item2.setId(2);
//            item2.setCategory(cat);
//            item2.setCreationDate(new Date());
//            item2.setChangeDate(new Date());
//            item2.setAuthor(author);
//            item2.setDescription("Laptop");
//            item2.setTitle("Lenovo Thinkpad");
//            items.add(item2);
//        }
//		return mapItemData(items);
		
    	handler = new DataHandler();
		
		Collection<data.model.Item> items = handler.getItemsFromCategory(category);
		
    	//close db connection
    	handler.closeDatabaseConnection();
		
		return mapItemData(items);

	}

	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void createItem(String itemString, @Context HttpServletRequest request,
			@Context HttpServletResponse response){
		
    	handler = new DataHandler();
		
		 // check user rights
        AuthenticationService.checkAuthority(request, response, Rights.CAN_CREATE_ITEM, handler);
		
		ObjectMapper om = new ObjectMapper();
		Item item = null;

		try {
			item = om.readValue(itemString, Item.class);
			
			//mapping error
			if(item == null){
				HTTPStatusService.sendError(response.SC_BAD_REQUEST, response);
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Daten richtig gemapped...
		if (item == null) {
			HTTPStatusService.sendError(response.SC_INTERNAL_SERVER_ERROR, response);
		} else {

			data.model.Item it = handler.createItem(item.title, item.description, item.categoryID, item.authorID);
			//data.model.Item it = new data.model.Item();
			
	    	//close db connection
	    	handler.closeDatabaseConnection();
			
			if (it == null) {
				HTTPStatusService.sendError(response.SC_INTERNAL_SERVER_ERROR, response);
			}

			response.setHeader("Location", "api/items");
			response.setStatus(201);
		}
		
	}
	
	@PUT
	@Path("/{item}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Item changeItem(@PathParam("item") int itemID, String itemString,
			@Context HttpServletRequest request, @Context HttpServletResponse response) {

    	handler = new DataHandler();
		
		 // check user rights
        AuthenticationService.checkAuthority(request, response, Rights.CAN_EDIT_ITEM, handler);
		
		ObjectMapper om = new ObjectMapper();
		Item it = null;

		try {
			it = om.readValue(itemString, Item.class);

			//mapping error
			if(it == null){
				HTTPStatusService.sendError(response.SC_BAD_REQUEST, response);
			}
			
			// Parameter m�ssen �bereinstimmen
			if (it.id != itemID) {
				HTTPStatusService.sendError(response.SC_BAD_REQUEST, response);
			}

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Daten richtig gemapped...
		if (it == null) {
			HTTPStatusService.sendError(response.SC_INTERNAL_SERVER_ERROR, response);
		}

		// dummy
		//return it;

		data.model.Item item = handler.changeItem(itemID, it.title, it.description, it.categoryID);
		
    	//close db connection
    	handler.closeDatabaseConnection();
		
		return mapSingleItem(item);

	}
	
	@DELETE
	@Path("/{item}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteItem(@PathParam("item") int item, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

    	handler = new DataHandler();
		
		 // check user rights
        AuthenticationService.checkAuthority(request, response, Rights.CAN_DELETE_ITEM, handler);
		
		// delete item
		handler.deleteItem(item);

    	//close db connection
    	handler.closeDatabaseConnection();
		
		response.setStatus(204);

	}
	
	// map single item to output data type
	private Item mapSingleItem(data.model.Item item) {

		Item it = new Item();

		// mapping
		it.id = item.getId();
		it.title = item.getTitle();
		it.description = item.getDescription();
		it.creationDate = item.getCreationDate().getTime();
		it.changeDate = item.getChangeDate().getTime();
		it.author = item.getAuthor().getAlias();
		it.authorID = item.getAuthor().getId();
		it.category = item.getCategory().getName();
		it.categoryID = item.getCategory().getId();

		return it;

	}
	
	// map data for output
	private Collection<Item> mapItemData(Collection<data.model.Item> items) {

		List<Item> its = new ArrayList<Item>();

		Iterator<data.model.Item> iterator = items.iterator();

		while (iterator.hasNext()) {
			its.add(mapSingleItem(iterator.next()));
		}

		// items zur�ckgeben
		return its;

	}

}
