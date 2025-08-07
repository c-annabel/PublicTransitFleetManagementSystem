<%@ page import="transferobjects.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Retrieve user session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp?error=unauthorized");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .dashboard-container {
            width: 600px;
            height: 600px;
            margin: 100px auto;
            background: #ffffff;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .dashboard-container h2 {
            text-align: center;
            color: #003366;
            margin-bottom: 20px;
        }
        ul {
            list-style-type: none;
            padding: 0;
            text-align: center;
        }
        ul li {
            margin: 20px 0;
        }
        ul li a {
            display: block;
            padding: 12px;
            background: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 4px;
            font-size: 16px;
            transition: background 0.3s ease;
        }
        ul li a:hover {
            background: #0056b3;
        }
        .message {
            text-align: center;
            margin-bottom: 10px;
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="dashboard-container">
    <h2>Welcome, <%= user.getName() %> (<%= user.getUserType() %>)</h2>

    <!-- Display error messages -->
    <%
        String error = request.getParameter("error");
        if ("noaccess".equals(error)) {
    %>
        <div class="message">Access Denied: You are not authorized to view that page.</div>
    <% } else if ("sessionExpired".equals(error)) { %>
        <div class="message">Session expired. Please log in again.</div>
    <% } %>
    <br>
    <ul>
        <% if ("Manager".equalsIgnoreCase(user.getUserType())) { %>
            <li><a href="vehicleManagement.jsp">Vehicle Management</a></li>
            <li><a href="gpsOperator.jsp">GPS Tracking Report</a></li>
            <li><a href="monitoring.jsp">Fuel/Energy Monitor</a></li>
            <li><a href="alertsMaintenance.jsp">Vehicle System Monitor</a></li>
            <li><a href="reports.jsp">Reports</a></li>
        <% } else { %>
            <li><a href="breakLog.jsp">Break Log</a></li>
            <li><a href="gpsOperator.jsp" class="btn">GPS Logging</a></li>
            <li><a href="reports.jsp?view=performance">Performance</a></li>

        <% } %>
        
        <li><br><a href="logout">Logout</a></li>
    </ul>
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