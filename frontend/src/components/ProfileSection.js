// src/components/ProfileSection.js
import React from "react";
import { Card, Button } from "react-bootstrap";
 
const ProfileSection = ({ profile }) => {
  return (
<Card className="shadow-sm">
<Card.Body>
<Card.Title>{profile.employeeName}</Card.Title>
<Card.Text>
<strong>Email:</strong> {profile.email}
</Card.Text>
<Card.Text>
<strong>Department:</strong> {profile.department}
</Card.Text>
<Card.Text>
<strong>Role:</strong> {profile.role}
</Card.Text>
<Card.Text>
<strong>Contact:</strong> {profile.contactNumber}
</Card.Text>
{/* <Button variant="primary">Edit Profile</Button> */}
</Card.Body>
</Card>
  );
};
 
export default ProfileSection;