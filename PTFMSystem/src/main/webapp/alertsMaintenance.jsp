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

    </style>
</head>
<body>
    <div style="text-align:right; margin-bottom:10px;">
        <a href="dashboard.jsp" class="back-btn">Back to Dashboard</a>
    </div>
    <h2>FR05 - Predictive Maintenance Alerts</h2>
    
    <!-- Content Area -->
    <div id="alertsContent">Loading data...</div>

    <!-- Alert Popup -->
    <div id="alertPopup">
        <h4 style="margin-bottom:10px;">Alerts</h4>
        <div id="alertMessages">No alerts</div>
        <br>
        <button onclick="document.getElementById('alertPopup').style.display='none';">Close</button>
    </div>

<script>
function loadAlerts() {
    fetch('predictive-maintenance')
        .then(res => res.text())
        .then(html => {
            document.getElementById('alertsContent').innerHTML = html;

            // ✅ Check for hiddenAlerts div from servlet output
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
window.onload = loadAlerts;
</script>

</body>
<footer>
    <div class="footer" style="text-align:center; margin-top:20px;">
        <p>Developed by: Annabel Cheng &copy; 2025</p>
        <p>25S CST8288 Section 013 Final Project</p>
    </div>
</footer>
</html>
