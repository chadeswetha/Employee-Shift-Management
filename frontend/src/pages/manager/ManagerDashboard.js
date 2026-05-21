import React, { useState } from "react";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import ManagerProfile from "../employee/EmployeeProfile";
import ViewLeaveRequests from "./LeaveRequests";
import ViewDepartmentEmployees from "./ViewDepartmentEmployees";
import SearchEmployeeShifts from "./SearchEmployeeShifts";
import Notifications from "../employee/Notification";
import { Container, Button, Row, Col, Card } from "react-bootstrap";
import { FaUser, FaUsers, FaSearch, FaBell, FaCalendarCheck } from "react-icons/fa";
 
const ManagerDashboard = () => {
  const [activeTab, setActiveTab] = useState("profile");
 
  const renderContent = () => {
    switch (activeTab) {
      case "profile": return <ManagerProfile />;
      case "leaveRequests": return <ViewLeaveRequests />;
      case "department": return <ViewDepartmentEmployees />;
      case "searchShifts": return <SearchEmployeeShifts />;
      case "notifications": return <Notifications />;
      default: return <ManagerProfile />;
    }
  };
 
  return (
<div className="d-flex flex-column min-vh-100 bg-light">
{/* <Header /> */}
<Container className="my-4">
<Row className="mb-4">
<Col md={3}>
<div className="bg-white p-3 rounded shadow-sm">
<h5 className="mb-4">Manager Dashboard</h5>
<Button
                variant={activeTab === "profile" ? "primary" : "outline-primary"}
                className="d-flex align-items-center mb-3 w-100"
                onClick={() => setActiveTab("profile")}
>
<FaUser className="me-2" /> Profile
</Button>
<Button
                variant={activeTab === "leaveRequests" ? "primary" : "outline-primary"}
                className="d-flex align-items-center mb-3 w-100"
                onClick={() => setActiveTab("leaveRequests")}
>
<FaCalendarCheck className="me-2" /> Leave Requests
</Button>
<Button
                variant={activeTab === "department" ? "primary" : "outline-primary"}
                className="d-flex align-items-center mb-3 w-100"
                onClick={() => setActiveTab("department")}
>
<FaUsers className="me-2" /> Department Employees
</Button>
<Button
                variant={activeTab === "searchShifts" ? "primary" : "outline-primary"}
                className="d-flex align-items-center mb-3 w-100"
                onClick={() => setActiveTab("searchShifts")}
>
<FaSearch className="me-2" /> Search Shifts
</Button>
<Button
                variant={activeTab === "notifications" ? "primary" : "outline-primary"}
                className="d-flex align-items-center mb-3 w-100"
                onClick={() => setActiveTab("notifications")}
>
<FaBell className="me-2" /> Notifications
</Button>
</div>
</Col>
<Col md={9}>
<Card className="p-4 shadow-sm mb-4">
              {renderContent()}
</Card>
</Col>
</Row>
</Container>
<Footer />
</div>
  );
};
 
export default ManagerDashboard;