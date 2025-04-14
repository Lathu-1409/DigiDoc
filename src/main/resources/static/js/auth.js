// Toggle between login and register forms
document.getElementById("createAccountLink").addEventListener("click", () => {
    document.getElementById("login-section").classList.add("hidden");
    document.getElementById("register-section").classList.remove("hidden");
  });
  
  document.getElementById("backToLoginLink").addEventListener("click", () => {
    document.getElementById("register-section").classList.add("hidden");
    document.getElementById("login-section").classList.remove("hidden");
  });
  
  // Handle user login
  document.getElementById("loginForm").addEventListener("submit", async function (event) {
    event.preventDefault();
  
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
  
    try {
      const response = await fetch("/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
      });
  
      if (response.ok) {
        const user = await response.json();
        alert(`Welcome, ${user.name}!`);
        // Redirect to dashboard or any other page
        window.location.href = "dashboard.html";
      } else {
        const error = await response.text();
        alert("Login failed: " + error);
      }
    } catch (err) {
      alert("An error occurred: " + err.message);
    }
  });
  
  // Handle user registration
  document.getElementById("registerForm").addEventListener("submit", async function (event) {
    event.preventDefault();
  
    const username = document.getElementById("registerName").value;
    const email = document.getElementById("registerEmail").value;
    const password = document.getElementById("registerPassword").value;
  
    try {
      const response = await fetch("/users/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, email, password })
      });
  
      if (response.ok) {
        const registeredUser = await response.json();
        alert("Registration successful! Please login.");
        // Redirect to login form
        document.getElementById("register-section").classList.add("hidden");
        document.getElementById("login-section").classList.remove("hidden");
      } else {
        const error = await response.text();
        alert("Registration failed: " + error);
      }
    } catch (err) {
      alert("An error occurred: " + err.message);
    }
  });