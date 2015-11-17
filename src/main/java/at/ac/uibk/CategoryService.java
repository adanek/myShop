package at.ac.uibk;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import core.Category;
import core.UserCredentials;
import data.handler.DataHandler;
import data.handler.IDataHandler;
import data.model.SavedUser;

@Path("/categories")
public class CategoryService {

	private IDataHandler handler;

	public CategoryService() {
		// handler = new DataHandler();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Category> getCategories() {

		// List<data.model.Category> categories = new
		// ArrayList<data.model.Category>();
		//
		// data.model.Category cat1 = new data.model.Category();
		// cat1.setId(1);
		// cat1.setName("Sport");
		// categories.add(cat1);
		//
		// data.model.Category cat2 = new data.model.Category();
		// cat2.setId(2);
		// cat2.setName("IT");
		// categories.add(cat2);

		handler = new DataHandler();

		Collection<data.model.Category> categories = handler.getAllCategories();

		// close db connection
		handler.closeDatabaseConnection();

		return mapCategoryData(categories);

	}

	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public core.Category createCategory(String catString, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		handler = new DataHandler();

		// check user rights
		AuthenticationService.checkAuthority(request, response, Rights.CAN_CREATE_CATEGORY, handler);

		ObjectMapper om = new ObjectMapper();
		Category cat = null;

		try {
			cat = om.readValue(catString, Category.class);

			// mapping error
			if (cat == null) {
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
		if (cat == null) {
			HTTPStatusService.sendError(response.SC_INTERNAL_SERVER_ERROR, response);
		} else {

			data.model.Category category = handler.createCategory(cat.name);

			// close db connection
			handler.closeDatabaseConnection();

			// data.model.Category category = new data.model.Category();
			// category.setId(cat.id);
			// category.setName(cat.name);

			if (category == null) {
				HTTPStatusService.sendError(response.SC_INTERNAL_SERVER_ERROR, response);
			}

			response.setHeader("Location", "api/categories/" + cat.id);
			response.setStatus(response.SC_CREATED);

			return mapSingleCategory(category);
		}

		return null;

	}

	@PUT
	@Path("/{category}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Category changeCategory(@PathParam("category") int category, String catString,
			@Context HttpServletRequest request, @Context HttpServletResponse response) {

		handler = new DataHandler();

		// check user rights
		AuthenticationService.checkAuthority(request, response, Rights.CAN_EDIT_CATEGORY, handler);

		ObjectMapper om = new ObjectMapper();
		Category cat = null;

		try {
			cat = om.readValue(catString, Category.class);

			// mapping error
			if (cat == null) {
				HTTPStatusService.sendError(response.SC_BAD_REQUEST, response);
			}

			// Parameter m�ssen �bereinstimmen
			if (cat.id != category) {
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
		if (cat == null) {
			HTTPStatusService.sendError(response.SC_INTERNAL_SERVER_ERROR, response);
		}

		// dummy
		// return cat;

		data.model.Category categoryDB = handler.changeCategory(category, cat.name);

		// close db connection
		handler.closeDatabaseConnection();

		return mapSingleCategory(categoryDB);

	}

	@DELETE
	@Path("/{category}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteCategory(@PathParam("category") int category, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		handler = new DataHandler();

		// check user rights
		AuthenticationService.checkAuthority(request, response, Rights.CAN_DELETE_CATEGORY, handler);

		// delete category
		handler.deleteCategory(category);

		// close db connection
		handler.closeDatabaseConnection();

		response.setStatus(response.SC_NO_CONTENT);

	}

	// map single category to output data type
	private Category mapSingleCategory(data.model.Category category) {

		if (category == null) {
			return null;
		}

		Category cat = new Category();

		cat.id = category.getId();
		cat.name = category.getName();

		return cat;

	}

	// map data for output
	private Collection<Category> mapCategoryData(Collection<data.model.Category> categories) {

		List<Category> cats = new ArrayList<Category>();

		if (categories != null) {
			Iterator<data.model.Category> it = categories.iterator();

			while (it.hasNext()) {
				cats.add(mapSingleCategory(it.next()));
			}
		}

		// Katzen zur�ckgeben
		return cats;

	}
}
