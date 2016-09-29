package android.test.seriespopularesapp.util.json;

import android.test.seriespopularesapp.util.json.ListOfJson;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by re032629 on 25/09/2016.
 */

public class JsonUtil {

    public <T> List<T> fromJsonList(String json, Class<T> typeClass){
        try {
            return new Gson().fromJson(json, new ListOfJson<T>(typeClass));
        }
        catch (Exception ex){
            return  null;
        }
    }

}
