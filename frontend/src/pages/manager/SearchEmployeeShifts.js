import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Form,
  Button,
  Alert,
  Spinner,
  Table,
  Row,
  Col,
  Pagination,
  Modal,
} from "react-bootstrap";

const SearchEmployeeShifts = () => {
  const [searchName, setSearchName] = useState("");
  const [searchEmail, setSearchEmail] = useState("");
  const [shifts, setShifts] = useState([]);
  const [filteredShifts, setFilteredShifts] = useState([]);
  const [employeeList, setEmployeeList] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [showFutureOnly, setShowFutureOnly] = useState(false);

  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;

  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [selectedShift, setSelectedShift] = useState(null);
  const [newShiftType, setNewShiftType] = useState("");

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      const res = await axios.get("http://localhost:8762/api/admin/employees/all");
      setEmployeeList(res.data.data);
    } catch (err) {
      console.error("Error fetching employees:", err);
    }
  };

  const handleSearch = async () => {
    let matchedEmployee = null;

    if (searchName) {
      matchedEmployee = employeeList.find((emp) =>
        emp.employeeName.toLowerCase() === searchName.toLowerCase()
      );
      if (!matchedEmployee) {
        setError("No employee found by that name.");
        setShifts([]);
        return;
      }
    } else if (searchEmail) {
      matchedEmployee = employeeList.find((emp) =>
        emp.email.toLowerCase() === searchEmail.toLowerCase()
      );
      if (!matchedEmployee) {
        setError("No employee found by that email.");
        setShifts([]);
        return;
      }
    } else {
      setError("Please enter either employee name or email to search.");
      setShifts([]);
      return;
    }

    try {
      setError("");
      setLoading(true);
      const res = await axios.get(`http://localhost:8763/api/shifts/employee/${matchedEmployee.id}`);
      const sortedShifts = res.data.data.sort((a, b) => new Date(a.startTime) - new Date(b.startTime));
      setShifts(sortedShifts);
      setCurrentPage(1);
    } catch (err) {
      setError("Error fetching shifts.");
      setShifts([]);
    } finally {
      setLoading(false);
    }
  };

  // Apply filters
  useEffect(() => {
    let filtered = [...shifts];

    if (startDate) {
      filtered = filtered.filter(
        (shift) => new Date(shift.startTime).toISOString().split("T")[0] >= startDate
      );
    }

    if (endDate) {
      filtered = filtered.filter(
        (shift) => new Date(shift.endTime).toISOString().split("T")[0] <= endDate
      );
    }

    if (showFutureOnly) {
      const today = new Date();
      filtered = filtered.filter((shift) => new Date(shift.startTime) > today);
    }

    // Sort filtered shifts by start date
    filtered.sort((a, b) => new Date(a.startTime) - new Date(b.startTime));
    setFilteredShifts(filtered);
  }, [shifts, startDate, endDate, showFutureOnly]);

  const indexOfLast = currentPage * itemsPerPage;
  const indexOfFirst = indexOfLast - itemsPerPage;
  const currentShifts = filteredShifts.slice(indexOfFirst, indexOfLast);
  const totalPages = Math.ceil(filteredShifts.length / itemsPerPage);
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  // Delete Shift
  const handleDelete = async (shiftId) => {
    if (!window.confirm("Are you sure you want to delete this shift?")) return;

    try {
      await axios.delete(`http://localhost:8763/api/shifts/${shiftId}`);
      setShifts(shifts.filter((s) => s.shiftId !== shiftId));
    } catch (err) {
      alert("Failed to delete shift.");
    }
  };

  const getDefaultShiftTimes = (shiftType) => {
    switch (shiftType) {
      case "MORNING":
        return { startTime: "09:00", endTime: "17:00" };
      case "EVENING":
        return { startTime: "14:00", endTime: "22:00" };
      case "NIGHT":
        return { startTime: "22:00", endTime: "06:00" };
      default:
        return { startTime: "", endTime: "" };
    }
  };

  // Open Update Modal
  const openUpdateModal = (shift) => {
    setSelectedShift(shift);
    setNewShiftType(shift.shiftType);
    setShowUpdateModal(true);
  };

  const handleShiftTypeChange = (e) => {
    const newType = e.target.value;
    setNewShiftType(newType);
    if (selectedShift) {
      const defaultTimes = getDefaultShiftTimes(newType);
      setSelectedShift({
        ...selectedShift,
        startTime: `${selectedShift.startTime.split("T")[0]}T${defaultTimes.startTime}:00`,
        endTime: `${selectedShift.endTime.split("T")[0]}T${defaultTimes.endTime}:00`,
      });
    }
  };

  // Update Shift
  const handleUpdate = async () => {
    if (!selectedShift) return;

    try {
      await axios.put(`http://localhost:8763/api/shifts/${selectedShift.shiftId}`, {
        employeeId: selectedShift.employeeId,
        shiftType: newShiftType,
        startTime: selectedShift.startTime,
        endTime: selectedShift.endTime,
      });
      alert("Shift updated successfully!");
      setShowUpdateModal(false);
      handleSearch(); // Refresh data
    } catch (err) {
      alert("Error updating shift.");
    }
  };

  return (
    <div className="container mt-4">
      <h4 className="mb-3">🔍 Search Employee Shifts</h4>

      <Row className="mb-3">
        <Col md={6}>
          <Form.Control
            type="text"
            placeholder="Enter employee name..."
            value={searchName}
            onChange={(e) => {
              setSearchName(e.target.value);
              setSearchEmail(""); // Clear email search when name changes
            }}
          />
          <Form.Text className="text-muted">Search by employee full name</Form.Text>
        </Col>
        <Col md={6}>
          <Form.Control
            type="email"
            placeholder="Enter employee email..."
            value={searchEmail}
            onChange={(e) => {
              setSearchEmail(e.target.value);
              setSearchName(""); // Clear name search when email changes
            }}
          />
          <Form.Text className="text-muted">Search by employee email address</Form.Text>
        </Col>
      </Row>

      <Button variant="dark" className="mb-3" onClick={handleSearch}>
        Search
      </Button>

      {shifts.length > 0 && (
        <Row className="mb-3">
          <Col md={4}>
            <Form.Label>Start Date</Form.Label>
            <Form.Control
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </Col>
          <Col md={4}>
            <Form.Label>End Date</Form.Label>
            <Form.Control
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </Col>
          <Col md={4} className="d-flex align-items-end">
            <Form.Check
              type="checkbox"
              label="Show only future shifts"
              checked={showFutureOnly}
              onChange={(e) => setShowFutureOnly(e.target.checked)}
            />
          </Col>
        </Row>
      )}

      {loading && <Spinner animation="border" variant="primary" />}
      {error && <Alert variant="danger">{error}</Alert>}

      {currentShifts.length > 0 && (
        <>
          <h5 className="mt-4">
            🗓️ Filtered Shifts for: <strong>{searchName || searchEmail}</strong>
          </h5>
          <Table striped bordered hover className="shadow-sm mt-3">
            <thead className="table-dark">
              <tr>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Start Time</th>
                <th>End Time</th>
                <th>Shift Type</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {currentShifts.map((shift) => (
                <tr key={shift.shiftId}>
                  <td>{shift.startTime.split("T")[0]}</td>
                  <td>{shift.endTime.split("T")[0]}</td>
                  <td>{shift.startTime.split("T")[1].slice(0, 5)}</td>
                  <td>{shift.endTime.split("T")[1].slice(0, 5)}</td>
                  <td>{shift.shiftType}</td>
                  <td>
                    <Button
                      variant="warning"
                      size="sm"
                      className="me-2"
                      onClick={() => openUpdateModal(shift)}
                    >
                      Update
                    </Button>
                    <Button
                      variant="danger"
                      size="sm"
                      onClick={() => handleDelete(shift.shiftId)}
                    >
                      Delete
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>

          {/* Pagination */}
          {totalPages > 1 && (
            <Pagination className="mt-3">
              {[...Array(totalPages).keys()].map((num) => (
                <Pagination.Item
                  key={num + 1}
                  active={num + 1 === currentPage}
                  onClick={() => paginate(num + 1)}
                >
                  {num + 1}
                </Pagination.Item>
              ))}
            </Pagination>
          )}
        </>
      )}

      {/* Update Modal */}
      <Modal show={showUpdateModal} onHide={() => setShowUpdateModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Update Shift</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label>Select New Shift Type</Form.Label>
            <Form.Select value={newShiftType} onChange={handleShiftTypeChange}>
              <option value="MORNING">MORNING</option>
              <option value="NIGHT">NIGHT</option>
              <option value="EVENING">EVENING</option>
            </Form.Select>
          </Form.Group>
          {selectedShift && (
            <>
              <Form.Group className="mb-3">
                <Form.Label>Start Time</Form.Label>
                <Form.Control
                  type="text"
                  value={selectedShift.startTime.split("T")[1].slice(0, 5)}
                  readOnly // Make it read-only as it's auto-updated
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>End Time</Form.Label>
                <Form.Control
                  type="text"
                  value={selectedShift.endTime.split("T")[1].slice(0, 5)}
                  readOnly // Make it read-only as it's auto-updated
                />
              </Form.Group>
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowUpdateModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleUpdate}>
            Update
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default SearchEmployeeShifts;