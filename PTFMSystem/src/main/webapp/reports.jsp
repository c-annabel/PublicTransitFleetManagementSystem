<%--
/**
 * Reports and Analytics Page.
 * This page displays different reports and analytics based on the user's role.
 * If the user is a "Manager", it shows various charts for maintenance and cost analysis.
 * If the user is an "Operator", it shows their individual performance metrics.
 * Unauthorized access is redirected to the login page.
 *
 * @author Annabel Cheng
 * @version 1.0
 * @since 2025-08-07
 */
--%>
<%@ page import="transferobjects.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    /**
     * Retrieves the User object from the session.
     * If the user is not logged in (user is null), it redirects to the login page
     * with an "unauthorized" error.
     */
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp?error=unauthorized");
        return;
    }
    
    /**
     * Gets the user type from the User object to determine which view to display.
     */
    String userType = user.getUserType();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Reports & Analytics</title>
    <link rel="stylesheet" href="css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        /* CSS styles for page layout and chart appearance */
        body { font-family: Arial, sans-serif; background: #f4f4f4; background-color: lightcyan; }
        h2 { text-align: center; color: #003366; margin: 20px 0; }
        .filters { text-align: center; margin-bottom: 20px; }
        .filters select, .filters button { padding: 6px; margin: 5px; }
        .chart-section { margin: 30px 0; }
        .chart-section h3 { text-align: center; margin-bottom: 10px; }
        canvas { display: block; margin: 0 auto; max-width: 800px; }
        .performance-card { max-width: 400px; margin: 40px auto; padding: 20px; background: #fff; border-radius: 8px; text-align: center; border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        .performance-card h3 { margin: 15px 0; color: #333; }
        .back-link { display: block; text-align: center; margin-top: 20px; }
        .back-link a { color: #007bff; text-decoration: none; }
        .back-link a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="container_management">
    <div style="text-align:right; margin-bottom:10px;">
        <a href="dashboard.jsp" class="back-btn">Back to Dashboard</a>
    </div>
    <h2>Reports & Analytics</h2>

    <% if ("Manager".equalsIgnoreCase(userType)) { %>
    <div class="filters">
        <label for="monthSelect" style="margin-right: 10px;">Select Month:</label>
        <div style="display: inline-flex; align-items: center; gap: 8px;">
            <select id="monthSelect"></select>
            <select id="yearSelect"></select>
            <button onclick="loadReports()">Apply Filters</button>
        </div>
    </div>

    <div class="chart-section">
        <h3>Maintenance Summary</h3>
        <canvas id="maintenanceSummaryChart"></canvas>
    </div>

    <div class="chart-section">
        <h3>Maintenance Cost Trend</h3>
        <canvas id="maintenanceTrendChart"></canvas>
    </div>

    <div class="chart-section">
        <h3>Cost Analysis (Fuel & Maintenance)</h3>
        <canvas id="costAnalysisChart"></canvas>
    </div>

    <script>
        const ctxSummary = document.getElementById('maintenanceSummaryChart');
        const ctxTrend = document.getElementById('maintenanceTrendChart');
        const ctxCost = document.getElementById('costAnalysisChart');

        function populateMonthYearDropdowns() {
            const monthSelect = document.getElementById("monthSelect");
            const yearSelect = document.getElementById("yearSelect");

            // Hardcoded: Allow only July and August
            const months = [
                { value: 7, name: "July" },
                { value: 8, name: "August" }
            ];

            const year = 2025;

            months.forEach(month => {
                const option = document.createElement("option");
                option.value = month.value;
                option.text = month.name;
                monthSelect.appendChild(option);
            });

            const yearOption = document.createElement("option");
            yearOption.value = year;
            yearOption.text = year;
            yearSelect.appendChild(yearOption);

            // Set default to July
            monthSelect.value = 7;
            yearSelect.value = 2025;
        }


        function getSelectedDateRange() {
            const month = parseInt(document.getElementById("monthSelect").value);
            const year = parseInt(document.getElementById("yearSelect").value);

            const startDate = new Date(year, month - 1, 1);
            const endDate = new Date(year, month, 0); // Last day of selected month

            const startStr = startDate.toISOString().split('T')[0];
            const endStr = endDate.toISOString().split('T')[0];
            return { startStr, endStr };
        }
        
        let chartSummary = null;
        let chartTrend = null;
        let chartCost = null;

        function loadReports() {
            const { startStr, endStr } = getSelectedDateRange();

            // Maintenance Summary
            fetch('<%=request.getContextPath()%>/ReportServlet?action=maintenance&startDate=' + startStr + '&endDate=' + endStr)
                .then(response => response.json())
                .then(data => {
                    if (chartSummary) chartSummary.destroy(); // 🔁 Destroy previous instance
                    if (chartTrend) chartTrend.destroy();

                    if (data.labels && data.values) {
                        chartSummary = new Chart(ctxSummary, {
                            type: 'pie',
                            data: {
                                labels: data.labels,
                                datasets: [{
                                    data: data.values,
                                    backgroundColor: ['#28a745', '#dc3545']
                                }]
                            }
                        });
                    }

                    if (data.trend && data.trend.labels && data.trend.values) {
                        chartTrend = new Chart(ctxTrend, {
                            type: 'line',
                            data: {
                                labels: data.trend.labels,
                                datasets: [{
                                    label: 'Maintenance Cost ($)',
                                    data: data.trend.values,
                                    borderColor: '#007bff',
                                    fill: false
                                }]
                            }
                        });
                    }
                });

            // Cost Analysis
            fetch('<%=request.getContextPath()%>/ReportServlet?action=cost&startDate=' + startStr + '&endDate=' + endStr)
                .then(response => response.json())
                .then(data => {
                    if (chartCost) chartCost.destroy(); // 🔁 Destroy previous instance

                    if (data.labels && data.values) {
                        chartCost = new Chart(ctxCost, {
                            type: 'bar',
                            data: {
                                labels: data.labels,
                                datasets: [{
                                    label: 'Cost ($)',
                                    data: data.values,
                                    backgroundColor: ['#17a2b8', '#ffc107']
                                }]
                            }
                        });
                    }
                });
        }


        // === Init ===
        document.addEventListener("DOMContentLoaded", function () {
            populateMonthYearDropdowns();
            loadReports(); // default to last month
        });
    </script>

    <% } else if ("Operator".equalsIgnoreCase(userType)) { %>
    <div class="performance-card">
        <h3>Loading Performance...</h3>
    </div>



    <script>
    document.addEventListener("DOMContentLoaded", function () {
        fetch('ReportServlet?action=operatorPerformance&operatorId=<%= user.getUserId() %>')
            .then(response => response.json())
            .then(data => {
                document.querySelector('.performance-card').innerHTML = `
                    <h2>My Performance</h2>
                    <h3>On-Time Arrival Rate: \${Number(data.onTimeRate).toFixed(2)}%</h3>
                    <h3>Efficiency Score: \${Number(data.efficiencyScore).toFixed(2)}%</h3>
                `;
            })
            .catch(err => {
                console.error("Failed to fetch performance data", err);
                document.querySelector('.performance-card').innerHTML = `
                    <h2>Performance Unavailable</h2>
                    <p>Could not load your data. Please try again later.</p>
                `;
            });
    });
    </script>

    <% } else { %>
    <p style="text-align:center;">You do not have access to this section.</p>
    <% } %>
</div>
</body>
<footer>
    <div class="footer">
        <p>Developed by: Annabel Cheng &copy; 2025</p>
        <p>25S CST8288 Section 013 Final Project</p>
    </div>
    <br><br>
</footer>
</html>