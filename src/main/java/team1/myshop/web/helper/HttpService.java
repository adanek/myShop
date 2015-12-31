package team1.myshop.web.helper;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpService implements team1.myshop.contracts.IHttpService {

    @Override
    public void cancelRequest(HttpServletResponse response, int status) {
        try {
            response.sendError(status);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String post(String url, Map<String, String> params, Map<String, String> headers) {
        return this.sendRequest("POST", url, params, headers);
    }

    @Override
    public String get(String url, Map<String, String> params, Map<String, String> headers) {
        try {
            URL target_url = new URL(url);
            URLConnection connection = target_url.openConnection();
            connection.setUseCaches(false);

            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }

            InputStream resp = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp));
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                body.append(line);
                body.append("\n");
            }
            rd.close();

            return body.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String sendRequest(String method, String url, Map<String, String> params, Map<String, String> headers) {
        try {
            URL target_url = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) target_url.openConnection();
            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }


            String queryString = getQueryString(params);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(queryString);
            wr.flush();
            wr.close();

            InputStream resp = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp));
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                body.append(line);
                body.append("\n");
            }
            rd.close();

            return body.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getQueryString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : params.keySet()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }

            sb.append(String.format("%s=%s", URLEncoder.encode(key, "UTF-8"), URLEncoder.encode(params.get(key), "UTF-8")));
        }
        return sb.toString();
    }
}
