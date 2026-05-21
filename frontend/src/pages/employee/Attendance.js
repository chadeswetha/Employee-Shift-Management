import React, { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import { Button, Card, Container, Row, Col, Alert } from "react-bootstrap";

const Attendance = () => {
  const [attendance, setAttendance] = useState(null);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(true);

  const token = localStorage.getItem("token");
  const decoded = jwtDecode(token);
  const employeeId = decoded.userId;
  console.log(token)

  const headers = {
    Authorization: `Bearer ${token}`,
  };

  const fetchTodayAttendance = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8765/api/attendance/today/${employeeId}`,
        { headers }
      );
      setAttendance(response.data);
    } catch (error) {
      console.error("Attendance not found yet for today.");
      setAttendance(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTodayAttendance();
  }, []);

  const handleCheckIn = async () => {
    try {
      const response = await axios.post(
        `http://localhost:8765/api/attendance/checkin/${employeeId}`,
        {},
        { headers }
      );
      setAttendance(response.data);
      setMessage("Check-in successful.");
    } catch (error) {
      console.error("Check-in failed", error);
      setMessage("Check-in failed. Try again.");
    }
  };

  const handleCheckOut = async () => {
    try {
      const response = await axios.post(
        `http://localhost:8765/api/attendance/checkout/${employeeId}`,
        {},
        { headers }
      );
      setAttendance(response.data);
      setMessage("Check-out successful.");
    } catch (error) {
      console.error("Check-out failed", error);
      setMessage("Check-out failed. Try again.");
    }
  };

  const isCheckInEnabled = !attendance?.checkInTime;

  const isCheckOutEnabled = () => {
    return attendance?.checkInTime && !attendance?.checkOutTime;
  };

  return (
    <Container className="mt-5">
      <Row className="justify-content-center">
        <Col md={8}>
          <Card className="p-4 shadow-lg">
            <h3 className="text-center mb-4">Attendance</h3>

            {message && <Alert variant="info">{message}</Alert>}

            {loading ? (
              <p>Loading...</p>
            ) : (
              <>
                <div className="mb-3">
                  <strong>Date:</strong> {new Date().toLocaleDateString()}
                </div>
                <div className="mb-3">
                  <strong>Check-in Time:</strong>{" "}
                  {attendance?.checkInTime
                    ? new Date(attendance.checkInTime).toLocaleTimeString()
                    : "Not checked in yet"}
                </div>
                <div className="mb-3">
                  <strong>Check-out Time:</strong>{" "}
                  {attendance?.checkOutTime
                    ? new Date(attendance.checkOutTime).toLocaleTimeString()
                    : "Not checked out yet"}
                </div>
                <div className="d-flex gap-3">
                  <Button
                    variant="success"
                    disabled={!isCheckInEnabled}
                    onClick={handleCheckIn}
                  >
                    Check In
                  </Button>
                  <Button
                    variant="primary"
                    disabled={!isCheckOutEnabled()}
                    onClick={handleCheckOut}
                  >
                    Check Out
                  </Button>
                </div>
              </>
            )}
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Attendance;