package team1.myshop.web.helper;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpService implements team1.myshop.contracts.IHttpService {

    @Override
    public void cancelRequest(HttpServletResponse response, int status){
        try {
            response.sendError(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
