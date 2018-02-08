package mike.cn.ksbus.network;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mike
 * @project KSBus
 * @date 07/02/2018, 9:25 PM
 * @e-mail mike@mikecoder.cn
 */

public class Spec {

    public static String filter(String content) {
        Pattern pattern = Pattern.compile("<div class=\"busTip.*?/stationGps/.*?\">(.*?)</a>");
        Matcher matcher = pattern.matcher(content);

        String str = "";
        while (matcher.find()) {
            str += matcher.group(1);
            str += "\n";
        }

        return str.equals("") ? "没有车！\n" : str;
    }
}
