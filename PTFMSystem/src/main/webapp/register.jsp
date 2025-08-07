<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--
  /**
   * register.jsp - User Registration Page
   *
   * This JSP page is part of the CST8288 Final Project.
   * It allows new users (Managers or Operators) to register with a full name, 
   * email, password, and user type.
   *
   * Features:
   * - Form validation for all fields
   * - Displays feedback messages on success, failure, or duplicate email
   * - Redirects to login.jsp after successful registration
   *
   * @author Annabel Cheng
   */
--%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .container {
            width: 400px;
            height: 450px;
            margin: 100px auto;
            background: #fff;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }
        h2 {
            color: #003366;
            text-align: center;
        }
        input, select {
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
        <br><br>
        <button type="submit">Register</button>
    </form>
    <br>
    <p>Already have an account? <a href="login.jsp">Login here</a></p>
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
