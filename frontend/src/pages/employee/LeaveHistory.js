
import React, { useState, useEffect } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { Table, Card, Spinner, Alert } from "react-bootstrap";
import moment from "moment"; // For formatting dates
 
const LeaveHistory = () => {
  const [leaveHistory, setLeaveHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
 
  useEffect(() => {
    const fetchLeaveHistory = async () => {
      setLoading(true);
      setError("");
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          setError("User not authenticated.");
          setLoading(false);
          return;
        }
        const decoded = jwtDecode(token);
        const employeeIdFromToken = decoded.userId; // Get employee ID from the token
 
        const response = await axios.get(
          `http://localhost:8764/api/leaves/history/${employeeIdFromToken}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        // Sort the leave history by startDate in descending order (newest first)
        const sortedHistory = response.data.data.sort((a, b) =>
          moment(b.startDate).valueOf() - moment(a.startDate).valueOf()
        );
        setLeaveHistory(sortedHistory);
      } catch (err) {
        console.error("Error fetching leave history:", err);
        setError("Failed to fetch leave history.");
      } finally {
        setLoading(false);
      }
    };
 
    fetchLeaveHistory();
  }, []);
 
  if (loading) {
    return (
      <Card className="m-4 shadow rounded">
        <Card.Body className="text-center">
          <Spinner animation="border" variant="primary" />
          <p className="mt-2">Loading Leave History...</p>
        </Card.Body>
      </Card>
    );
  }
 
  if (error) {
    return (
      <Card className="m-4 shadow rounded">
        <Card.Body>
          <Alert variant="danger">{error}</Alert>
        </Card.Body>
      </Card>
    );
  }
 
  return (
    <Card className="m-4 shadow rounded">
      <Card.Header className="bg-primary text-white">
        <h4>Leave History</h4>
      </Card.Header>
      <Card.Body>
        {leaveHistory.length === 0 ? (
          <p>No leave requests found.</p>
        ) : (
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th>Employee Name</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Reason</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {leaveHistory.map((leave) => (
                <tr key={leave.leaveId}>
                  <td>{leave.employeeName}</td>
                  <td>{moment(leave.startDate).format("DD-MM-YYYY")}</td>
                  <td>{moment(leave.endDate).format("DD-MM-YYYY")}</td>
                  <td>{leave.reason}</td>
                  <td>{leave.status}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        )}
      </Card.Body>
    </Card>
  );
};
 
export default LeaveHistory;
 