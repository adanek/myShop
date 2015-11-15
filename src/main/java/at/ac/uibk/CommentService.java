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

import core.Comment;
import core.Item;
import data.handler.DataHandler;
import data.model.ItemComment;

@Path("/comments")
public class CommentService {

    private DataHandler handler;

    public CommentService() {
        // handler = new DataHandler();
    }

    @GET
    @Path("/{commentID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Comment getComment(@PathParam("commentID") int commentID, @Context HttpServletRequest req,
                              @Context HttpServletResponse response) {

        Comment com = new Comment();

        com.commentId = 7;
        com.author = "Pati";
        com.authorID = 3;
        com.content = "Hallo, das ist mein erster Kommentar";
        com.creationDate = new Date().getTime();

        return com;

        //return mapSingleComment(handler.getItemCommentByID(commentID));

    }

    @GET
    @Path("/item/{itemID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Comment> getCommentsFromItem(@PathParam("itemID") int itemID) {

        List<Comment> coms = new ArrayList<Comment>();

        Comment com1 = new Comment();
        com1.commentId = 1;
        com1.itemId = itemID;
        com1.content = "erster kommentar";
        com1.author = "Pati";
        com1.authorID = 3;
        coms.add(com1);

        Comment com2 = new Comment();
        com2.commentId = 2;
        com2.itemId = itemID;
        com2.content = "zweiter kommentar";
        com2.author = "Andi";
        com2.authorID = 4;
        coms.add(com2);

        return coms;

        //return mapCommentData(handler.getCommentsFromItem(itemID));

    }

    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void createComment(String commentString, @Context HttpServletRequest req,
                              @Context HttpServletResponse response) {

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
            HTTPStatusService.sendError(response.SC_INTERNAL_SERVER_ERROR, response);
        } else {

            //data.model.ItemComment comment = handler.createItemComment(com.content, com.itemID, com.authorID);
            data.model.ItemComment comment = new data.model.ItemComment();

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
                                 @Context HttpServletRequest req, @Context HttpServletResponse response) {

        ObjectMapper om = new ObjectMapper();
        Comment com = null;

        try {
            com = om.readValue(commentString, Comment.class);

            // Parameter m�ssen �bereinstimmen
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
        return com;

        //return mapSingleComment(handler.changeComment(com.id, com.content));

    }

    @DELETE
    @Path("/{commentID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteComment(@PathParam("commentID") int commentID, @Context HttpServletRequest req,
                              @Context HttpServletResponse response) {

        // delete comment
        //handler.deleteComment(commentID);

        response.setStatus(response.SC_NO_CONTENT);

    }

    //map single comment to output data type
    private Comment mapSingleComment(ItemComment comment) {

        Comment com = new Comment();

        // mapping
        com.commentId = comment.getId();
        com.itemId = comment.getItem().getId();
        com.authorID = comment.getAuthor().getId();

        com.content = comment.getComment();
        com.author = comment.getAuthor().getAlias();
        com.creationDate= new Date().getTime();

        return com;

    }

    // map data for output
    private Collection<Comment> mapCommentData(Collection<data.model.ItemComment> comments) {

        List<Comment> coms = new ArrayList<Comment>();

        Iterator<data.model.ItemComment> iterator = comments.iterator();

        while (iterator.hasNext()) {
            coms.add(mapSingleComment(iterator.next()));
        }

        // comments zur�ckgeben
        return coms;

    }

}
