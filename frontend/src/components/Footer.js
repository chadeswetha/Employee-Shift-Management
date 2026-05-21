import React, { useState } from "react";
import { Container, Row, Col } from "react-bootstrap";
import { EnvelopeAtFill, GeoAltFill, TelephoneFill, Linkedin, Github, Instagram } from "react-bootstrap-icons";
 
const Footer = () => {
  const [showAbout, setShowAbout] = useState(false); // Add this line
  const [showEmail, setShowEmail] = useState(false);
  const [showPhone, setShowPhone] = useState(false);
  const [showLocation, setShowLocation] = useState(false);
 
  return (
    <footer  className="footer bg-dark text-light pt-4 mt-5">
      <Container>
        <Row>
          {/* About */}
          <Col md={4} sm={12} className="mb-4">
            <h5 className="fw-bold mb-3" onClick={() => setShowAbout(!showAbout)} style={{ cursor: "pointer" }}>
              ShiftNex
            </h5>
            {showAbout && (
              <p>
                ShiftNex is a powerful employee shift and attendance management platform built for modern organizations.
                Manage shifts, leaves, and productivity with ease.
              </p>
            )}
          </Col>
 
          {/* Contact and Social Media */}
          <Col md={8} sm={12} className="mb-4">
            <div className="d-flex justify-content-between align-items-center">
              <p onClick={() => setShowEmail(!showEmail)} className="me-3" style={{ cursor: "pointer" }}>
                <EnvelopeAtFill className="me-2" />
                {showEmail ? "support@shiftnex.com" : "Email"}
              </p>
              <p onClick={() => setShowPhone(!showPhone)} className="me-3" style={{ cursor: "pointer" }}>
                <TelephoneFill className="me-2" />
                {showPhone ? "+91 98765 43210" : "Phone"}
              </p>
              <p onClick={() => setShowLocation(!showLocation)} className="me-3" style={{ cursor: "pointer" }}>
                <GeoAltFill className="me-2" />
                {showLocation ? "Chill Special Economic Zone, Keeranatham, CHIL SEZ Road, Saravanampatti, Coimbatore, Tamil Nadu 641035" : "Location"}
              </p>
              <a href="https://linkedin.com" target="_blank" rel="noopener noreferrer" className="text-light me-3">
                <Linkedin size={20} />
              </a>
              <a href="https://github.com" target="_blank" rel="noopener noreferrer" className="text-light me-3">
                <Github size={20} />
              </a>
              <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" className="text-light">
                <Instagram size={20} />
              </a>
            </div>
          </Col>
        </Row>
        <hr className="border-light" />
        <div className="text-center pb-3">
          <small>© 2025 ShiftNex. All rights reserved.</small>
        </div>
      </Container>
    </footer>
  );
};
 
export default Footer;
 
 