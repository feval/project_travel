package com.lin.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lin.vo.MessageVO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * Description:
 * Author:  llf
 * Created in 2019/8/23 16:31
 */
public class CommUtils {
    //谷歌GSON类      建造者模式   创建对象
    private static final Gson GSON=new GsonBuilder().create();

    public static Properties loadProperties(String fileName) {
        Properties properties=new Properties();
        InputStream in=CommUtils.class.getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(in);
        } catch (IOException e) {
            return null;
        }
        return properties;
    }

    public static String object2Json(Object obj) {
        return GSON.toJson(obj);
    }

    public static Object json2Object(String jsonStr, Class objClass) {
        return GSON.fromJson(jsonStr,objClass);

    }
}
