<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, transferobjects.User, business.GPSLogService, dataaccess.VehicleDAO, dataaccess.StationDAO" %>
<%
    // Validate user session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    boolean isManager = "Manager".equalsIgnoreCase(user.getUserType());

    // Load dropdown data
    VehicleDAO vehicleDAO = new VehicleDAO();
    StationDAO stationDAO = new StationDAO();
    List<String[]> vehicles = new ArrayList<>();
    List<String[]> stations = new ArrayList<>();
    try {
        vehicles = vehicleDAO.getVehicleIdAndNumber();
        stations = stationDAO.getStationIdAndName();
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Load GPS logs for report
    GPSLogService gpsService = new GPSLogService();
    List<String[]> logs = new ArrayList<>();
    try {
        logs = gpsService.getDetailedLogs();
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>GPS Tracking</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .row {
            display: flex;
            align-items: center;
            gap: 20px;
            margin-bottom: 10px;
        }
        .row label {
            width: 150px;
            font-weight: bold;
        }
        .row select {
            width: 250px;
            padding: 8px;
        }
        .row button {
            padding: 10px 18px;
            border: none;
            border-radius: 4px;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }
        .btn-arrival {
            background-color: #28a745;
        }
        .btn-arrival:hover {
            background-color: #218838;
        }
        .btn-departure {
            background-color: #007bff;
        }
        .btn-departure:hover {
            background-color: #0056b3;
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
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #007bff;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<div class="container_management">
    <!-- Back Button -->
    <div style="text-align:right; margin-bottom:10px;">
        <a href="dashboard.jsp" class="back-btn">Back to Dashboard</a>
    </div>
    <h2>GPS Tracking</h2>

    <!-- Feedback message -->
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
        <div class="<%= cssClass %>"><%= msg %></div>
    <% } %>

    <!-- Operator Form (disabled for Manager) -->
    <%
        if (!isManager) {
    %>
    <form action="gpsAction" method="post">
        <div class="row">
            <label for="vehicleId">Select Vehicle:</label>
            <select name="vehicleId" id="vehicleId" required>
                <option value="">-- Select Vehicle --</option>
                <%
                    for (String[] v : vehicles) {
                %>
                    <option value="<%= v[0] %>"><%= v[1] %></option>
                <%
                    }
                %>
            </select>
        </div>

        <div class="row">
            <label for="stationId">Select Station:</label>
            <select name="stationId" id="stationId" required>
                <option value="">-- Select Station --</option>
                <%
                    for (String[] s : stations) {
                %>
                    <option value="<%= s[0] %>"><%= s[1] %></option>
                <%
                    }
                %>
            </select>
        </div>

        <div class="row">
            <button type="submit" name="action" value="arrival" class="btn-arrival">Log Arrival</button>
            <button type="submit" name="action" value="departure" class="btn-departure">Log Departure</button>
        </div>
    </form>
    <% } %>

    <!-- GPS Logs Report -->
    <h3>GPS Logs Report</h3>
    <table>
        <thead>
            <tr>
                <th>Vehicle</th>
                <th>Station</th>
                <th>Arrival Time</th>
                <th>Departure Time</th>
            </tr>
        </thead>
        <tbody>
        <%
            if (logs != null && !logs.isEmpty()) {
                for (String[] log : logs) {
        %>
            <tr>
                <td><%= log[0] %></td>
                <td><%= log[1] %></td>
                <td><%= log[2] %></td>
                <td><%= log[3] %></td>
            </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="4">No GPS logs available.</td>
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
