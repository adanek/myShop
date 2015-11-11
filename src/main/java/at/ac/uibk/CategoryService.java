package at.ac.uibk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import core.Category;
import data.handler.DataHandler;

@Path("/categories")
public class CategoryService {

	private DataHandler handler;
	
	public CategoryService(){
		//handler = new DataHandler();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Category> getCategories(){
		
		List<data.model.Category> categories = new ArrayList<data.model.Category>(); 
		
		data.model.Category cat1 = new data.model.Category();
		cat1.setId(1);
		cat1.setName("Sport");
		categories.add(cat1);
		
		data.model.Category cat2 = new data.model.Category();
		cat2.setId(2);
		cat2.setName("IT");
		categories.add(cat2);
		
		//return mapCategoryData(handler.getAllCategories());
		return mapCategoryData(categories);
		
	}
	
	//map data for output
	private Collection<Category> mapCategoryData(Collection<data.model.Category> categories){
		
		List<Category> cats = new ArrayList<Category>();
		
		Iterator<data.model.Category> it = categories.iterator();
		
		while(it.hasNext()){
			data.model.Category category = it.next();
			
			Category cat = new Category();
			
			cat.id = category.getId();
			cat.name = category.getName();
			
			cats.add(cat);
		}
		
		//Katzen zurückgeben
		return cats;
		
	}
}
