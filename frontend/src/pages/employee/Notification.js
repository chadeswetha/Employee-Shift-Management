// src/pages/employee/Notification.js
import React, { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
 
const Notification = () => {
  const [notifications, setNotifications] = useState([]);
  const [error, setError] = useState(null);
 
  const token = localStorage.getItem("token");
  let employeeId = null;
  try {
    const decoded = jwtDecode(token);
    employeeId = decoded.userId;
  } catch (err) {
    console.error("Invalid token:", err);
    setError("Authentication error");
  }
 
  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8766/api/notifications/${employeeId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setNotifications(response.data || []);
      } catch (err) {
        setError("Error fetching notifications");
      }
    };
 
    if (employeeId) fetchNotifications();
  }, [employeeId, token]);
 
  const markAsRead = async (notificationId) => {
    try {
      const response = await axios.put(
        `http://localhost:8766/api/notifications/${employeeId}/read`,
        { notificationId },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
 
      if (response.status === 200) {
        setNotifications((prev) =>
          prev.map((n) => (n.id === notificationId ? { ...n, read: true } : n))
        );
      }
    } catch (err) {
      setError("Error updating notification status");
    }
  };
 
  return (
<div className="container mt-5">
<h2 className="mb-4">Notifications</h2>
      {error && <div className="alert alert-danger">{error}</div>}
<table className="table table-bordered shadow-sm">
<thead className="table-light">
<tr>
<th>Message</th>
<th>Type</th>
<th>Timestamp</th>
<th>Status</th>
<th>Action</th>
</tr>
</thead>
<tbody>
          {notifications.length === 0 ? (
<tr>
<td colSpan="5" className="text-center text-muted">
                No notifications available
</td>
</tr>
          ) : (
            notifications.map((notification) => (
<tr key={notification.id}>
<td>{notification.message}</td>
<td>{notification.type}</td>
<td>{new Date(notification.timestamp).toLocaleString()}</td>
<td>{notification.read ? "Read" : "Unread"}</td>
<td>
                  {!notification.read && (
<button
                      className="btn btn-sm btn-primary"
                      onClick={() => markAsRead(notification.id)}
>
                      Mark as Read
</button>
                  )}
</td>
</tr>
            ))
          )}
</tbody>
</table>
</div>
  );
};
 
export default Notification;