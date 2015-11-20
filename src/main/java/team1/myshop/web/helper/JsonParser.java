package team1.myshop.web.helper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonParser {

    /**
     * Parses the data string in instance of target
     * @param data the input string
     * @param target the class of the target type
     * @param <T> the target type
     * @return a instance of the target type or null
     */
    public static <T> T parse(String data, Class<T> target){
        ObjectMapper om = new ObjectMapper();
        T result = null;

        try {
            result = om.readValue(data, target);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
