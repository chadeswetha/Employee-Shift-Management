import React, { useState, useEffect } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { Form, Button, Card, Alert, Spinner } from "react-bootstrap";
 
const ApplyLeave = () => {
  const [formData, setFormData] = useState({
    startDate: "",
    endDate: "",
    reason: "",
    department: "",
  });
 
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [loadingDepartment, setLoadingDepartment] = useState(false);
  const [isFormValid, setIsFormValid] = useState(false);
  const [today, setToday] = useState("");
 
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      const decoded = jwtDecode(token);
      const employeeId = decoded.userId;
      fetchDepartment(employeeId);
    } else {
      setErrorMessage("User not authenticated.");
    }
 
    const todayDate = new Date();
    const year = todayDate.getFullYear();
    const month = String(todayDate.getMonth() + 1).padStart(2, '0');
    const day = String(todayDate.getDate()).padStart(2, '0');
    setToday(`${year}-${month}-${day}`);
 
  }, []);
 
  useEffect(() => {
    setIsFormValid(
      formData.startDate && formData.endDate && formData.reason && formData.department
    );
  }, [formData.startDate, formData.endDate, formData.reason, formData.department]);
 
  const handleChange = (e) => {
    setFormData((prevData) => ({
      ...prevData,
      [e.target.name]: e.target.value,
    }));
  };
 
  const fetchDepartment = async (employeeId) => {
    setLoadingDepartment(true);
    setErrorMessage("");
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(
        `http://localhost:8762/api/admin/employees/${employeeId}/department-name`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (response.data) {
        setFormData((prevData) => ({
          ...prevData,
          department: response.data,
        }));
      } else {
        console.error("Failed to fetch department name or data is empty.");
        setErrorMessage("Failed to fetch department name.");
      }
    } catch (error) {
      console.error("Error fetching department name:", error);
      setErrorMessage("Failed to fetch department name.");
    } finally {
      setLoadingDepartment(false);
    }
  };
 
  const validateDate = (date) => {
    const currentDate = new Date().toISOString().split("T")[0];
    return date >= currentDate;
  };
 
  const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage("");
        setSuccessMessage("");
   
        if (!isFormValid || !validateDate(formData.startDate) || !validateDate(formData.endDate)) {
          setErrorMessage("Please fill in all fields and ensure dates are valid.");
          return;
        }
   
        const token = localStorage.getItem("token");
        const decoded = jwtDecode(token);
        const employeeId = decoded.userId;
   
        try {
          await axios.post(
             "http://localhost:8764/api/leaves",
            {
               ...formData,
               employeeId: employeeId,
               status: "PENDING",
            },
           {
             headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
            }
          );
   
          setSuccessMessage("Leave applied successfully!");
          setErrorMessage("");
          setFormData({
            startDate: "",
            endDate: "",
            reason: "",
            department: formData.department, // retain department
          });
         } catch (error) {
           console.error("Error applying leave:", error);
            if (error.response && error.response.data) {
              setErrorMessage(error.response.data); // Set the backend error message
            } else {
             setErrorMessage("Failed to apply leave. Please try again."); // Fallback generic error
          }
          setSuccessMessage("");
        }
     };
 
  return (
    <Card className="m-4 shadow rounded">
      <Card.Header className="bg-primary text-white">
        <h4>Apply for Leave</h4>
      </Card.Header>
      <Card.Body>
        {loadingDepartment ? (
          <div className="text-center">
            <Spinner animation="border" variant="primary" />
          </div>
        ) : (
          <>
            {successMessage && <Alert variant="success">{successMessage}</Alert>}
            {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
 
            <Form onSubmit={handleSubmit}>
              <Form.Group controlId="startDate" className="mb-3">
                <Form.Label>Start Date</Form.Label>
                <Form.Control
                  type="date"
                  name="startDate"
                  value={formData.startDate}
                  onChange={handleChange} // Correctly referencing the handleChange function
                  required
                  min={today}
                />
              </Form.Group>
 
              <Form.Group controlId="endDate" className="mb-3">
                <Form.Label>End Date</Form.Label>
                <Form.Control
                  type="date"
                  name="endDate"
                  value={formData.endDate}
                  onChange={handleChange} // Correctly referencing the handleChange function
                  required
                  min={formData.startDate || today}
                />
              </Form.Group>
 
              <Form.Group controlId="reason" className="mb-3">
                <Form.Label>Reason</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={3}
                  name="reason"
                  value={formData.reason}
                  onChange={handleChange} // Correctly referencing the handleChange function
                  placeholder="Enter reason"
                  required
                />
              </Form.Group>
 
              <Form.Group controlId="department" className="mb-3">
                <Form.Label>Department</Form.Label>
                <Form.Control
                  type="text"
                  name="department"
                  value={formData.department}
                  onChange={handleChange} // Correctly referencing the handleChange function
                  readOnly
                  required
                />
              </Form.Group>
 
              <Button variant="success" type="submit" disabled={!isFormValid}>
                Submit Leave Request
              </Button>
            </Form>
          </>
        )}
      </Card.Body>
    </Card>
  );
};
 
export default ApplyLeave;
 