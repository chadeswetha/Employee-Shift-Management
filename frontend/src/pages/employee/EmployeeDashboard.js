import React, { useState } from "react";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import Profile from "./EmployeeProfile";
import ApplyLeave from "./ApplyLeave";
import Attendance from "./Attendance";
import WeeklyGraph from "./WeeklyAttendance";
import ViewShifts from "./ViewShifts";
import Notifications from "./Notification";
import LeaveHistory from "./LeaveHistory";
import { Container, Button, Row, Col, Card } from "react-bootstrap";
import { FaUser, FaCalendarAlt, FaChartLine, FaListAlt, FaBell ,FaHistory} from "react-icons/fa";
 
const EmployeeDashboard = () => {
  const [activeTab, setActiveTab] = useState("profile");
 
  const renderContent = () => {
    switch (activeTab) {
      case "profile": return <Profile />;
      case "leave": return <ApplyLeave />;
      case "history": return <LeaveHistory />; 
      case "attendance": return <Attendance />;
      case "graph": return <WeeklyGraph />;
      case "shifts": return <ViewShifts />;
      case "notifications": return <Notifications />;

      default: return <Profile />;
    }
  };
 
  return (
<div className="d-flex flex-column min-vh-100 bg-light">
{/* <Header /> */}
<Container className="my-4">
<Row className="mb-4">
<Col md={3}>
<div className="bg-white p-3 rounded shadow-sm">
<h5 className="mb-4">Dashboard</h5>
<Button
                variant={activeTab === "profile" ? "primary" : "outline-primary"}
                className="d-flex align-items-center mb-3 w-100"
                onClick={() => setActiveTab("profile")}
>
<FaUser className="me-2" /> Profile
</Button>
<Button
                variant={activeTab === "leave" ? "primary" : "outline-primary"}
                className="d-flex align-items-center mb-3 w-100"
                onClick={() => setActiveTab("leave")}
>
<FaCalendarAlt className="me-2" /> Apply Leave
</Button>
<Button
                variant={activeTab === "attendance" ? "primary" : "outline-primary"}
                className="d-flex align-items-center mb-3 w-100"
                onClick={() => setActiveTab("attendance")}
>
<FaListAlt className="me-2" /> Attendance
</Button>
<Button
                variant={activeTab === "graph" ? "primary" : "outline-primary"}
                className="d-flex align-items-center mb-3 w-100"
                onClick={() => setActiveTab("graph")}
>
<FaChartLine className="me-2" /> Weekly Graph
</Button>
<Button
                variant={activeTab === "shifts" ? "primary" : "outline-primary"}
                className="d-flex align-items-center mb-3 w-100"
                onClick={() => setActiveTab("shifts")}
>
<FaListAlt className="me-2" /> View Shifts
</Button>
<Button
                variant={activeTab === "history" ? "primary" : "outline-primary"}
                className="d-flex align-items-center mb-3 w-100"
                onClick={() => setActiveTab("history")}
              >
                <FaHistory className="me-2" /> Leave History {/* New Button */}
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
 
export default EmployeeDashboard;