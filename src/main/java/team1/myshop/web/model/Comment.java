package team1.myshop.web.model;

import data.model.ItemComment;

import java.util.*;

public class Comment {

	public int commentId;
	public int itemID;
	public String content;
	public String author;
	public int authorID;
	public long creationDate;
	public long changeDate;

	public static Comment parse(ItemComment comment) {

		if (comment == null) {
			return null;
		}

		Comment com = new Comment();

		// mapping
		com.commentId = comment.getId();
		com.itemID = comment.getItem().getId();
		com.authorID = comment.getAuthor().getId();

		com.content = comment.getComment();
		com.author = comment.getAuthor().getAlias();
		com.creationDate = comment.getCreationDate().getTime();
		com.changeDate = comment.getChangeDate().getTime();

		return com;
	}

	public static Collection<Comment> parse(Collection<data.model.ItemComment> comments) {

		List<Comment> result = new ArrayList<>();

		if (comments != null) {
            for (ItemComment comment : comments) {
                result.add(Comment.parse(comment));
            }
		}

		return result;
	}
	
}
