package at.ac.uibk;

import static org.mockito.Mockito.*;

import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.*;

public class AuthenticationServiceTest {


    @Test
    public void getUserInfo_userLogedIn_doNotSendUnauthorized() throws IOException {

        int userid = 2;
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userid")).thenReturn(userid);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);

        AuthenticationService.checkGetUserInfo(request, response, userid);

        verify(response, times(0)).sendError(anyInt());
    }

    @Test
    public void getUserInfo_userNotLogedIn_SendUnauthorized() throws IOException {

        int userid = 2;
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(null);

        AuthenticationService.checkGetUserInfo(request, response, userid);

        verify(response).sendError(401);
    }

    @Test
    public void getUserInfo_otherUser_SendUnauthorized() throws IOException {

        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        int userid = 1;
        int otherUserId = 2;

        // Setup Mocks
        when(session.getAttribute("userid")).thenReturn(otherUserId);
        when(request.getSession(false)).thenReturn(session);

        AuthenticationService.checkGetUserInfo(request, response, userid);

        verify(response).sendError(401);
    }
}