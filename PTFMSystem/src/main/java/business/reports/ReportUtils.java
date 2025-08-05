package business.reports;

import com.google.gson.JsonArray;

public class ReportUtils {

    public static JsonArray createJsonArray(String[] values) {
        JsonArray arr = new JsonArray();
        for (String val : values) {
            arr.add(val);
        }
        return arr;
    }

    public static JsonArray createJsonArray(double[] values) {
        JsonArray arr = new JsonArray();
        for (double val : values) {
            arr.add(val);
        }
        return arr;
    }
}
