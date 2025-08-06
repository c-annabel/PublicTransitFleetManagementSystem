<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FR05 - Maintenance Alerts</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        body { font-family: Arial; margin: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 8px; border: 1px solid #ccc; text-align: center; }
        #alertPopup, #bookingModal {
            display:none;
            position:fixed;
            top:20%;
            left:50%;
            transform:translateX(-50%);
            background:#fff;
            border:2px solid #333;
            padding:20px;
            box-shadow:0 0 10px rgba(0,0,0,0.5);
            z-index:1000;
        }
        #bookingModal { width: 400px; text-align:left; }
    </style>
</head>
<body>
    <div style="text-align:right; margin-bottom:10px;">
        <a href="dashboard.jsp" class="back-btn">Back to Dashboard</a>
    </div>
<h2>FR05 - Maintenance Alerts</h2>
<div id="alertsContent">Loading...</div>

<!-- Popup -->
<div id="alertPopup">
    <h4>Maintenance Alerts</h4>
    <div id="alertMessages"></div>
    <br>
    <button onclick="document.getElementById('alertPopup').style.display='none';">Close</button>
</div>

<!-- Booking Modal -->
<div id="bookingModal">
    <h3>Book Maintenance</h3>
    <form id="bookingForm">
        <input type="hidden" id="alertId" name="alertId">
        <input type="hidden" id="vehicleId" name="vehicleId">
        <p><b>Vehicle:</b> <span id="vehicleDisplay"></span></p>
        <label>Task:</label><br>
        <select id="taskType" name="taskType" required>
            <option value="">-- Select Task --</option>
            <option value="Engine Check">Engine Check</option>
            <option value="Brake Inspection">Brake Inspection</option>
            <option value="Pantograph Check">Pantograph Check</option>
            <option value="Electrical System">Electrical System</option>
        </select><br><br>
        <label>Date:</label><br>
        <% String today = java.time.LocalDate.now().toString(); %>
        <input type="date" id="scheduleDate" name="scheduleDate" min="<%= today %>" required><br><br>
        <label>Time:</label><br>
        <select id="timeSlot" name="timeSlot" required>
            <option value="">-- Select Time --</option>
            <option value="09:00">09:00</option>
            <option value="10:00">10:00</option>
            <option value="11:00">11:00</option>
            <option value="13:00">13:00</option>
            <option value="14:00">14:00</option>
            <option value="15:00">15:00</option>
        </select><br><br>
        <button type="button" onclick="closeBookingModal()">Cancel</button>
        <button type="submit">Confirm</button>
    </form>
</div>

<script>
function loadAlerts() {
    fetch('predictive-maintenance')
        .then(res => res.text())
        .then(html => {
            document.getElementById('alertsContent').innerHTML = html;

            const hiddenDiv = document.getElementById('alertsContent').querySelector('#hiddenAlerts');
            if (hiddenDiv && hiddenDiv.textContent.trim() !== '') {
                const messages = hiddenDiv.textContent.split('|').filter(m => m.trim() !== '');
                let alertHtml = '<ul>';
                messages.forEach(msg => alertHtml += '<li>' + msg + '</li>');
                alertHtml += '</ul>';
                document.getElementById('alertMessages').innerHTML = alertHtml;
                document.getElementById('alertPopup').style.display = 'block';
            }
        })
        .catch(err => console.error("Error loading alerts:", err));
}
window.onload = loadAlerts;

function openBookingModal(alertId, vehicleId, vehicleDisplay) {
    document.getElementById('alertId').value = alertId;
    document.getElementById('vehicleId').value = vehicleId;
    document.getElementById('vehicleDisplay').innerText = vehicleDisplay;
    document.getElementById('bookingModal').style.display = 'block';
}
function closeBookingModal() {
    document.getElementById('bookingModal').style.display = 'none';
}

document.getElementById('scheduleDate').addEventListener('change', function() {
    const selectedDate = this.value;
    const today = new Date().toISOString().split('T')[0];
    const now = new Date();
    const currentHour = now.getHours();
    const timeSlotSelect = document.getElementById('timeSlot');
    for (let option of timeSlotSelect.options) {
        if (!option.value) continue;
        option.disabled = false;
        if (selectedDate === today && parseInt(option.value.split(':')[0]) <= currentHour) {
            option.disabled = true;
        }
    }
});

document.getElementById('bookingForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const formData = new FormData(this);
    fetch('book-maintenance', { method: 'POST', body: formData })
        .then(res => res.json())
        .then(data => {
            if (data.status === 'success') {
                alert("Maintenance booked successfully for " + data.scheduledDatetime);
                closeBookingModal();
                loadAlerts(); // Refresh table after booking
            } else {
                alert("Failed: " + data.message);
            }
        })
        .catch(err => alert("Error booking maintenance"));
});
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
