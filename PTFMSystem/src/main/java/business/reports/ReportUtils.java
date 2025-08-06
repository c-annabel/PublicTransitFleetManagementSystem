package business.reports;

import com.google.gson.JsonArray;

public class ReportUtils {

    public static JsonArray toJsonArray(String[] arr) {
        JsonArray jsonArray = new JsonArray();
        for (String s : arr) jsonArray.add(s);
        return jsonArray;
    }

    public static JsonArray toJsonArray(double[] arr) {
        JsonArray jsonArray = new JsonArray();
        for (double d : arr) jsonArray.add(d);
        return jsonArray;
    }
}
