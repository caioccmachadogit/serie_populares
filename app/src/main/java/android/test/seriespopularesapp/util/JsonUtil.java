package android.test.seriespopularesapp.util;

import android.test.seriespopularesapp.model.Series;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
