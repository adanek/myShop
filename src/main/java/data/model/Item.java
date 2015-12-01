package data.model;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "Item")
public class Item {

	@Id
	@GeneratedValue
	private int id;
	private String title;
	private String description;
	private double price;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date();
	@Temporal(TemporalType.TIMESTAMP)
	private Date changeDate = new Date();
	@ManyToOne
	private Category category;
	@ManyToOne
	private SavedUser author;
	@OneToMany(mappedBy = "item")
	@Cascade({ CascadeType.DELETE })
	private Collection<ItemComment> comments = new LinkedList<>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public SavedUser getAuthor() {
		return author;
	}

	public void setAuthor(SavedUser author) {
		this.author = author;
	}

	public Collection<ItemComment> getComments() {
		return comments;
	}

	public void setComments(Collection<ItemComment> comments) {
		this.comments = comments;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
