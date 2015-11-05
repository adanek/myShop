package data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ItemComment")
public class ItemComment {

	@Id
	@GeneratedValue
	private int id;
	@Lob
	private String comment;
	@ManyToOne
	private Item item;
	@ManyToOne
	private SavedUser author;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public SavedUser getAuthor() {
		return author;
	}
	public void setAuthor(SavedUser author) {
		this.author = author;
	}
	
}
