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
    String viewMode = request.getParameter("view"); // For operators: view=performance
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
        .container { width: 90%; margin: 30px auto; background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #003366; margin-bottom: 20px; }
        .filters { text-align: center; margin-bottom: 20px; }
        .filters input, .filters button { padding: 6px; margin: 5px; }
        .chart-section { margin: 30px 0; }
        .chart-section h3 { text-align: center; margin-bottom: 10px; }
        canvas { display: block; margin: 0 auto; max-width: 600px; }
        .performance-card { max-width: 400px; margin: 30px auto; padding: 20px; background: #f9f9f9; border-radius: 6px; text-align: center; border: 1px solid #ddd; }
        .performance-card h3 { margin: 10px 0; color: #333; }
        .back-link { display: block; text-align: center; margin-top: 20px; }
        .back-link a { color: #007bff; text-decoration: none; }
        .back-link a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="container">
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

        <!-- Maintenance Dashboard -->
        <div class="chart-section">
            <h3>Maintenance Summary</h3>
            <canvas id="maintenanceChart" width="400" height="200"></canvas>
        </div>

        <div class="chart-section">
            <h3>Maintenance Cost Trend</h3>
            <canvas id="maintenanceCostChart" width="400" height="200"></canvas>
        </div>

        <!-- Cost Analysis -->
        <div class="chart-section">
            <h3>Cost Analysis (Fuel & Maintenance)</h3>
            <canvas id="costChart" width="400" height="200"></canvas>
        </div>

        <script>
            function loadReports() {
                let startDate = document.getElementById("startDate").value;
                let endDate = document.getElementById("endDate").value;

                fetch(`ReportServlet?action=all&startDate=${startDate}&endDate=${endDate}`)
                    .then(response => response.json())
                    .then(data => {
                        updateChart(maintenanceChart, data.maintenance.labels, data.maintenance.values);
                        updateChart(maintenanceCostChart, data.costTrend.labels, data.costTrend.values);
                        updateChart(costChart, data.cost.labels, data.cost.values);
                    });
            }

            function updateChart(chart, labels, values) {
                chart.data.labels = labels;
                chart.data.datasets[0].data = values;
                chart.update();
            }

            // Initialize Charts
            let maintenanceChart = new Chart(document.getElementById('maintenanceChart'), {
                type: 'bar',
                data: { labels: [], datasets: [{ label: 'Tasks', data: [], backgroundColor: '#007bff' }] }
            });

            let maintenanceCostChart = new Chart(document.getElementById('maintenanceCostChart'), {
                type: 'line',
                data: { labels: [], datasets: [{ label: 'Cost ($)', data: [], borderColor: '#28a745', fill: false }] }
            });

            let costChart = new Chart(document.getElementById('costChart'), {
                type: 'pie',
                data: { labels: [], datasets: [{ label: 'Cost', data: [], backgroundColor: ['#ff6384','#36a2eb','#cc65fe'] }] }
            });
        </script>

    <% } else if ("performance".equalsIgnoreCase(viewMode)) { %>
        <!-- ============================= -->
        <!-- OPERATOR VIEW -->
        <!-- ============================= -->
        <div class="performance-card">
            <h3>Loading your performance...</h3>
        </div>


        <script>
            fetch('ReportServlet?action=operatorPerformance&operatorId=<%= user.getUserId() %>')
                .then(response => response.json())
                .then(data => {
                    document.querySelector('.performance-card').innerHTML = `
                        <h2>My Performance</h2>
                        <h3>On-Time Arrival Rate: ${data.onTimeRate}%</h3>
                        <h3>Efficiency Score: ${data.efficiencyScore}%</h3>
                    `;
                });
        </script>

    <% } else { %>
        <p style="text-align:center;">You do not have access to this section.</p>
    <% } %>
</div>
</body>
</html>
