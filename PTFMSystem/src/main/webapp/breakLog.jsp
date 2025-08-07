<%--
  /**
   * BreakLog.jsp - Operator Break Logging Interface
   *
   * This page is part of the CST8288 Final Project.
   * It allows Operators to start, pause, and end their break sessions, 
   * and displays a table of their break log history.
   *
   * Features:
   * - Authenticates that only logged-in Operators may access this page.
   * - Provides interactive controls to manage break records.
   * - Displays a formatted table of break logs retrieved from the backend.
   * - Uses messages to confirm success/error operations.
   *
   * @author Annabel Cheng
   */
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, transferobjects.BreakLog, business.BreakLogService, transferobjects.User" %>

<%
    // Ensure user is logged in and is an Operator
    User user = (User) session.getAttribute("user");
    if (user == null || !"Operator".equalsIgnoreCase(user.getUserType())) {
        response.sendRedirect("login.jsp");
        return;
    }

    BreakLogService breakLogService = new BreakLogService();
    List<BreakLog> logs = new ArrayList<>();
    try {
        logs = breakLogService.getBreakLogs(user.getUserId());
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Break Log</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .row {
            display: flex;
            align-items: center;
            gap: 20px;
            margin-bottom: 10px;
        }
        .row label {
            width: 130px;
            margin-right: 8px;
            font-weight: bold;
        }
        .row input {
            width: 200px;
            padding: 8px;
            flex: 1;
        }
        .row button {
            width: 200px;
            padding: 8px 12px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            flex: 1;
        }
        .row button:hover {
            background-color: #0056b3;
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
        .message {
          text-align: center;
          margin: 10px 0;
          padding: 8px;
          border-radius: 4px;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
<div class="container_management">

    <!-- Back to dashboard -->
    <div style="text-align:right; margin-bottom:10px;">
        <a href="dashboard.jsp" class="back-btn">Back to Dashboard</a>
    </div>

    <h2>Break Log</h2>

    <!-- Show feedback message if available -->
    <%
        String msg = request.getParameter("msg");
        String type = request.getParameter("type");
        if (msg != null && !msg.isEmpty()) {
            String cssClass = "message";
            if ("success".equals(type)) {
                cssClass += " success";
            } else if ("error".equals(type)) {
                cssClass += " error";
            }
    %>
        <div class="<%= cssClass %>">
            <%= msg %>
        </div>
    <% } %>

    <!-- Break Form -->
    <form action="breakAction" method="post">
        <input type="hidden" name="operatorId" value="<%= user.getUserId() %>">

        <!-- Start Break -->
        <div class="row">
            <label for="vehicleId">Vehicle ID:</label>
            <input type="number" id="vehicleId" name="vehicleId">
            <button type="submit" name="action" value="start">Start Break</button>
        </div>

        <!-- Pause/End Break -->
        <div class="row">
            <label for="breakId">Break ID:</label>
            <input type="number" id="breakId" name="breakId">
            <button type="submit" name="action" value="pause">Pause Break</button>
            <button type="submit" name="action" value="end">End Break</button>
        </div>
    </form>

    <br><br>

    <!-- Display Logs -->
    <h3>Your Break Logs</h3>
    <table>
        <thead>
            <tr>
                <th>Break ID</th>
                <th>Vehicle ID</th>
                <th>Start Time</th>
                <th>End Time</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
        <%
            if (logs != null && !logs.isEmpty()) {
                for (BreakLog log : logs) {
        %>
            <tr>
                <td><%= log.getBreakId() %></td>
                <td><%= log.getVehicleId() %></td>
                <td><%= log.getStartTime() %></td>
                <td><%= (log.getEndTime() != null ? log.getEndTime() : "-") %></td>
                <td><%= log.getStatus() %></td>
            </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="5">No break logs available.</td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>
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
