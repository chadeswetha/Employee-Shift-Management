import React, { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';
import { Card, Spinner, Alert, Row, Col, Badge } from "react-bootstrap";

const EmployeeProfile = () => {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const token = localStorage.getItem("token");
        const decoded = jwtDecode(token);
        const employeeId = decoded.userId;
        const role = decoded.role;
        console.log(role);
        console.log(employeeId);
        const response = await axios.get(
          `http://localhost:8762/api/admin/employees/employee/${employeeId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        setProfile(response.data.data);
        console.log(response.data.data);
        setLoading(false);
      } catch (err) {
        setError("Failed to load profile.");
        setLoading(false);
      }
    };

    fetchProfile();
  }, []);

  if (loading) return <Spinner animation="border" variant="primary" />;
  if (error) return <Alert variant="danger">{error}</Alert>;


  return (
    <Card className="m-4 shadow rounded">
      <Card.Header className="bg-primary text-white">
        <h4>My Profile</h4>
      </Card.Header>
      <Card.Body>
        <Row className="mb-3">
          <Col md={4} className="text-center">
           
              <img
                src={`data:image/jpeg;base64,${profile.image}`}
                alt="Employee Profile"
                className="rounded-circle mb-2"
                style={{ width: '150px', height: '150px', objectFit: 'cover' }}
                onError={(e) => {
                  e.target.onerror = null;
                  e.target.src = "https://via.placeholder.com/150?text=No+Image";
                }}
              />
           
            {profile?.role && (
              <Badge pill bg={profile.role === 'MANAGER' ? 'success' : 'info'}>
                {profile.role}
              </Badge>
            )}
          </Col>
          <Col md={8}>
            <h5 className="mb-2">{profile?.employeeName}</h5>
            <p className="mb-1">
              <strong>Email:</strong> {profile?.email}
            </p>
            <p className="mb-1">
              <strong>Department:</strong> {profile?.department}
            </p>
            <p className="mb-0">
              <strong>Contact:</strong> {profile?.contactNumber}
            </p>
          </Col>
        </Row>

        
      </Card.Body>
    </Card>
  );
};

export default EmployeeProfile;