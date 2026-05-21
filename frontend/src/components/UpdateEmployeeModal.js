import React, { useState, useEffect } from "react";
import axios from "axios";
import { Modal, Button, Form, Alert, Spinner, InputGroup } from "react-bootstrap";

const UpdateEmployeeModal = ({ employee, onClose }) => {
  const [formData, setFormData] = useState({
    id: "",
    employeeName: "",
    email: "",
    password: "",
    department: "",
    role: "EMPLOYEE",
    contactNumber: "",
  });

  const [message, setMessage] = useState(null);
  const [variant, setVariant] = useState("success");
  const [loading, setLoading] = useState(false);
  const [nameError, setNameError] = useState("");

  useEffect(() => {
    if (employee) {
      setFormData({
        id: employee.id,
        employeeName: employee.employeeName,
        email: employee.email,
        password: "", // Do not pre-fill password for security
        department: employee.department,
        role: employee.role,
        contactNumber: employee.contactNumber,
      });
      setNameError(""); // Clear any previous name error when employee data loads
    }
  }, [employee]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "employeeName") {
      setFormData((prev) => ({ ...prev, [name]: value })); // Allow all characters in the state

      // Check for numbers
      if (/[0-9]/.test(value)) {
        setNameError("Numbers are not allowed in the employee name.");
        return;
      }

      // Check for special characters (excluding spaces)
      const specialCharRegex = /[^A-Za-z\s]/;
      if (specialCharRegex.test(value)) {
        setNameError("Special characters are not allowed in the employee name.");
        return;
      }

      setNameError(""); // Clear error if no numbers or special characters
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };

  const validateEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  const validatePhoneNumber = (phone) => /^[0-9]{10}$/.test(phone);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    // Basic validation
    if (!formData.employeeName.trim()) {
      setMessage("❌ Employee name is required");
      setVariant("danger");
      setLoading(false);
      return;
    }

    if (nameError) {
      setMessage(`❌ ${nameError}`);
      setVariant("danger");
      setLoading(false);
      return;
    }

    if (!validateEmail(formData.email)) {
      setMessage("❌ Invalid email format");
      setVariant("danger");
      setLoading(false);
      return;
    }

    if (!validatePhoneNumber(formData.contactNumber)) {
      setMessage("❌ Contact number must be 10 digits");
      setVariant("danger");
      setLoading(false);
      return;
    }

    try {
      const res = await axios.put(
        `http://localhost:8762/api/admin/employees/update/${formData.id}`,
        { ...formData, employeeName: formData.employeeName.replace(/[^A-Za-z\s]/g, "") }, // Send cleaned name
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      setVariant("success");
      setMessage("✅ Employee updated successfully!");
      onClose(); // Close modal after success
    } catch (err) {
      if (err.response && err.response.data && err.response.data.message) {
        const errorMessage = err.response.data.message;
        if (errorMessage.includes("contact number")) {
          setMessage(`❌ The contact number '${formData.contactNumber}' is already in use.`);
        } else if (errorMessage.includes("email")) {
          setMessage(`❌ The email '${formData.email}' is already in use.`);
        } else {
          setMessage("❌ Failed to update employee: " + errorMessage);
        }
      } else {
        setMessage("❌ Failed to update employee");
      }
      setVariant("danger");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={true} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Update Employee</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {message && <Alert variant={variant}>{message}</Alert>}

        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Name</Form.Label>
            <Form.Control
              type="text"
              name="employeeName"
              value={formData.employeeName}
              onChange={handleChange}
              required
              placeholder="Enter employee's name"
              isInvalid={!!nameError}
            />
            <Form.Control.Feedback type="invalid">
              {nameError}
            </Form.Control.Feedback>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Email</Form.Label>
            <Form.Control
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              placeholder="Enter employee's email"
              isInvalid={!validateEmail(formData.email) && formData.email !== ""}
            />
            <Form.Control.Feedback type="invalid">
              Please provide a valid email address.
            </Form.Control.Feedback>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Enter new password (leave empty to keep existing)"
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Department</Form.Label>
            <Form.Control
              type="text"
              name="department"
              value={formData.department}
              onChange={handleChange}
              required
              placeholder="Enter department"
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Role</Form.Label>
            <Form.Select
              name="role"
              value={formData.role}
              onChange={handleChange}
              required
            >
              <option value="EMPLOYEE">EMPLOYEE</option>
              <option value="MANAGER">MANAGER</option>
            </Form.Select>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Contact Number</Form.Label>
            <InputGroup>
              <InputGroup.Text>+91</InputGroup.Text>
              <Form.Control
                type="text"
                name="contactNumber"
                value={formData.contactNumber}
                onChange={handleChange}
                required
                placeholder="Enter contact number"
                isInvalid={!validatePhoneNumber(formData.contactNumber) && formData.contactNumber !== ""}
              />
              <Form.Control.Feedback type="invalid">
                Please provide a valid 10-digit phone number.
              </Form.Control.Feedback>
            </InputGroup>
          </Form.Group>

          <Button variant="primary" type="submit" disabled={loading || !!nameError}>
            {loading ? <Spinner animation="border" size="sm" /> : "Update"}
          </Button>
        </Form>
      </Modal.Body>
    </Modal>
  );
};

export default UpdateEmployeeModal;