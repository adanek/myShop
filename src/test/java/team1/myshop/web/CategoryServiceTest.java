package team1.myshop.web;

import team1.myshop.web.model.Category;
import org.testng.annotations.Test;
import team1.myshop.contracts.IDataHandler;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;


public class CategoryServiceTest {

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
    public void getCategories_receiveCategory_returnCategory(){
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

}