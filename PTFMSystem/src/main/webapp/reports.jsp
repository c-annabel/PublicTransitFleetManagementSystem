<%@ page import="transferobjects.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Session check
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp?error=unauthorized");
        return;
    }

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
        body { font-family: Arial, sans-serif; background: #f4f4f4; }
        h2 { text-align: center; color: #003366; margin: 20px 0; }
        .filters { text-align: center; margin-bottom: 20px; }
        .filters input, .filters button { padding: 6px; margin: 5px; }
        .chart-section { margin: 30px 0; }
        .chart-section h3 { text-align: center; margin-bottom: 10px; }
        canvas { display: block; margin: 0 auto; max-width: 600px; }
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
        <!-- ============================= -->
        <!-- MANAGER VIEW -->
        <!-- ============================= -->
        <div class="filters">
            <label>Date Range:</label>
            <input type="date" id="startDate">
            <input type="date" id="endDate">
            <button onclick="loadReports()">Apply Filters</button>
        </div>

        <!-- Maintenance Summary -->
        <div class="chart-section">
            <h3>Maintenance Summary</h3>
            <canvas id="maintenanceSummaryChart"></canvas>
        </div>

        <!-- Maintenance Cost Trend -->
        <div class="chart-section">
            <h3>Maintenance Cost Trend</h3>
            <canvas id="maintenanceTrendChart"></canvas>
        </div>

        <!-- Cost Analysis -->
        <div class="chart-section">
            <h3>Cost Analysis (Fuel & Maintenance)</h3>
            <canvas id="costAnalysisChart"></canvas>
        </div>

        <script>
            const ctxSummary = document.getElementById('maintenanceSummaryChart');
            const ctxTrend = document.getElementById('maintenanceTrendChart');
            const ctxCost = document.getElementById('costAnalysisChart');

            function loadReports() {
                let startDate = document.getElementById("startDate").value;
                let endDate = document.getElementById("endDate").value;

                // Maintenance Summary & Trend
                fetch('<%=request.getContextPath()%>/ReportServlet?action=maintenance&startDate=' + startDate + '&endDate=' + endDate)
                    .then(response => response.json())
                    .then(data => {
                        if (data.labels && data.values) {
                            new Chart(ctxSummary, {
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
                        if (data.trend) {
                            new Chart(ctxTrend, {
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
                fetch('<%=request.getContextPath()%>/ReportServlet?action=cost&startDate=' + startDate + '&endDate=' + endDate)
                    .then(response => response.json())
                    .then(data => {
                        if (data.labels && data.values) {
                            new Chart(ctxCost, {
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

            // Load default data on page load
            loadReports();
        </script>

    <% } else if ("Operator".equalsIgnoreCase(userType)) { %>
        <!-- ============================= -->
        <!-- OPERATOR VIEW -->
        <!-- ============================= -->
        <div class="performance-card">
            <h3>Loading Performance...</h3>
        </div>

        <script>
        document.addEventListener("DOMContentLoaded", function() {
            fetch('<%=request.getContextPath()%>/ReportServlet?action=operatorPerformance&operatorId=<%=user.getUserId()%>')
                .then(response => response.json())
                .then(data => {
                    console.log("Operator Performance Data:", data); // Debug
                    document.querySelector('.performance-card').innerHTML = `
                        <h2>My Performance</h2>
                        <h3>On-Time Arrival Rate: ` + Number(data.onTimeRate).toFixed(2) + `%</h3>
                        <h3>Efficiency Score: ` + Number(data.efficiencyScore).toFixed(2) + `%</h3>
                    `;
                })
                .catch(err => {
                    console.error("Error fetching performance:", err);
                    document.querySelector('.performance-card').innerHTML = `
                        <h2>My Performance</h2>
                        <h3>Error loading data</h3>
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
