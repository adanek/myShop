package at.ac.uibk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.media.sound.ModelAbstractChannelMixer;

import core.Category;
import core.Item;
import data.handler.DataHandler;
import data.model.SavedUser;

@Path("/items")
public class ItemService {

	private DataHandler handler;

	public ItemService() {
		// handler = new DataHandler();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Item> getItems() {

		data.model.Category cat = new data.model.Category();
		cat.setId(1);
		cat.setName("Sport");

		SavedUser author = new SavedUser();
		author.setId(3);
		author.setAlias("Pati");

		List<data.model.Item> items = new ArrayList<data.model.Item>();

		data.model.Item item1 = new data.model.Item();
		item1.setId(1);
		item1.setCategory(cat);
		item1.setCreationDate(new Date());
		item1.setChangeDate(new Date());
		item1.setAuthor(author);
		item1.setDescription("Sportschuh");
		item1.setTitle("Adidas Boost");
		items.add(item1);

		data.model.Item item2 = new data.model.Item();
		item2.setId(1);
		item2.setCategory(cat);
		item2.setCreationDate(new Date());
		item2.setChangeDate(new Date());
		item2.setAuthor(author);
		item2.setDescription("Laptop");
		item2.setTitle("Lenovo Thinkpad");
		items.add(item2);

		return mapCategoryData(items);
		// return mapCategoryData(handler.getAllItems());

	}

	@GET
	@Path("/category/{category}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Item> getItemsFromCategory(@PathParam("category") int category) {

		data.model.Category cat = new data.model.Category();
		cat.setId(category);
		if (category == 3) {
			cat.setName("Sport");
		} else {
			cat.setName("IT");
		}

		SavedUser author = new SavedUser();
		author.setId(3);
		author.setAlias("Pati");

		List<data.model.Item> items = new ArrayList<data.model.Item>();

		data.model.Item item1 = new data.model.Item();
		item1.setId(1);
		item1.setCategory(cat);
		item1.setCreationDate(new Date());
		item1.setChangeDate(new Date());
		item1.setAuthor(author);
		item1.setDescription("Sportschuh");
		item1.setTitle("Adidas Boost");
		items.add(item1);

		data.model.Item item2 = new data.model.Item();
		item2.setId(1);
		item2.setCategory(cat);
		item2.setCreationDate(new Date());
		item2.setChangeDate(new Date());
		item2.setAuthor(author);
		item2.setDescription("Laptop");
		item2.setTitle("Lenovo Thinkpad");
		items.add(item2);

		return mapCategoryData(items);
		// return mapCategoryData(handler.getItemsFromCategory(category));

	}

	// map data for output
	private Collection<Item> mapCategoryData(Collection<data.model.Item> items) {

		List<Item> its = new ArrayList<Item>();

		Iterator<data.model.Item> iterator = items.iterator();

		while (iterator.hasNext()) {
			data.model.Item item = iterator.next();

			Item it = new Item();

			// mapping
			it.id = item.getId();
			it.title = item.getTitle();
			it.description = item.getDescription();
			it.creationDate = item.getCreationDate().getTime();
			it.changeDate = item.getChangeDate().getTime();
			it.author = item.getAuthor().getAlias();
			it.category = item.getCategory().getName();

			its.add(it);
		}

		// items zurückgeben
		return its;

	}

}
