package team1.myshop.web;

import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import team1.myshop.contracts.IDataHandler;
import team1.myshop.contracts.UserRights;
import team1.myshop.web.helper.AuthenticationService;
import team1.myshop.web.helper.HttpService;
import team1.myshop.web.model.Category;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static javax.servlet.http.HttpServletResponse.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;


public class CategoryServiceTest {

    CategoryService sut;
    AuthenticationService auth;
    HttpService http;
    Logger logger;
    IDataHandler dh;
    HttpServletRequest request;
    HttpServletResponse response;

    @BeforeMethod
    public void setup() {
        sut = new CategoryService();
        auth = mock(AuthenticationService.class);
        http = mock(HttpService.class);
        logger = mock(Logger.class);
        dh = mock(IDataHandler.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        sut.setAuthenticationService(auth);
        sut.setHttpService(http);
        sut.setLogger(logger);
        sut.setDataHandler(dh);
    }


    @Test
    public void getCategories_DatabaseReturnsNull_sendsInternalServerError() throws IOException {

        IDataHandler dh = mock(IDataHandler.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        CategoryService sut = new CategoryService();
        sut.setDataHandler(dh);

        when(dh.getAllCategories()).thenReturn(null);

        sut.getCategories(response);

        verify(response, times(1)).sendError(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void getCategories_receiveCategory_returnCategory() {
        IDataHandler dh = mock(IDataHandler.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        CategoryService sut = new CategoryService();
        sut.setDataHandler(dh);

        data.model.Category category = new data.model.Category();
        category.setId(1);
        category.setName("TestCategory");
        final Collection<data.model.Category> categories = new ArrayList<>();
        categories.add(category);
        when(dh.getAllCategories()).thenReturn(categories);

        Collection<Category> expected = Category.parse(categories);
        Collection<Category> actual = sut.getCategories(response);

        assertEquals(actual, expected);
    }

    @Test
    public void createCategory_notAuthenticated_ReturnsNull() throws Exception {

        String catString = "{name: 'New Category'}";

        when(auth.userHasRight(request, UserRights.CAN_CREATE_CATEGORY)).thenReturn(false);

        Category expected = null;
        Category actual = sut.createCategory(catString, request, response);

        assertEquals(actual, expected);
        verify(http, times(1)).cancelRequest(response, SC_UNAUTHORIZED);
    }

    @Test
    public void createCategory_Authenticated_ReturnsCategory() throws Exception {

        data.model.Category dhCategory = new data.model.Category();
        String catString = "{\"name\": \"New Category\"}";

        dhCategory.setId(123);
        dhCategory.setName("New Category");

        when(auth.userHasRight(request, UserRights.CAN_CREATE_CATEGORY)).thenReturn(true);
        when(dh.createCategory("New Category")).thenReturn(dhCategory);

        Category expected = new Category();
        expected.name = "New Category";
        expected.id = 123;

        Category actual = sut.createCategory(catString, request, response);

        assertEquals(actual, expected);
    }

    @Test
    public void createCategory_invalidInputString_ReturnsNull() throws Exception {

        String catString = "Not a valid json string";

        when(auth.userHasRight(request, UserRights.CAN_CREATE_CATEGORY)).thenReturn(true);

        Category expected = null;
        Category actual = sut.createCategory(catString, request, response);

        assertEquals(actual, expected);
        verify(http, times(1)).cancelRequest(response, SC_BAD_REQUEST);
    }

    @Test
    public void changeCategory_Authenticated_ReturnCategory() throws Exception {

        data.model.Category dhCategory = new data.model.Category();
        String catString = "{\"id\":\"123\", \"name\": \"Changed Category\"}";
        int categoryId = 123;

        dhCategory.setId(categoryId);
        dhCategory.setName("Changed Category");

        when(auth.userHasRight(request, UserRights.CAN_EDIT_CATEGORY)).thenReturn(true);
        when(dh.changeCategory(categoryId, "Changed Category")).thenReturn(dhCategory);

        Category expected = new Category();
        expected.name = "Changed Category";
        expected.id = categoryId;

        Category actual = sut.changeCategory(categoryId, catString, request, response);

        assertEquals(actual, expected);
    }

    @Test
    public void deleteCategory_NotAuthorized_ReturnsUnauthorized() throws Exception {

        int categoryId = 123;

        when(auth.userHasRight(request, UserRights.CAN_DELETE_CATEGORY)).thenReturn(false);

        sut.deleteCategory(categoryId, request, response);
        verify(http, times(1)).cancelRequest(response, SC_UNAUTHORIZED);
    }

    @Test
    public void deleteCategory_Authorized_ReturnsNoContent() throws Exception {

        int categoryId = 123;

        when(auth.userHasRight(request, UserRights.CAN_DELETE_CATEGORY)).thenReturn(true);

        sut.deleteCategory(categoryId, request, response);
        verify(response, times(1)).setStatus(SC_NO_CONTENT);
    }
}