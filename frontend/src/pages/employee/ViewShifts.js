import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { Card, Button, Table, Spinner, Alert, Form } from 'react-bootstrap';
import { FaFilter } from 'react-icons/fa';

const ViewShifts = () => {
  const [shifts, setShifts] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [showPastShifts, setShowPastShifts] = useState(false);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');

  const token = localStorage.getItem("token");
  const decoded = jwtDecode(token);
  const employeeId = decoded.userId;

  useEffect(() => {
    const fetchShifts = async () => {
      if (employeeId) {
        setLoading(true);
        try {
          // Fetch shifts for the employee
          const response = await axios.get(`http://localhost:8763/api/shifts/employee/${employeeId}`);
          if (response.data.success && response.data.data) {
            const today = new Date();
            today.setHours(0, 0, 0, 0);

            let filtered = response.data.data;

            const inStartRange = startDate ? new Date(startDate) : null;
            const inEndRange = endDate ? new Date(endDate) : null;

            filtered = filtered.filter(shift => {
              const shiftDate = new Date(shift.shiftDate);
              const isFuture = shiftDate >= today;

              const isAfterStart = !inStartRange || shiftDate >= inStartRange;
              const isBeforeEnd = !inEndRange || shiftDate <= inEndRange;

              return (showPastShifts || isFuture) && isAfterStart && isBeforeEnd;
            });

            const sorted = filtered.sort((a, b) => new Date(a.shiftDate) - new Date(b.shiftDate));
            setShifts(sorted);
          } else {
            setError('Failed to fetch shifts');
          }
        } catch (err) {
          setError('Error fetching shifts');
        } finally {
          setLoading(false);
        }
      } else {
        setError('Employee ID not found in token');
      }
    };

    fetchShifts();
  }, [employeeId, showPastShifts, startDate, endDate]);

  const toggleShiftView = () => {
    setShowPastShifts(!showPastShifts);
  };


  return (
    <div className="container mt-5">
      <Card className="shadow-lg rounded-lg p-4">
        <Card.Header className="bg-primary text-white d-flex align-items-center justify-content-between rounded-top">
          <h3>Assigned Shifts</h3>
          <Button variant="outline-light" onClick={toggleShiftView}>
            {showPastShifts ? 'Show Upcoming Only' : 'Show All Shifts'}
          </Button>
        </Card.Header>
        <Card.Body>
          {/* Filter Section */}
          <div className="mb-4 d-flex justify-content-between align-items-center">
            <Form.Group className="d-flex align-items-center gap-2">
              <FaFilter size={20} />
              <label className="m-0">Filters</label>
              <Form.Check
                type="switch"
                id="togglePastShifts"
                label="Show Past Shifts"
                checked={showPastShifts}
                onChange={toggleShiftView}
              />
            </Form.Group>

            <div className="d-flex gap-3">
              <Form.Control
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
                placeholder="Start Date"
                className="w-auto"
              />
              <Form.Control
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
                placeholder="End Date"
                className="w-auto"
              />
            </div>
          </div>

          {/* Shifts Table */}
          {loading ? (
            <div className="d-flex justify-content-center">
              <Spinner animation="border" variant="primary" size="lg" />
            </div>
          ) : error ? (
            <Alert variant="danger">{error}</Alert>
          ) : (
            <Table bordered hover responsive>
              <thead>
                <tr>
                  <th>Shift Date</th>
                  <th>Shift Type</th>
                  <th>Start Time</th>
                  <th>End Time</th>
                  <th>Assigned By</th>
                </tr>
              </thead>
              <tbody>
                {shifts.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="text-center">
                      No shifts match the selected filters
                    </td>
                  </tr>
                ) : (
                  shifts.map((shift) => (
                    <tr key={shift.shiftId}>
                      <td>{new Date(shift.shiftDate).toLocaleDateString()}</td>
                      <td>{shift.shiftType}</td>
                      <td>{new Date(shift.startTime).toLocaleTimeString()}</td>
                      <td>{new Date(shift.endTime).toLocaleTimeString()}</td>
                      <td>{shift.assignedBy || 'N/A'}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </Table>
          )}
        </Card.Body>
      </Card>
    </div>
  );
};

export default ViewShifts;