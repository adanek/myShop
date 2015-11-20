package team1.myshop.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Item {

	public int id;
	public String title;
	public String category;
	public int categoryID;
	public String description;
	public long creationDate;
	public long changeDate;
	public String author;
	public int authorID;

	public static Item parse(data.model.Item item) {

		if (item == null) {
			return null;
		}

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

	public static Collection<Item> parse(Collection<data.model.Item> items) {

		List<Item> its = new ArrayList<>();

		if (items != null) {
            for (data.model.Item item : items) {
                its.add(Item.parse(item));
            }
		}

		return its;
	}
}
