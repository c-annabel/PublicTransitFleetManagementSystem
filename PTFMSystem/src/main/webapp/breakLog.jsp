<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Break Log</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .container {
            width: 600px;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 6px;
            background-color: #f9f9f9;
        }
        h2 {
            color: #003366;
            text-align: center;
        }
        .message {
            text-align: center;
            margin-bottom: 10px;
            color: red;
        }
        .success {
            color: green;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #007bff;
            color: #fff;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        .back-link {
            display: inline-block;
            margin-top: 15px;
            text-decoration: none;
            background: #007bff;
            color: #fff;
            padding: 10px 15px;
            border-radius: 4px;
        }
        .back-link:hover {
            background: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Break Log</h2>

    <!-- Display message if present -->
    <%
        String message = request.getParameter("msg");
        if (message != null && !message.trim().isEmpty()) {
    %>
        <div class="message success"><%= message %></div>
    <% } %>

    <!-- Break Log Table -->
    <table>
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
                <td colspan="4">No break logs available yet.</td>
            </tr>
        </tbody>
    </table>

    <!-- Navigation -->
    <p style="text-align:center;">
        <a href="dashboard.jsp" class="back-link">Back to Dashboard</a>
    </p>
</div>
</body>
</html>
