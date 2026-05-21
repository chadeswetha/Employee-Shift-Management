// src/App.js
import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from "react-router-dom";
import LoginPage from "./pages/LoginPage"; // Import the LoginPage component
import RegisterPage from "./pages/RegisterPage"; // Import the RegisterPage component
import EmployeeDashboard from "./pages/employee/EmployeeDashboard"; // Import the EmployeeDashboard component
import ManagerDashboard from "./pages/manager/ManagerDashboard"; // Import the ManagerDashboard component
import AdminDashboard from "./pages/AdminDashboard"; // Import the AdminDashboard component
import ProtectedRoute from "./components/ProtectedRoute"; // Import the ProtectedRoute component
import Header from "./components/Header"; // Import the Header component
import Notification from "./pages/employee/Notification"; // Import the Notification component

function App() {
    return (
        <Router>
            <AppContent />
        </Router>
    );
}

function AppContent() {
    const location = useLocation(); // Get the current location using useLocation hook
    const showHeader = !["/", "/register"].includes(location.pathname); // Determine if the header should be shown based on the current path

    return (
        <>
            {showHeader && <Header />} {/* Render the Header component if showHeader is true */}
            <Routes>
                <Route path="/" element={<LoginPage />} /> {/* Define the route for the login page */}
                <Route path="/register" element={<RegisterPage />} /> {/* Define the route for the register page */}

                <Route
                    path="/employee-dashboard"
                    element={
                        <ProtectedRoute allowedRoles={["EMPLOYEE"]}> {/* Protect the employee dashboard route for users with the "EMPLOYEE" role */}
                            <EmployeeDashboard /> {/* Render the EmployeeDashboard component if the user has the required role */}
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/manager-dashboard"
                    element={
                        <ProtectedRoute allowedRoles={["MANAGER"]}> {/* Protect the manager dashboard route for users with the "MANAGER" role */}
                            <ManagerDashboard /> {/* Render the ManagerDashboard component if the user has the required role */}
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/admin-dashboard"
                    element={
                        <ProtectedRoute allowedRoles={["ADMIN"]}> {/* Protect the admin dashboard route for users with the "ADMIN" role */}
                            <AdminDashboard /> {/* Render the AdminDashboard component if the user has the required role */}
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/notifications"
                    element={
                        <ProtectedRoute allowedRoles={["EMPLOYEE", "MANAGER", "ADMIN"]}> {/* Protect the notifications route for users with "EMPLOYEE", "MANAGER", or "ADMIN" roles */}
                            <Notification /> {/* Render the Notification component if the user has the required role */}
                        </ProtectedRoute>
                    }
                />

                <Route path="*" element={<Navigate to="/" />} /> {/* Redirect any unknown paths to the login page */}
            </Routes>
        </>
    );
}

export default App;