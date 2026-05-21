// src/components/Navbar.js
import React from "react";
import { Link } from "react-router-dom";

const Navbar = () => (
  <nav className="navbar navbar-expand-lg navbar-dark bg-dark px-4">
    <span className="navbar-brand">ShiftNex</span>
    <div className="collapse navbar-collapse">
      <ul className="navbar-nav ms-auto">
        <li className="nav-item"><Link className="nav-link" to="/">Home</Link></li>
        <li className="nav-item"><Link className="nav-link" to="#">Notifications</Link></li>
      </ul>
    </div>
  </nav>
);

export default Navbar;
