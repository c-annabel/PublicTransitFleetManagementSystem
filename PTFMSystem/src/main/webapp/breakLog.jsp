<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Break Log</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <h2>Break Log</h2>

    <!-- Placeholder for messages -->
    <%
        String message = request.getParameter("msg");
        if (message != null) {
    %>
        <div class="message success"><%= message %></div>
    <% } %>

    <!-- Placeholder for break log table -->
    <table border="1" width="100%" cellpadding="8" style="margin-top:10px;">
        <thead>
            <tr>
                <th>Log ID</th>
                <th>Vehicle ID</th>
                <th>Start Time</th>
                <th>End Time</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td colspan="4" style="text-align:center;">No break logs available yet.</td>
            </tr>
        </tbody>
    </table>

    <!-- Navigation -->
    <p style="text-align:center; margin-top:15px;">
        <a href="dashboard.jsp">Back to Dashboard</a>
    </p>
</div>
</body>
</html>
