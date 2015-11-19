package at.ac.uibk;

import static org.mockito.Mockito.*;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import static org.testng.Assert.*;

/**
 * Created by adanek on 19.11.2015.
 */
public class AuthenticationServiceTest {

    @Test
    public void testCheckAuthority() throws Exception {
        List mockedList = mock(List.class);

        mockedList.add("one");

        verify(mockedList).add("one");
    }

    @Test
    public void test(){
        HttpServletRequest request;
        HttpServletResponse response;
        int userid = 1;
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userid")).thenReturn(userid);

    }
}