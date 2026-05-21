// src/components/EmployeeTable.js
import React, { useEffect, useState } from "react";
import axios from "axios";
import { Table, Button, FormControl, InputGroup, Spinner, Modal } from "react-bootstrap";
import { FaEdit, FaTrashAlt } from "react-icons/fa";
 
const EmployeeTable = ({ setSelectedEmployee }) => {
  const [employees, setEmployees] = useState([]);
  const [searchId, setSearchId] = useState("");
  const [loading, setLoading] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [employeeToDelete, setEmployeeToDelete] = useState(null);
  const [message, setMessage] = useState(null);
 
  useEffect(() => {
    const fetchEmployees = async () => {
      setLoading(true);
      try {
        const res = await axios.get("http://localhost:8762/api/admin/employees/all", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
        setEmployees(res.data.data);
      } catch (error) {
        console.error("Error fetching employees", error);
        setMessage({ text: "Error fetching employees", variant: "danger" });
      } finally {
        setLoading(false);
      }
    };
 
    fetchEmployees();
  }, []);
 
  const handleSearch = (e) => {
    setSearchId(e.target.value);
  };
 
  const handleDelete = async () => {
    if (!employeeToDelete || employeeToDelete.role === "ADMIN") {
      setMessage({ text: "Admin cannot be deleted", variant: "danger" });
      setShowDeleteModal(false);
      return;
    }
 
    try {
      await axios.delete(`http://localhost:8762/api/admin/employees/delete/${employeeToDelete.id}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
      setEmployees(employees.filter((emp) => emp.id !== employeeToDelete.id)); // Remove from list
      setMessage({ text: "Employee deleted successfully!", variant: "success" });
    } catch (error) {
      console.error("Error deleting employee", error);
      setMessage({ text: "Error deleting employee", variant: "danger" });
    } finally {
      setShowDeleteModal(false);
    }
  };
 
  const filteredEmployees = employees.filter(
    (employee) =>
      employee.id.toLowerCase().includes(searchId.toLowerCase()) ||
      employee.employeeName.toLowerCase().includes(searchId.toLowerCase())
  );
 
  return (
<div>
      {message && (
<div className={`alert alert-${message.variant}`} role="alert">
          {message.text}
</div>
      )}
 
      <InputGroup className="mb-3">
<InputGroup.Text>Search by Employee ID or Name</InputGroup.Text>
<FormControl
          value={searchId}
          onChange={handleSearch}
          placeholder="Enter employee ID or Name"
        />
</InputGroup>
 
      {loading ? (
<div className="text-center">
<Spinner animation="border" variant="primary" />
</div>
      ) : (
<Table striped bordered hover responsive>
<thead>
<tr>
<th>Employee Name</th>
<th>Email</th>
<th>Department</th>
<th>Role</th>
<th>Contact Number</th>
<th>Actions</th>
</tr>
</thead>
<tbody>
            {filteredEmployees.length === 0 ? (
<tr>
<td colSpan="6" className="text-center">
                  No employees found
</td>
</tr>
            ) : (
              filteredEmployees.map((employee) => (
<tr key={employee.id}>
<td>{employee.employeeName}</td>
<td>{employee.email}</td>
<td>{employee.department}</td>
<td>{employee.role}</td>
<td>{employee.contactNumber}</td>
<td>
<Button
                      variant="warning"
                      onClick={() => setSelectedEmployee(employee)}
>
<FaEdit /> Edit
</Button>{" "}
<Button
                      variant="danger"
                      onClick={() => {
                        setEmployeeToDelete(employee);
                        setShowDeleteModal(true);
                      }}
                      disabled={employee.role === "ADMIN"}
>
<FaTrashAlt /> Delete
</Button>
</td>
</tr>
              ))
            )}
</tbody>
</Table>
      )}
 
      <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
<Modal.Header closeButton>
<Modal.Title>Confirm Delete</Modal.Title>
</Modal.Header>
<Modal.Body>
          Are you sure you want to delete employee{" "}
<strong>{employeeToDelete?.employeeName}</strong>?
</Modal.Body>
<Modal.Footer>
<Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
            Cancel
</Button>
<Button variant="danger" onClick={handleDelete}>
            Confirm Delete
</Button>
</Modal.Footer>
</Modal>
</div>
  );
};
 
export default EmployeeTable;