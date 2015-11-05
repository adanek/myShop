package data.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Item")
public class Item {

	@Id
	@GeneratedValue
	private int id;
	private String title;
	private String description;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date();
	@Temporal(TemporalType.TIMESTAMP)
	private Date changeDate = new Date();
	@ManyToOne
	private SavedUser author;
	
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

}
