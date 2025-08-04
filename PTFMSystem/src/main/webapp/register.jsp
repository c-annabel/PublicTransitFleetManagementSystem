<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .container {
            width: 350px;
            margin: 60px auto;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h2 {
            color: #003366;
            text-align: center;
            margin-bottom: 20px;
        }
        input, select {
            width: 100%;
            padding: 10px;
            margin: 8px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
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
        button:hover {
            background: #0056b3;
        }
        .message {
            text-align: center;
            margin-bottom: 10px;
            color: red;
            font-weight: bold;
        }
        .success {
            color: green;
        }
        p {
            text-align: center;
            margin-top: 10px;
        }
        a {
            color: #007bff;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Create Account</h2>

    <!-- Display error or success messages -->
    <%
        String error = request.getParameter("error");
        String success = request.getParameter("success");
        if ("1".equals(error)) {
    %>
        <div class="message">Registration failed. Please try again.</div>
    <% } else if ("1".equals(success)) { %>
        <div class="message success">Registration successful! Please login.</div>
    <% } else if ("duplicate".equals(error)) { %>
        <div class="message">Email already exists. Please use another email.</div>
    <% } %>

    <form action="register" method="post">
        <input type="text" name="name" placeholder="Full Name" required>
        <input type="email" name="email" placeholder="Email Address" required>
        <input type="password" name="password" placeholder="Password" required minlength="6">
        
        <select name="userType" required>
            <option value="">Select User Type</option>
            <option value="Manager">Manager</option>
            <option value="Operator">Operator</option>
        </select>
        
        <button type="submit">Register</button>
    </form>

    <p><a href="login.jsp">Already have an account? Login here</a></p>
</div>
</body>
</html>
