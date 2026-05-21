import React, { useState } from "react";
import axios from "axios";
import { Form, Button, Card, Alert, Spinner, InputGroup } from "react-bootstrap";

const AddEmployeeForm = () => {
  const [formData, setFormData] = useState({
    employeeName: "",
    email: "",
    password: "",
    department: "",
    role: "EMPLOYEE",
    contactNumber: "",
  });
  const [image, setImage] = useState(null);
  const [message, setMessage] = useState(null);
  const [variant, setVariant] = useState("success");
  const [loading, setLoading] = useState(false);
  const [nameError, setNameError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "employeeName") {
      setFormData((prev) => ({ ...prev, [name]: value })); // Allow all characters to be stored in the state

      // Check for numbers
      if (/[0-9]/.test(value)) {
        setNameError("Numbers are not allowed in the employee name.");
        return; // Exit early if numbers are found
      }

      // Check for special characters (excluding spaces)
      const specialCharRegex = /[^A-Za-z\s]/;
      if (specialCharRegex.test(value)) {
        setNameError("Special characters are not allowed in the employee name.");
        return; // Exit early if special characters are found
      }

      // If no numbers or special characters, clear the error
      setNameError("");
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };
  

  const handleImageChange = (e) => {
    setImage(e.target.files[0]);
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

    if (!formData.password) {
      setMessage("❌ Password is required");
      setVariant("danger");
      setLoading(false);
      return;
    }

    if (!formData.department) {
      setMessage("❌ Department is required");
      setVariant("danger");
      setLoading(false);
      return;
    }

    try {
      const form = new FormData();
      form.append("employeeName", formData.employeeName);
      form.append("email", formData.email);
      form.append("password", formData.password);
      form.append("department", formData.department);
      form.append("role", formData.role);
      form.append("contactNumber", formData.contactNumber);
      if (image) {
        form.append("image", image);
      }

      const res = await axios.post(
        "http://localhost:8762/api/admin/employees/add",
        form,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "multipart/form-data", // Important for file uploads
          },
        }
      );
      setVariant("success");
      setMessage("✅ " + res.data.message);
      setFormData({
        employeeName: "",
        email: "",
        password: "",
        department: "",
        role: "EMPLOYEE",
        contactNumber: "",
      });
      setImage(null); // Clear the image input
      setNameError(""); // Clear any name error on successful submission
    } catch (err) {
      if (err.response && err.response.data && err.response.data.message) {
        const errorMessage = err.response.data.message;
        if (errorMessage.includes("contact number")) {
          setMessage(`❌ The contact number '${formData.contactNumber}' is already in use.`);
        } else if (errorMessage.includes("email")) {
          setMessage(`❌ The email '${formData.email}' is already in use.`);
        } else {
          setMessage("❌ Failed to add employee: " + errorMessage);
        }
      } else {
        setMessage("❌ Failed to add employee");
      }
      setVariant("danger");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card className="mb-4 shadow-sm">
      <Card.Body>
        <Card.Title>Add New Employee/Manager</Card.Title>

        {message && <Alert variant={variant}>{message}</Alert>}

        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-2">
            <Form.Label>Name</Form.Label>
            <Form.Control
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

          <Form.Group className="mb-2">
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

          <Form.Group className="mb-2">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              placeholder="Enter password"
            />
          </Form.Group>

          <Form.Group className="mb-2">
            <Form.Label>Department</Form.Label>
            <Form.Control
              name="department"
              value={formData.department}
              onChange={handleChange}
              required
              placeholder="Enter department"
            />
          </Form.Group>

          <Form.Group className="mb-2">
            <Form.Label>Role</Form.Label>
            <Form.Select
              name="role"
              value={formData.role}
              onChange={handleChange}
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
                name="contactNumber"
                value={formData.contactNumber}
                onChange={handleChange}
                required
                placeholder="Enter contact number"
                isInvalid={
                  !validatePhoneNumber(formData.contactNumber) &&
                  formData.contactNumber !== ""
                }
              />
              <Form.Control.Feedback type="invalid">
                Please provide a valid 10-digit phone number.
              </Form.Control.Feedback>
            </InputGroup>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Image</Form.Label>
            <Form.Control
              type="file"
              name="image"
              onChange={handleImageChange}
            />
          </Form.Group>

          <Button variant="primary" type="submit" disabled={loading || !!nameError}>
            {loading ? <Spinner animation="border" size="sm" /> : "Add"}
          </Button>
        </Form>
      </Card.Body>
    </Card>
  );
};

export default AddEmployeeForm;