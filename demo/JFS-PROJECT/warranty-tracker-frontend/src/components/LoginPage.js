import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

export default function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();

    // Basic validation
    if (!username || password.length < 6) {
      alert("Username required and password must be at least 6 characters.");
      return;
    }

    // Mock login success
    localStorage.setItem("user", username);
    navigate("/dashboard");
  };

  return (
    <div
      className="d-flex vh-100 justify-content-center align-items-center"
      style={{
        backgroundImage: "url('/images/login-bg.jpg')",
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
      }}
    >
      <div
        className="card shadow-lg p-4"
        style={{
          width: "400px",
          backdropFilter: "blur(6px)",
          backgroundColor: "rgba(255, 255, 255, 0.85)",
          borderRadius: "12px",
        }}
      >
        <h3 className="text-center mb-3">🔐 Warranty Tracker Login</h3>

        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <label className="form-label fw-semibold">Username</label>
            <input
              type="text"
              className="form-control"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter your username"
              autoFocus
            />
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Password</label>
            <input
              type="password"
              className="form-control"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter password (min 6 chars)"
            />
          </div>

          <button className="btn btn-primary w-100 mt-2" type="submit">
            Login
          </button>

          <button
            type="button"
            className="btn btn-link mt-2 w-100"
            onClick={() => navigate("/register")}
          >
            Create new account
          </button>
        </form>
      </div>
    </div>
  );
}
