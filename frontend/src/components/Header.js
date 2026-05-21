// src/components/Header.js
import React, { useEffect, useState } from "react";
import {
    Navbar,
    Nav,
    Container,
    NavDropdown,
    Badge,
    Dropdown,
} from "react-bootstrap";
import {
    Bell,
    PersonCircle,
    BoxArrowRight,
} from "react-bootstrap-icons";
import axios from "axios";
import { Link } from "react-router-dom";

const Header = () => {
    const [hasUnreadNotifications, setHasUnreadNotifications] = useState(false);
    const [notifications, setNotifications] = useState([]);
    const [adminName, setAdminName] = useState("");

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) return;

        let employeeId = null;
        try {
            const decoded = JSON.parse(atob(token.split(".")[1]));
            employeeId = decoded.userId;
        } catch (err) {
            console.error("Invalid token:", err);
            return;
        }

        const fetchAdminProfile = async () => {
            try {
                const res = await axios.get(
                    `http://localhost:8762/api/admin/employees/employee/${employeeId}`,
                    {
                        headers: { Authorization: `Bearer ${token}` },
                    }
                );
                setAdminName(res.data.data.employeeName);
            } catch (error) {
                console.error("Error fetching admin profile", error);
            }
        };

        const fetchNotifications = async () => {
            try {
                const res = await axios.get(
                    `http://localhost:8766/api/notifications/${employeeId}`,
                    {
                        headers: { Authorization: `Bearer ${token}` },
                    }
                );
                const hasUnread = res.data.some((n) => n.read === false);
                setNotifications(res.data);
                setHasUnreadNotifications(hasUnread);
            } catch (error) {
                console.error("Error checking notifications", error);
            }
        };

        fetchAdminProfile();
        fetchNotifications();
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("token");
        window.location.href = "/";
    };

    return (
        <Navbar bg="light" variant="light" expand="lg" sticky="top" className="shadow-sm py-3">
            <Container>
                <Navbar.Brand href="/" className="d-flex align-items-center fw-bold">
                    <img
                        src="https://cdn-icons-png.flaticon.com/512/1055/1055646.png"
                        alt="ShiftNex Logo"
                        width="30"
                        height="30"
                        className="me-2"
                    />
                    ShiftNex
                </Navbar.Brand>

                <Navbar.Toggle />
                <Navbar.Collapse className="justify-content-end">
                    <Nav className="align-items-center gap-3">
                        <Dropdown align="end">
                            <Dropdown.Toggle
                                as={Nav.Link}
                                className="position-relative text-dark"
                            >
                                <Bell size={20} />
                                {hasUnreadNotifications && (
                                    <Badge
                                        bg="danger"
                                        pill
                                        className="position-absolute top-0 start-100 translate-middle p-2"
                                        style={{ borderRadius: "50%" }}
                                    />
                                )}
                            </Dropdown.Toggle>

                            <Dropdown.Menu style={{ minWidth: "300px", maxHeight: "400px", overflowY: "auto" }}>
                                <Dropdown.Header>Notifications</Dropdown.Header>
                                {notifications.length === 0 ? (
                                    <Dropdown.Item disabled>No notifications</Dropdown.Item>
                                ) : (
                                    notifications.slice(0, 5).map((notification) => (
                                        <Dropdown.Item
                                            key={notification.id}
                                            className={notification.read ? "" : "fw-bold"}
                                            as={Link}
                                            to="/notifications"
                                        >
                                            {notification.message}
                                        </Dropdown.Item>
                                    ))
                                )}
                                <Dropdown.Divider />
                                <Dropdown.Item as={Link} to="/notifications" className="text-primary text-center">
                                    View All
                                </Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>

                        <NavDropdown
                            title={<PersonCircle size={22} />}
                            id="profile-dropdown"
                            align="end"
                        >
                            <NavDropdown.Item disabled>
                                <strong>{adminName || "Admin"}</strong>
                            </NavDropdown.Item>
                            <NavDropdown.Divider />
                            <NavDropdown.Item onClick={handleLogout} className="text-danger">
                                <BoxArrowRight className="me-2" />
                                Logout
                            </NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default Header;