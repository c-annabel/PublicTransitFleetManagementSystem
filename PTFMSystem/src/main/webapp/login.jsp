<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .container {
            width: 400px;
            height: 450px;
            margin: 100px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 6px;
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
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        button {
            width: 100%;
            padding: 12px;
            margin-top: 10px;
            background: #007bff;
            color: #fff;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }
        p {
            text-align: center;
            margin-top: 10px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Login</h2><br>

    <!-- Display error or success messages -->
    <%
        String error = request.getParameter("error");
        String success = request.getParameter("success");
        if ("1".equals(error)) {
    %>
        <div class="message">Invalid email or password. Please try again.</div>
    <% } else if ("unauthorized".equals(error)) { %>
        <div class="message">Please log in to access the application.</div>
    <% } else if ("sessionExpired".equals(error)) { %>
        <div class="message">Your session has expired. Please log in again.</div>
    <% } else if ("1".equals(success)) { %>
        <div class="message success">Registration successful! Please login.</div>
    <% } %>

    <!-- Login form -->
    <form action="login" method="post">
        <input type="email" name="email" placeholder="Email" required><br>
        <input type="password" name="password" placeholder="Password" required><br><br><br>
        <button type="submit">Login</button>
    </form>
    <br><br>
    <p>
        Don't have an account? <a href="register.jsp">Register here</a>
    </p>
</div>

</body>
</html>
