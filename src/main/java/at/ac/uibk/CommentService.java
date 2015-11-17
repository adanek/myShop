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
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.Comment;
import core.Item;
import data.handler.DataHandler;
import data.handler.IDataHandler;
import data.model.ItemComment;

@Path("/comments")
public class CommentService {

	private IDataHandler handler;

	public CommentService() {
		// handler = new DataHandler();
	}

	@GET
	@Path("/{commentID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Comment getComment(@PathParam("commentID") int commentID, @Context HttpServletRequest req,
			@Context HttpServletResponse response) {

		// Comment com = new Comment();
		//
		// com.commentId = 7;
		// com.author = "Pati";
		// com.authorID = 3;
		// com.content = "Hallo, das ist mein erster Kommentar";
		// com.creationDate = new Date().getTime();
		//
		// return com;

		handler = new DataHandler();

		data.model.ItemComment comment = handler.getItemCommentByID(commentID);

		// close db connection
		handler.closeDatabaseConnection();

		return mapSingleComment(comment);

	}

	@GET
	@Path("/item/{itemID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Comment> getCommentsFromItem(@PathParam("itemID") int itemID, @Context HttpServletRequest req,
			@Context HttpServletResponse response) {

		// List<Comment> coms = new ArrayList<Comment>();
		//
		// if(itemID == 1){
		// Comment com1 = new Comment();
		// com1.commentId = 1;
		// com1.itemId = itemID;
		// com1.content = "Sport Kommentar";
		// com1.author = "Pati";
		// com1.authorID = 3;
		// com1.creationDate = new Date().getTime();
		// coms.add(com1);
		// } else {
		// Comment com2 = new Comment();
		// com2.commentId = 2;
		// com2.itemId = itemID;
		// com2.content = "IT Kommentar";
		// com2.author = "Andi";
		// com2.authorID = 4;
		// com2.creationDate = new Date().getTime();
		// coms.add(com2);
		// }
		//
		// return coms;

		handler = new DataHandler();

		Collection<data.model.ItemComment> comments;

		try {
			comments = handler.getCommentsFromItem(itemID);
		} catch (Exception e) {
			response.setStatus(response.SC_NO_CONTENT);
			
			//leere liste zurückgeben
			return new ArrayList<Comment>();
		} finally{
			// close db connection
			handler.closeDatabaseConnection();
		}

		return mapCommentData(comments);
	}

	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void createComment(String commentString, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		handler = new DataHandler();

		// check user rights
		// AuthenticationService.checkAuthority(request, response,
		// Rights.CAN_CREATE_COMMENT, handler);

		ObjectMapper om = new ObjectMapper();
		Comment com = null;

		try {
			com = om.readValue(commentString, Comment.class);

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
		if (com == null) {
			HTTPStatusService.sendError(response.SC_BAD_REQUEST, response);
		} else {

			if(com.authorID < 1){
				com.authorID = 11; //dummy
			}
			
			data.model.ItemComment comment = handler.createItemComment(com.content, com.itemID, com.authorID);
			// data.model.ItemComment comment = new data.model.ItemComment();

			// close db connection
			handler.closeDatabaseConnection();

			if (comment == null) {
				HTTPStatusService.sendError(response.SC_INTERNAL_SERVER_ERROR, response);
			}

			response.setHeader("Location", "api/comments/" + comment.getId());
			response.setStatus(201);
		}

	}

	@PUT
	@Path("/{commentID}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Comment changeComment(@PathParam("commentID") int commentID, String commentString,
			@Context HttpServletRequest request, @Context HttpServletResponse response) {

		handler = new DataHandler();

		// check user rights
		AuthenticationService.checkAuthority(request, response, Rights.CAN_EDIT_COMMENT, handler);

		ObjectMapper om = new ObjectMapper();
		Comment com = null;

		try {
			com = om.readValue(commentString, Comment.class);

			// mapping error
			if (com == null) {
				HTTPStatusService.sendError(response.SC_BAD_REQUEST, response);
			}

			// Parameter mï¿½ssen ï¿½bereinstimmen
			if (com.commentId != commentID) {
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
		if (com == null) {
			HTTPStatusService.sendError(response.SC_INTERNAL_SERVER_ERROR, response);
		}

		// dummy
		// return com;

		data.model.ItemComment comment = handler.changeComment(com.commentId, com.content);

		// close db connection
		handler.closeDatabaseConnection();

		return mapSingleComment(comment);

	}

	@DELETE
	@Path("/{commentID}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteComment(@PathParam("commentID") int commentID, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		handler = new DataHandler();

		// check user rights
		AuthenticationService.checkAuthority(request, response, Rights.CAN_DELETE_COMMENT, handler);

		// delete comment
		handler.deleteComment(commentID);

		// close db connection
		handler.closeDatabaseConnection();

		response.setStatus(response.SC_NO_CONTENT);

	}

	// map single comment to output data type
	private Comment mapSingleComment(ItemComment comment) {

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

	// map data for output
	private Collection<Comment> mapCommentData(Collection<data.model.ItemComment> comments) {

		List<Comment> coms = new ArrayList<Comment>();

		// no comments
		if (comments != null) {
			Iterator<data.model.ItemComment> iterator = comments.iterator();

			while (iterator.hasNext()) {
				coms.add(mapSingleComment(iterator.next()));
			}
		}

		// comments zurï¿½ckgeben
		return coms;

	}

}
