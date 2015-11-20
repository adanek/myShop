package team1.myshop.contracts;

import javax.servlet.http.HttpServletResponse;

public interface IHttpService {
    /**
     * Cancels the current request and send a status code to the client
     * @param response the response for the request
     * @param status the status code to sent
     */
    void cancelRequest(HttpServletResponse response, int status);
}
