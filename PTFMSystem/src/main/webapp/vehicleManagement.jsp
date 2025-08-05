<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, transferobjects.Vehicle, business.VehicleService" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Vehicle Management</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="container_management">
    <div style="text-align:right; margin-bottom:10px;">
        <a href="dashboard.jsp" class="back-btn">Back to Dashboard</a>
    </div>
    <h2>Vehicle Management</h2>

    <!-- ✅ Feedback Messages -->
    <%
        String success = request.getParameter("success");
        String error = request.getParameter("error");
        if ("1".equals(success)) {
    %>
        <div class="message success">Vehicle added successfully.</div>
    <%
        } else if ("updated".equals(success)) {
    %>
        <div class="message success">Vehicle updated successfully.</div>
    <%
        } else if ("deleted".equals(success)) {
    %>
        <div class="message success">Vehicle deleted successfully.</div>
    <%
        } else if ("1".equals(error)) {
    %>
        <div class="message">Error: Could not process the request (missing fields or duplicate number).</div>
    <%
        }
    %>

    <!-- ✅ Single Form for Add/Update/Delete -->
    <form action="addVehicle" method="post">
        <input type="hidden" id="vehicleId" name="vehicleId">
        
        <div class="form-row">
            <label for="vehicleNumber">Vehicle Number:</label>
            <input type="text" id="vehicleNumber" name="vehicleNumber" required>
        </div>
        <div class="form-row">
            <label for="vehicleType">Vehicle Type:</label>
            <select id="vehicleType" name="vehicleType">
                <option>Diesel Bus</option>
                <option>Electric Light Rail</option>
                <option>Diesel-Electric Train</option>
            </select>
        </div>
        <div class="form-row">
            <label for="fuelType">Fuel Type:</label>
            <select id="fuelType" name="fuelType">
                <option>Diesel</option>
                <option>Electric</option>
                <option>Diesel-Electric</option>
            </select>
        </div>
        <div class="form-row">
            <label for="consumptionRate">Consumption Rate:</label>
            <input type="number" step="0.01" id="consumptionRate" name="consumptionRate" required>
        </div>
        <div class="form-row">
            <label for="maxPassengers">Max Passengers:</label>
            <input type="number" id="maxPassengers" name="maxPassengers" required>
        </div>
        <div class="form-row">
            <label for="routeId">Route ID:</label>
            <input type="number" id="routeId" name="routeId" required>
        </div>

        <div class="button-row">
            <button type="submit">Add Vehicle</button>
            <button type="submit" formaction="updateVehicle">Update Vehicle</button>
            <button type="submit" formaction="deleteVehicle">Delete Vehicle</button>
        </div>

        <h3>Existing Vehicles</h3>
        <table class="styled-table" width="100%">
            <tr>
                <th>Select</th><th>ID</th><th>Number</th><th>Type</th><th>Fuel</th><th>Rate</th><th>Passengers</th><th>Route</th>
            </tr>
            <%
                VehicleService service = new VehicleService();
                List<Vehicle> vehicles = service.getAllVehicles();
                for (Vehicle v : vehicles) {
            %>
            <tr onclick="selectVehicle('<%=v.getVehicleId()%>', '<%=v.getVehicleNumber()%>', '<%=v.getVehicleType()%>', '<%=v.getFuelType()%>', '<%=v.getConsumptionRate()%>', '<%=v.getMaxPassengers()%>', '<%=v.getRouteId()%>')">
                <td style="width: 1%; white-space: nowrap; text-align: center;">
                    <input type="radio" name="selectedVehicle" value="<%= v.getVehicleId() %>">
                </td>
                <td><%= v.getVehicleId() %></td>
                <td><%= v.getVehicleNumber() %></td>
                <td><%= v.getVehicleType() %></td>
                <td><%= v.getFuelType() %></td>
                <td><%= v.getConsumptionRate() %></td>
                <td><%= v.getMaxPassengers() %></td>
                <td><%= v.getRouteId() %></td>
            </tr>
            <% } %>
        </table>
        <br><br>
    </form>
</div>

<!-- ✅ JavaScript to Fill Form on Row Click -->
<script>
function selectVehicle(id, number, type, fuel, rate, passengers, route) {
    document.getElementById('vehicleId').value = id;
    document.getElementById('vehicleNumber').value = number;
    document.getElementById('vehicleType').value = type;
    document.getElementById('fuelType').value = fuel;
    document.getElementById('consumptionRate').value = rate;
    document.getElementById('maxPassengers').value = passengers;
    document.getElementById('routeId').value = route;
}
</script>

</body>
<footer>
    <div class="footer">
        <p>Developed by: Annabel Cheng &copy; 2025</p>
        <p>25S CST8288 Section 013 Final Project</p>
    </div>
    <br><br>
</footer>

</html>
