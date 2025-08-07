<%--
  /**
   * FR05 - Predictive Maintenance Alerts Page
   * 
   * This JSP/HTML page is part of the CST8288 Final Project.
   * It displays real-time maintenance alerts for transit vehicles and allows
   * users to book maintenance tasks using AJAX and modal forms.
   * 
   * Key Features:
   * - Real-time alert fetching from predictive-maintenance servlet
   * - Modal form to schedule maintenance tasks
   * - Alert popup for managers
   * - Date validation with 2-day minimum and conflict checking
   *
   * @author Annabel Cheng
   */
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FR05 - Predictive Maintenance Alerts</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        body { font-family: Arial; margin: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 8px; border: 1px solid #ccc; text-align: center; }

        #alertPopup {
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
            width: 600px;
            font-size: 14px;
            text-align:left;
        }

        #bookingModal {
            display:none;
            position:fixed;
            top:50%;
            left:50%;
            transform:translate(-50%,-50%);
            background:white;
            padding:20px;
            border:2px solid #444;
            box-shadow:0 0 10px rgba(0,0,0,0.5);
            z-index:1000;
            width: 400px;
        }
    </style>
</head>
<body>

<div style="text-align:right; margin-bottom:10px;">
    <a href="dashboard.jsp" class="back-btn">Back to Dashboard</a>
</div>

<h2>FR05 - Predictive Maintenance Alerts</h2>
<div id="alertsContent">Loading data...</div>

<!-- Alert Popup -->
<div id="alertPopup">
    <h4 style="margin-bottom:10px;">Alerts</h4>
    <div id="alertMessages">No alerts</div>
    <br>
    <button onclick="document.getElementById('alertPopup').style.display='none';">Close</button>
</div>

<!-- Booking Modal -->
<div id="bookingModal">
    <h3>Book Maintenance</h3>
    <form id="bookingForm">
        <input type="hidden" name="vehicleId" id="vehicleId">
        <input type="hidden" name="alertId" id="alertId">

        <label for="taskType">Task:</label>
        <select name="taskType" id="taskType" required>
            <option value="">Select Task</option>
            <option value="Engine Check">Engine Check</option>
            <option value="Pantograph Check">Pantograph Check</option>
            <option value="Brake Inspection">Brake Inspection</option>
        </select><br><br>

        <label for="scheduleDate">Date:</label>
        <input type="date" name="scheduleDate" id="scheduleDate" required><br><br>

        <button type="submit">Confirm</button>
        <button type="button" onclick="closeBookingModal()">Cancel</button>
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
                messages.forEach(msg => { alertHtml += '<li>' + msg + '</li>'; });
                alertHtml += '</ul>';

                document.getElementById('alertMessages').innerHTML = alertHtml;
                document.getElementById('alertPopup').style.display = 'block';
            }
        })
        .catch(err => {
            document.getElementById('alertsContent').innerHTML = "<p style='color:red;'>Error loading alerts</p>";
            console.error(err);
        });
}

function openBookingModal(vehicleId, alertId) {
    document.getElementById('vehicleId').value = vehicleId;
    document.getElementById('alertId').value = alertId;

    const today = new Date();
    today.setDate(today.getDate() + 2);  // 2 days from now
    const minDate = today.toISOString().split('T')[0];

    document.getElementById('scheduleDate').min = minDate;
    document.getElementById('scheduleDate').value = minDate;

    disableBookedDates();
    document.getElementById('bookingModal').style.display = 'block';
}

function closeBookingModal() {
    document.getElementById('bookingModal').style.display = 'none';
}

function disableBookedDates() {
    fetch('check-booked-dates')
        .then(res => res.json())
        .then(bookedDates => {
            console.log("Booked dates received:", bookedDates);

            window.bookedDates = bookedDates; 

            const dateInput = document.getElementById('scheduleDate');

            dateInput.onchange = function () {
                const selected = this.value.trim();
                console.log("User selected date:", selected);
                console.log("Booked dates:", bookedDates);

                const isBooked = bookedDates.some(date => date.trim() === selected);       
                
                if (isBooked) {
                    alert("This date is already booked. Please choose another.");
                    this.value = '';  // Clear the invalid date
                }
            };
        })
        .catch(err => console.error("Error loading booked dates:", err));
}

// Handle form submit for maintenance booking
document.getElementById('bookingForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const formData = new FormData(this);
    const date = formData.get('scheduleDate');
    if (!date) {
        alert("Please select a valid date.");
        return;
    }

    if (window.bookedDates && window.bookedDates.includes(date.trim())) {
        alert("This date is already booked. Please choose another.");
        return;
    }

    formData.append("timeSlot", "09:00");

    console.log("Submitting form data:", Object.fromEntries(formData));

    fetch('book-maintenance', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams(formData)
    })
    .then(res => res.json())
    .then(data => {
        if (data.status === 'success') {
            alert("Maintenance booked successfully for " + data.scheduledDatetime);
            closeBookingModal();

            const button = document.querySelector("button[data-alert-id='" + formData.get('alertId') + "']");
            if (button) {
                button.parentElement.innerHTML = 
                  "<span><strong>Maintenance booked:</strong> " + data.scheduledDatetime + "</span>";
            }
        }
    })
    .catch(err => {
        console.error("Booking error:", err);
        alert("Error booking maintenance");
    });
});

// Delegate book button clicks
document.addEventListener('click', function(e) {
    if (e.target && e.target.dataset.action === 'book') {
        const vehicleId = e.target.dataset.vehicleId;
        const alertId = e.target.dataset.alertId;
        openBookingModal(vehicleId, alertId);
    }
});

window.onload = loadAlerts;
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
