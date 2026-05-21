import React, { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { Button, OverlayTrigger, Tooltip, Table, Form, Container, Row, Col } from "react-bootstrap";
 
const LeaveRequests = () => {
  const [leaveRequests, setLeaveRequests] = useState([]);
  const [filterText, setFilterText] = useState("");
 
  const token = localStorage.getItem("token");
  const decoded = jwtDecode(token);
  const managerId = decoded.userId;
 
  const fetchLeaveRequests = async () => {
    try {
      const response = await axios.get(`http://localhost:8764/api/leaves/pending/manager/${managerId}`);
      setLeaveRequests(response.data || []);
    } catch (error) {
      console.error("Error fetching leave requests:", error);
    }
  };
 
  useEffect(() => {
    fetchLeaveRequests();
  }, []);
 
  const handleAction = async (leave, action) => {
    const url = `http://localhost:8764/api/leaves/${leave.employeeId}/${action}`;
    try {
      await axios.put(url, leave);
      setLeaveRequests(prev => prev.filter(l => l.leaveId !== leave.leaveId));
    } catch (err) {
      console.error(`Error while ${action}ing leave:`, err);
    }
  };
 
  const calculateDuration = (start, end) => {
    const diff = (new Date(end) - new Date(start)) / (1000 * 60 * 60 * 24) + 1;
    return `${diff} day${diff > 1 ? "s" : ""}`;
  };
 
  const filteredRequests = leaveRequests.filter(req =>
    req.employeeName.toLowerCase().includes(filterText.toLowerCase())
  );
 
  return (
<Container className="mt-4">
<Row className="mb-3">
<Col>
<h3 className="mb-3">Pending Leave Requests</h3>
<Form.Control
            type="text"
            placeholder="Search by employee name..."
            value={filterText}
            onChange={(e) => setFilterText(e.target.value)}
          />
</Col>
</Row>
<Table striped bordered hover responsive className="table-sm shadow-sm">
<thead className="table-dark text-center">
<tr>
<th>Employee Name</th>
<th>Department</th>
<th>Start Date</th>
<th>End Date</th>
<th>Duration</th>
<th>Reason</th>
<th>Actions</th>
</tr>
</thead>
<tbody className="text-center align-middle">
          {filteredRequests.length > 0 ? (
            filteredRequests.map((leave) => (
<tr key={leave.leaveId}>
<td>{leave.employeeName}</td>
<td>{leave.department}</td>
<td>{leave.startDate}</td>
<td>{leave.endDate}</td>
<td>{calculateDuration(leave.startDate, leave.endDate)}</td>
<td>{leave.reason}</td>
<td className="d-flex justify-content-center gap-2">
<OverlayTrigger overlay={<Tooltip>Accept</Tooltip>}>
<Button variant="success" size="sm" onClick={() => handleAction(leave, "accept")}>
                      ✓
</Button>
</OverlayTrigger>
<OverlayTrigger overlay={<Tooltip>Reject</Tooltip>}>
<Button variant="danger" size="sm" onClick={() => handleAction(leave, "reject")}>
                      ✗
</Button>
</OverlayTrigger>
</td>
</tr>
            ))
          ) : (
<tr>
<td colSpan="7" className="text-muted text-center">No pending leave requests</td>
</tr>
          )}
</tbody>
</Table>
</Container>
  );
};
 
export default LeaveRequests;