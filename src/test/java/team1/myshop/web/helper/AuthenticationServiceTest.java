package team1.myshop.web.helper;

import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.testng.annotations.Test;
import team1.myshop.contracts.IAuthenticationService;
import team1.myshop.contracts.IHttpService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {


    @Test
    public void getUserInfo_userLogedIn_doNotSendUnauthorized() throws IOException {

        IAuthenticationService sut = new AuthenticationService();
        HttpServletResponse response = mock(HttpServletResponse.class);
        int userid = 2;

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userid")).thenReturn(userid);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(session);

        sut.checkGetUserInfo(request, response, userid);

        verify(response, times(0)).sendError(anyInt());
    }

    @Test
    public void getUserInfo_userNotLogedIn_SendUnauthorized() throws IOException {

        AuthenticationService sut = new AuthenticationService();
        int userid = 2;
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(null);

        IHttpService http = mock(IHttpService.class);
        sut.setHttpService(http);

        sut.checkGetUserInfo(request, response, userid);

        verify(http, times(1)).cancelRequest(response, SC_UNAUTHORIZED);
    }

    @Test
    public void getUserInfo_otherUser_SendUnauthorized() throws IOException {

        AuthenticationService sut = new AuthenticationService();
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        int userid = 1;
        int otherUserId = 2;

        // Setup Mocks
        when(session.getAttribute("userid")).thenReturn(otherUserId);
        when(request.getSession(false)).thenReturn(session);

        IHttpService http = mock(IHttpService.class);
        sut.setHttpService(http);

        sut.checkGetUserInfo(request, response, userid);

        verify(http, times(1)).cancelRequest(response, SC_UNAUTHORIZED);
    }
}