package business;

import business.reports.CostReport;
import business.reports.MaintenanceReport;
import business.reports.OperatorPerformanceReport;
import business.reports.Report;

/**
 * A factory class for creating report instances based on the specified report type.
 * This factory supports maintenance, cost, and operator performance reports.
 *
 * @author Annabel Cheng
 * Course 25S CST8288 Lab013 Final Project
 */
public class ReportFactory {

    /**
     * Returns a specific type of {@link Report} based on the provided report type string.
     *
     * @param reportType A string representing the type of report to generate. 
     *                   Expected values: "maintenance", "cost", or "operatorPerformance".
     * @return An instance of a class that implements the {@code Report} interface.
     * @throws IllegalArgumentException if the {@code reportType} is null or does not match any supported type.
     */
    public static Report getReport(String reportType) {
        if (reportType == null) {
            throw new IllegalArgumentException("Report type cannot be null");
        }

        switch (reportType) {
            case "maintenance":
                return new MaintenanceReport();
            case "cost":
                return new CostReport();
            case "operatorPerformance":
                return new OperatorPerformanceReport();
            default:
                throw new IllegalArgumentException("Invalid report type: " + reportType);              
        }
    }
}
