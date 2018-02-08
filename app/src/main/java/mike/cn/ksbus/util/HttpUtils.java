package mike.cn.ksbus.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Mike
 * @project KSBus
 * @date 07/02/2018, 9:24 PM
 * @e-mail mike@mikecoder.cn
 */

public class HttpUtils {
    public static String executeHttpGet(String url) {
        String            result     = "";
        URL               u          = null;
        HttpURLConnection connection = null;
        InputStreamReader in         = null;
        try {
            u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "*/*");
            
            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer   strBuffer      = new StringBuffer();
            String         line           = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
