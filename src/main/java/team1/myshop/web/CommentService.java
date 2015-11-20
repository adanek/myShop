package team1.myshop.web;

import team1.myshop.contracts.UserRights;
import team1.myshop.web.model.Comment;
import data.model.ItemComment;
import team1.myshop.web.helper.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;

@Path("/comments")
public class CommentService extends ServiceBase {

    @GET
    @Path("/{commentID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Comment getComment(@PathParam("commentID") int commentID, @Context HttpServletRequest req,
                              @Context HttpServletResponse response) {

        this.initialize();

        ItemComment comment = dh.getItemCommentByID(commentID);

        if (comment == null) {
            http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
        }

        return Comment.parse(comment);
    }

    @GET
    @Path("/item/{itemID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Comment> getCommentsFromItem(@PathParam("itemID") int itemID, @Context HttpServletRequest req,
                                                   @Context HttpServletResponse response) {

        this.initialize();

        Collection<ItemComment> comments = dh.getCommentsFromItem(itemID);

        return Comment.parse(comments);
    }

    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Comment createComment(String commentString, @Context HttpServletRequest request,
                                 @Context HttpServletResponse response) {

        this.initialize();

        // check user rights
        auth.ensureUserRight(request, response, UserRights.CAN_CREATE_COMMENT);

        // Parse body data
        Comment com = JsonParser.parse(commentString, Comment.class);
        if (com == null) {
            http.cancelRequest(response, SC_BAD_REQUEST);
        }

        assert com != null;
        ItemComment comment = dh.createItemComment(com.content, com.itemID, com.authorID);

        if (comment == null) {
            http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
        }

        assert comment != null;
        response.setHeader("Location", "api/comments/" + comment.getId());
        response.setStatus(201);

        return Comment.parse(comment);
    }

    @PUT
    @Path("/{commentID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Comment changeComment(@PathParam("commentID") int commentID, String commentString,
                                 @Context HttpServletRequest request, @Context HttpServletResponse response) {

        this.initialize();

        // check user rights
        auth.ensureUserRight(request, response, UserRights.CAN_EDIT_COMMENT);

        Comment com = JsonParser.parse(commentString, Comment.class);
        if (com == null) {
            http.cancelRequest(response, SC_BAD_REQUEST);
        }

        // Parameter müssen übereinstimmen
        assert com != null;
        if (com.commentId != commentID) {
            http.cancelRequest(response, SC_BAD_REQUEST);
        }

        data.model.ItemComment comment = dh.changeComment(com.commentId, com.content);

        if(comment == null){
            http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
        }

        return Comment.parse(comment);
    }

    @DELETE
    @Path("/{commentID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteComment(@PathParam("commentID") int commentID, @Context HttpServletRequest request,
                              @Context HttpServletResponse response) {

        this.initialize();

        // check user rights
        auth.ensureUserRight(request, response, UserRights.CAN_DELETE_COMMENT);

        // delete comment
        dh.deleteComment(commentID);

        response.setStatus(SC_NO_CONTENT);
    }
}
