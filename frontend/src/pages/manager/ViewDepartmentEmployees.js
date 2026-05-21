import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import {
  Table,
  Alert,
  Spinner,
  Form,
  Modal,
  Button,
} from 'react-bootstrap';
 
const ViewDepartmentEmployees = () => {
  const [employees, setEmployees] = useState([]);
  const [filteredEmployees, setFilteredEmployees] = useState([]);
  const [search, setSearch] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [showModal, setShowModal] = useState(false);
 
  const token = localStorage.getItem('token');
  const decoded = jwtDecode(token);
  const managerId = decoded.userId;
 
  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8762/api/admin/employees/department/manager/${managerId}`
        );
        setEmployees(response.data);
        setFilteredEmployees(response.data);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch employees under your department.');
        setLoading(false);
      }
    };
 
    fetchEmployees();
  }, [managerId]);
 
  useEffect(() => {
    const filtered = employees.filter((emp) =>
      emp.employeeName.toLowerCase().includes(search.toLowerCase())
    );
    setFilteredEmployees(filtered);
  }, [search, employees]);
 
  const handleRowClick = (employee) => {
    setSelectedEmployee(employee);
    setShowModal(true);
  };
 
  if (loading) return <div className="text-center my-4"><Spinner animation="border" /></div>;
  if (error) return <Alert variant="danger">{error}</Alert>;
 
  return (
<div className="container mt-4">
<h3 className="mb-4">Employees in Your Department</h3>
 
      <Form.Control
        type="text"
        placeholder="Search by employee name..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="mb-3 shadow-sm"
      />
 
      <Table striped bordered hover responsive className="shadow">
<thead className="table-dark">
<tr>
<th>Employee Name</th>
<th>Email</th>
<th>Department</th>
<th>Role</th>
<th>Contact Number</th>
</tr>
</thead>
<tbody>
          {filteredEmployees.length === 0 ? (
<tr>
<td colSpan="5" className="text-center">No employees found.</td>
</tr>
          ) : (
            filteredEmployees.map((emp) => (
<tr
                key={emp.id}
                onClick={() => handleRowClick(emp)}
                style={{ cursor: 'pointer' }}
>
<td>{emp.employeeName}</td>
<td>{emp.email}</td>
<td>{emp.department}</td>
<td>{emp.role}</td>
<td>{emp.contactNumber}</td>
</tr>
            ))
          )}
</tbody>
</Table>
 
      {/* Profile Modal */}
<Modal show={showModal} onHide={() => setShowModal(false)} centered>
<Modal.Header closeButton>
<Modal.Title>Employee Profile</Modal.Title>
</Modal.Header>
<Modal.Body>
          {selectedEmployee && (
<div>
<p><strong>Name:</strong> {selectedEmployee.employeeName}</p>
<p><strong>Email:</strong> {selectedEmployee.email}</p>
<p><strong>Contact:</strong> {selectedEmployee.contactNumber}</p>
<p><strong>Role:</strong> {selectedEmployee.role}</p>
<p><strong>Department:</strong> {selectedEmployee.department}</p>
</div>
          )}
</Modal.Body>
<Modal.Footer>
<Button variant="secondary" onClick={() => setShowModal(false)}>
            Close
</Button>
</Modal.Footer>
</Modal>
</div>
  );
};
 
export default ViewDepartmentEmployees;