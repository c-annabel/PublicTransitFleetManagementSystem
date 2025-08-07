<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, transferobjects.MaintenanceTask, dataaccess.MaintenanceDAO" %>
<%@ page import="java.time.LocalDate" %>

<%--
  /**
   * maintenanceSchedule.jsp - Maintenance Schedule Management Page
   *
   * This JSP page is part of the CST8288 Final Project.
   * It provides a form and table interface for managing vehicle maintenance tasks,
   * including adding, updating, and deleting scheduled maintenance records.
   *
   * Features:
   * - Displays feedback messages for add/update/delete actions
   * - Validates that only one task is allowed per day (via backend logic)
   * - Prevents editing/deletion of completed tasks
   * - Automatically disables/enables buttons based on selection
   * - JavaScript-assisted form pre-filling when selecting a task from the table
   *
   * @author Annabel Cheng
   */
--%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Maintenance Schedule Management</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        button:disabled {
            background-color: #ccc !important;
            color: #666 !important;
            cursor: not-allowed;
        }
    </style>
</head>
<body>

<div class="container_management">
    <div style="text-align:right; margin-bottom:10px;">
        <a href="dashboard.jsp" class="back-btn">Back to Dashboard</a>
    </div>
    <h2>Maintenance Schedule Management</h2>

    <!-- ✅ Feedback Messages -->
    <%
        String success = request.getParameter("success");
        String error = request.getParameter("error");

        if ("1".equals(success)) {
    %><div class="message success">Task added successfully.</div><%
        } else if ("updated".equals(success)) {
    %><div class="message success">Task updated successfully.</div><%
        } else if ("deleted".equals(success)) {
    %><div class="message success">Task deleted successfully.</div><%
        } else if ("completed".equals(error)) {
    %><div class="message error">Cannot modify/delete a completed task.</div><%
        } else if ("1".equals(error)) {
    %><div class="message error">Error: Invalid input or server issue.</div><%
        }
    %>

    <% if ("duplicate".equals(error)) { %>
    <div class="message error">Only one maintenance task can be scheduled per day. Choose another date.</div>
<% } %>


    <!-- ✅ Unified Form -->
    <form action="addMaintenance" method="post">
        <input type="hidden" id="taskId" name="taskId">

        <div class="form-row">
            <label for="vehicleId">Vehicle ID:</label>
            <input type="number" id="vehicleId" name="vehicleId" required>
        </div>
        <div class="form-row">
            <label for="description">Task Type</label>
            <select name="description" id="description" required>
                <option value="">Select Task</option>
                <option value="Engine Check">Engine Check</option>
                <option value="Pantograph Check">Pantograph Check</option>
                <option value="Brake Inspection">Brake Inspection</option>
                <option value="Inspect pantograph">Inspect pantograph</option>
                <option value="Inspect fuel system">Inspect fuel system</option>
                <option value="Check LRT electrical systems">Check LRT electrical systems</option>
                <option value="Engine overhaul">Engine overhaul</option>
                <option value="Replace brake pads">Replace brake pads</option>
                <option value="Engine diagnostics">Engine diagnostics</option>
                <option value="Engine optimization check">Engine optimization check</option>
            </select>
        </div>
        <div class="form-row">
            <label for="scheduledDate">Scheduled Date:</label>
            <input type="date" id="scheduledDate" name="scheduledDate" min="<%= LocalDate.now().plusDays(2) %>" required>
        </div>
        <div class="form-row">
            <label for="cost">Cost ($):</label>
            <input type="number" id="cost" name="cost" step="0.01" min="0.00" required>
        </div>
        <div class="form-row">
            <label for="completed">Completed:</label>
            <select id="completed" name="completed">
                <option value="false">No</option>
                <option value="true">Yes</option>
            </select>
        </div>

        <div class="button-row">
            <button type="submit" id="addBtn" formaction="addMaintenance">Add Task</button>
            <button type="submit" id="updateBtn" formaction="updateMaintenance">Update Task</button>
            <button type="submit" id="deleteBtn" formaction="deleteMaintenance">Delete Task</button>
        </div>

        <!-- ✅ Maintenance Table -->
        <h3>Existing Maintenance Tasks</h3>
        <table class="styled-table" width="100%">
            <tr>
                <th>Select</th>
                <th>Scheduled</th>
                <th>Vehicle ID</th>
                <th>Task Type</th>
                <th>Cost</th>
                <th>Completed</th>
            </tr>
            <%
                MaintenanceDAO dao = new MaintenanceDAO();
                List<MaintenanceTask> tasks = dao.getAllMaintenanceTasks();
                for (MaintenanceTask task : tasks) {
                    String dateStr = task.getScheduledDatetime().toLocalDateTime().toLocalDate().toString();
                    String descEscaped = task.getDescription().replace("'", "\'");
            %>
            <tr onclick="selectTask('<%= task.getTaskId() %>', '<%= task.getVehicleId() %>', '<%= descEscaped %>', '<%= dateStr %>', '<%= task.getCost() %>', '<%= task.isCompleted() %>')">
                <td style="text-align: center;">
                    <% if (!task.isCompleted()) { %>
                        <input type="radio" name="selectedTask" value="<%= task.getTaskId() %>">
                    <% } %>
                </td>
                <td><%= dateStr %></td>
                <td><%= task.getVehicleId() %></td>
                <td><%= task.getDescription() %></td>
                <td><%= task.getCost() %></td>
                <td><%= task.isCompleted() ? "Yes" : "No" %></td>
            </tr>
            <% } %>
        </table>
    </form>
</div>

<!-- ✅ JS Logic -->
<script>
function selectTask(id, vehicleId, description, date, cost, completed) {
    document.getElementById('taskId').value = id;
    document.getElementById('vehicleId').value = vehicleId;
    document.getElementById('description').value = description.trim();
    document.getElementById('scheduledDate').value = date;
    document.getElementById('cost').value = cost;
    document.getElementById('completed').value = completed;

    const isCompleted = completed === "true";
    document.getElementById("updateBtn").disabled = isCompleted;
    document.getElementById("deleteBtn").disabled = isCompleted;
}

window.addEventListener('DOMContentLoaded', function () {
    const params = new URLSearchParams(window.location.search);
    if (params.get("success")) {
        // Clear form inputs after success
        document.getElementById("taskId").value = "";
        document.getElementById("vehicleId").value = "";
        document.getElementById("description").value = "";
        document.getElementById("scheduledDate").value = "";
        document.getElementById("cost").value = "";
        document.getElementById("completed").value = "false";

        // Enable buttons again
        document.getElementById("updateBtn").disabled = true;
        document.getElementById("deleteBtn").disabled = true;
    }
});
</script>

</body>
<footer>
    <div class="footer">
        <p>Developed by: Annabel Cheng &copy; 2025</p>
        <p>25S CST8288 Section 013 Final Project</p>
    </div>
</footer>
</html>
