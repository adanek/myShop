package data.model;

import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "SavedUser")
public class SavedUser {

	@Id
	@GeneratedValue
	private int id;
	private String alias;
	private String password;
	private int role;
	@OneToMany(mappedBy = "author")
	@Cascade({ CascadeType.DELETE })
	private Collection<ItemComment> comments = new LinkedList<>();
	@OneToMany(mappedBy = "author")
	@Cascade({ CascadeType.DELETE })
	private Collection<Item> items = new LinkedList<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public Collection<ItemComment> getComments() {
		return comments;
	}
	public void setComments(Collection<ItemComment> comments) {
		this.comments = comments;
	}
	public Collection<Item> getItems() {
		return items;
	}
	public void setItems(Collection<Item> items) {
		this.items = items;
	}
	
	
}
