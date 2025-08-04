<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .container {
            width: 300px;
            margin: 80px auto;
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
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }
        .success {
            color: green;
        }
        input {
            width: 95%;
            padding: 8px;
            margin: 5px 0;
        }
        button {
            width: 100%;
            padding: 10px;
            margin-top: 10px;
        }
    </style>
</head>
<body>

    <h2>Login</h2>

    <!-- Display error message -->
    <%
        String error = request.getParameter("error");
        String success = request.getParameter("success");
        if ("1".equals(error)) {
    %>
        <div class="message">Invalid email or password. Please try again.</div>
    <% } else if ("1".equals(success)) { %>
        <div class="message success">Registration successful! Please login.</div>
    <% } %>

    <form action="login" method="post">
        <input type="email" name="email" placeholder="Email" required><br>
        <input type="password" name="password" placeholder="Password" required><br>
        <button type="submit">Login</button>
    </form>

    <p style="text-align:center; margin-top:10px;">
        <a href="register.jsp">Don't have an account? Register here</a>
    </p>

</body>
</html>
