// src/pages/AdminDashboard.js
import React, { useEffect, useState } from "react"; // Import React, useEffect, and useState hooks from the 'react' library.
import axios from "axios"; // Import the axios library for making HTTP requests.
import Header from "../components/Header"; // Import the Header component.
import Footer from "../components/Footer"; // Import the Footer component.
import ProfileSection from "../components/ProfileSection"; // Import the ProfileSection component.
import EmployeeTable from "../components/EmployeeTable"; // Import the EmployeeTable component.
import AddEmployeeForm from "../components/AddEmployeeForm"; // Import the AddEmployeeForm component.
import UpdateEmployeeModal from "../components/UpdateEmployeeModal"; // Import the UpdateEmployeeModal component.
import { Container, Row, Col, Card, Button } from "react-bootstrap"; // Import specific components from the 'react-bootstrap' library for styling.

const AdminDashboard = () => {
    // Define the functional component AdminDashboard.
    const [adminProfile, setAdminProfile] = useState(null); // State to store the admin's profile data, initialized to null.
    const [selectedEmployee, setSelectedEmployee] = useState(null); // State to store the data of the employee selected for update, initialized to null.
    const adminId = JSON.parse(atob(localStorage.getItem("token").split(".")[1])).userId; // Extract the admin's ID from the JWT token stored in local storage.

    useEffect(() => {
        // useEffect hook to perform side effects, in this case, fetching the admin's profile.
        const fetchAdminProfile = async () => {
            // Define an asynchronous function to fetch the admin's profile data.
            try {
                // Try block to handle potential errors during the API call.
                const res = await axios.get(`http://localhost:8762/api/admin/employees/employee/${adminId}`, {
                    // Make a GET request to the specified API endpoint to fetch the admin's profile.
                    headers: {
                        // Set the authorization header with the JWT token.
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                });
                setAdminProfile(res.data.data); // Update the adminProfile state with the fetched data.
            } catch (error) {
                // Catch block to handle errors that occur during the API call.
                console.error("Error fetching admin profile", error); // Log the error to the console.
            }
        };

        fetchAdminProfile(); // Call the fetchAdminProfile function when the component mounts or when adminId changes.
    }, [adminId]); // Specify adminId as a dependency for the useEffect hook, so it re-runs if adminId changes.

    return (
        // Return the JSX to render the Admin Dashboard.
        <Container className="mt-4">
            {/* Apply a top margin to the container. */}
            {/* <Header /> */}
            {/* Render the Header component (commented out). */}
            <h2 className="text-center mb-4">Welcome, Admin</h2>
            {/* Display a welcome message centered at the top. */}

            <Row className="mb-4">
                {/* Create a row with a bottom margin for layout. */}
                <Col md={4}>
                    {/* Create a column that takes up 4 out of 12 columns on medium-sized screens and above. */}
                    {adminProfile && <ProfileSection profile={adminProfile} />}
                    {/* Conditionally render the ProfileSection component if adminProfile data is available, passing the profile data as a prop. */}
                </Col>
                <Col md={8}>
                    {/* Create a column that takes up 8 out of 12 columns on medium-sized screens and above. */}
                    <Card className="shadow-sm mb-3">
                        {/* Render a Card component with a shadow and bottom margin. */}
                        <Card.Body>
                            {/* Render the body of the Card. */}
                            <h5>Employee Management</h5>
                            {/* Display a heading for employee management. */}
                            <p>Manage your team by adding, updating, or viewing employee details.</p>
                            {/* Provide a brief description of the employee management section. */}
                            <AddEmployeeForm />
                            {/* Render the AddEmployeeForm component to allow adding new employees. */}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Row>
                {/* Create a new row for the employee list. */}
                <Col>
                    {/* Create a column that takes up all 12 columns. */}
                    <Card className="shadow-sm">
                        {/* Render a Card component with a shadow. */}
                        <Card.Body>
                            {/* Render the body of the Card. */}
                            <h5>Employees List</h5>
                            {/* Display a heading for the employees list. */}
                            <EmployeeTable setSelectedEmployee={setSelectedEmployee} />
                            {/* Render the EmployeeTable component to display the list of employees and pass the setSelectedEmployee function as a prop. */}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            {selectedEmployee && (
                // Conditionally render the UpdateEmployeeModal if an employee is selected for update.
                <UpdateEmployeeModal
                    employee={selectedEmployee}
                    // Pass the selected employee data as a prop to the modal.
                    onClose={() => setSelectedEmployee(null)}
                    // Pass a function to close the modal by resetting the selectedEmployee state to null.
                />
            )}

            <Footer />
            {/* Render the Footer component. */}
        </Container>
    );
};

export default AdminDashboard; // Export the AdminDashboard component to make it available for use in other parts of the application.