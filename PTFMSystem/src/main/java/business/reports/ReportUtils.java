package business.reports;

import com.google.gson.JsonArray;

/**
 * Utility class for converting Java arrays to JSON arrays.
 * 
 * This class provides helper methods to transform arrays of strings or doubles
 * into {@code JsonArray} format, which is useful for building structured report
 * data in JSON responses.
 * 
 * These methods are commonly used in report generation classes such as
 * {@code CostReport}, {@code MaintenanceReport}, and {@code OperatorPerformanceReport}.
 * 
 * @author Annabel Cheng
 * @course Course 25S CST8288 Lab013 Final Project
 */
public class ReportUtils {

    /**
     * Converts a {@code String[]} to a {@code JsonArray}.
     *
     * @param arr the array of strings to convert
     * @return a {@code JsonArray} containing all strings from the array
     */
    public static JsonArray toJsonArray(String[] arr) {
        JsonArray jsonArray = new JsonArray();
        for (String s : arr) jsonArray.add(s);
        return jsonArray;
    }

    /**
     * Converts a {@code double[]} to a {@code JsonArray}.
     *
     * @param arr the array of double values to convert
     * @return a {@code JsonArray} containing all doubles from the array
     */
    public static JsonArray toJsonArray(double[] arr) {
        JsonArray jsonArray = new JsonArray();
        for (double d : arr) jsonArray.add(d);
        return jsonArray;
    }
}
