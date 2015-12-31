package team1.myshop.contracts;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IHttpService {
    /**
     * Cancels the current request and send a status code to the client
     * @param response the response for the request
     * @param status the status code to sent
     */
    void cancelRequest(HttpServletResponse response, int status);

    /**
     * Sends a http post request to the given url and returns the response body
     * @param url
     * @param params
     * @param headers
     * @return
     */
    String post(String url, Map<String, String> params, Map<String, String> headers);

    String get(String url, Map<String, String> params, Map<String, String> headers);
}
