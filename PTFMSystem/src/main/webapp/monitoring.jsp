<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FR04 - Monitoring</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        body { font-family: Arial; margin: 20px; background-color: #f9f9f9; }
        h2 { text-align: center; color: #003366; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; background: #fff; }
        th, td { padding: 10px; border: 1px solid #ccc; text-align: center; }
        th { background-color: #e8f1f9; }

        #alertPopup {
            display: none;
            position: fixed;
            top: 20%;
            left: 50%;
            transform: translateX(-50%);
            background: #fff;
            border: 2px solid #333;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0,0,0,0.5);
            z-index: 1000;
            width: 600px;
            font-size: 14px;
            text-align: left;
        }

        .footer {
            text-align: center;
            font-size: 14px;
            margin-top: 40px;
            color: #666;
        }
    </style>
</head>
<body>
    <div style="text-align:right; margin-bottom:10px;">
        <a href="dashboard.jsp" class="back-btn">Back to Dashboard</a>
    </div>

    <h2>FR04 - Real-Time Fuel & Energy Monitoring</h2>
    <div id="monitoringContent">Loading fuel & energy data...</div>

    <!-- Alert Popup -->
    <div id="alertPopup">
        <h4 style="margin-bottom:10px;">Alerts</h4>
        <div id="alertMessages">No alerts</div>
        <br>
        <button onclick="document.getElementById('alertPopup').style.display='none';">Close</button>
    </div>

<script>
function loadMonitoring() {
    fetch('consumption')
        .then(res => res.text())
        .then(html => {
            document.getElementById('monitoringContent').innerHTML = html;

            // Check for hidden alerts
            const hiddenDiv = document.getElementById('monitoringContent').querySelector('#hiddenAlerts');
            if (hiddenDiv && hiddenDiv.textContent.trim() !== '') {
                const messages = hiddenDiv.textContent.split('|').filter(m => m.trim() !== '');
                let alertHtml = '<ul>';
                messages.forEach(msg => { alertHtml += '<li>' + msg + '</li>'; });
                alertHtml += '</ul>';

                document.getElementById('alertMessages').innerHTML = alertHtml;
                document.getElementById('alertPopup').style.display = 'block';
            }
        });
}
window.onload = loadMonitoring;
</script>

</body>
<footer>
    <div class="footer">
        <p>Developed by: Annabel Cheng &copy; 2025</p>
        <p>25S CST8288 Section 013 Final Project</p>
    </div>
</footer>
</html>
