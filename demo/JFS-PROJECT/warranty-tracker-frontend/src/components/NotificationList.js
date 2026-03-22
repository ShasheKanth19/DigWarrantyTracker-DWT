import React, { useState, useEffect } from "react";
import { getUserNotifications, markNotificationRead, getAllUsers } from "../services/api";

export default function NotificationList() {
    const [notifications, setNotifications] = useState([]);
    const [users, setUsers] = useState([]);
    const [selectedUserId, setSelectedUserId] = useState("");

    useEffect(() => {
        loadUsers();
    }, []);

    useEffect(() => {
        if (selectedUserId) {
            loadNotifications(selectedUserId);
        } else {
            setNotifications([]);
        }
    }, [selectedUserId]);

    const loadUsers = async () => {
        try {
            const result = await getAllUsers();
            setUsers(result.data);
            // Auto-select first user if available for demo purposes
            if (result.data.length > 0) {
                setSelectedUserId(result.data[0].id);
            }
        } catch (error) {
            console.error("Error loading users:", error);
        }
    };

    const loadNotifications = async (userId) => {
        try {
            const result = await getUserNotifications(userId);
            setNotifications(result.data);
        } catch (error) {
            console.error("Error loading notifications:", error);
        }
    };

    const handleMarkRead = async (id) => {
        try {
            await markNotificationRead(id);
            loadNotifications(selectedUserId); // Refresh list
        } catch (error) {
            console.error("Error marking notification as read:", error);
        }
    };

    return (
        <div className="container mt-5">
            <div className="card shadow-lg p-4">
                <h2 className="text-center mb-4">Notifications</h2>

                <div className="mb-4">
                    <label className="form-label">Select User to View Notifications:</label>
                    <select
                        className="form-select"
                        value={selectedUserId}
                        onChange={(e) => setSelectedUserId(e.target.value)}
                    >
                        <option value="">Select User</option>
                        {users.map(user => (
                            <option key={user.id} value={user.id}>{user.name}</option>
                        ))}
                    </select>
                </div>

                {notifications.length === 0 ? (
                    <div className="alert alert-info text-center">No notifications found.</div>
                ) : (
                    <ul className="list-group">
                        {notifications.map((notif) => (
                            <li
                                key={notif.id}
                                className={`list-group-item d-flex justify-content-between align-items-center ${notif.read ? "bg-light" : "list-group-item-warning"
                                    }`}
                            >
                                <div>
                                    <p className="mb-1 fw-bold">{notif.message}</p>
                                    <small className="text-muted">{notif.date}</small>
                                </div>
                                {!notif.read && (
                                    <button
                                        className="btn btn-sm btn-outline-success"
                                        onClick={() => handleMarkRead(notif.id)}
                                    >
                                        Mark as Read
                                    </button>
                                )}
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
}
