// src/components/Dashboard.js
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

const DASHBOARD_BG = "/images/dashboard-bg.jpg"; // change filename here if needed

export default function Dashboard() {
  const navigate = useNavigate();

  useEffect(() => {
    // quick runtime check — will fail silently but logs helpful message in console
    fetch(DASHBOARD_BG, { method: "HEAD" })
      .then((res) => {
        if (!res.ok) {
          console.warn(`Dashboard background not found at ${DASHBOARD_BG} (status ${res.status})`);
        }
      })
      .catch((err) => {
        console.warn("Could not fetch dashboard background:", err);
      });
  }, []);

  return (
    <div
      style={{
        minHeight: "100vh",
        width: "100%",
        backgroundImage: `url('${DASHBOARD_BG}')`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        position: "relative",
        display: "flex",
        alignItems: "flex-start",
        paddingTop: "48px",
        paddingBottom: "48px",
      }}
    >
      {/* Overlay for contrast */}
      <div
        style={{
          position: "absolute",
          inset: 0,
          background:
            "linear-gradient(180deg, rgba(0,0,0,0.35) 0%, rgba(0,0,0,0.45) 100%)",
          zIndex: 0,
        }}
      />

      {/* Content container */}
      <div className="container" style={{ position: "relative", zIndex: 2 }}>
        <div className="text-center mb-4">
          <h1 className="display-5 fw-bold text-white">Warranty Tracker Dashboard</h1>
          <p className="text-light">
            Manage users, product warranties, and renewals — all in one place.
          </p>
        </div>

        <div className="row justify-content-center">
          {/* View Users */}
          <div className="col-md-3 mb-4">
            <div className="card shadow-lg border-0 rounded-4 h-100">
              <div className="card-body text-center">
                <i className="bi bi-people fs-1 text-primary"></i>
                <h5 className="card-title mt-3">View Users</h5>
                <p className="text-muted">See all registered users in the system.</p>
                <button className="btn btn-outline-primary" onClick={() => navigate("/users")}>
                  Go
                </button>
              </div>
            </div>
          </div>

          {/* Add New User */}
          <div className="col-md-3 mb-4">
            <div className="card shadow-lg border-0 rounded-4 h-100">
              <div className="card-body text-center">
                <i className="bi bi-person-plus fs-1 text-success"></i>
                <h5 className="card-title mt-3">Add New User</h5>
                <p className="text-muted">Register a new user to the system.</p>
                <button className="btn btn-outline-success" onClick={() => navigate("/add-user")}>
                  Add
                </button>
              </div>
            </div>
          </div>

          {/* Add Warranty */}
          <div className="col-md-3 mb-4">
            <div className="card shadow-lg border-0 rounded-4 h-100">
              <div className="card-body text-center">
                <i className="bi bi-shield-check fs-1 text-warning"></i>
                <h5 className="card-title mt-3">Add Warranty</h5>
                <p className="text-muted">Add product warranty or insurance details.</p>
                <button className="btn btn-outline-warning" onClick={() => navigate("/add-product")}>
                  Add
                </button>
              </div>
            </div>
          </div>

          {/* Notifications */}
          <div className="col-md-3 mb-4">
            <div className="card shadow-lg border-0 rounded-4 h-100">
              <div className="card-body text-center">
                <i className="bi bi-bell fs-1 text-danger"></i>
                <h5 className="card-title mt-3">Notifications</h5>
                <p className="text-muted">Check warranty expiry alerts.</p>
                <button className="btn btn-outline-danger" onClick={() => navigate("/notifications")}>
                  View
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
