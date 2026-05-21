import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../services/authService";
import "./Auth.css"; // Make sure to use the same CSS file

const RegisterPage = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    role: "ADMIN",
    department: "",
    contact: "",
    image: null, // Initialize image as null
  });
  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleImageChange = (e) => {
    setForm({ ...form, image: e.target.files[0] }); // Store the selected file object
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    const formData = new FormData(); // Create FormData to handle file uploads
    for (const key in form) {
      formData.append(key, form[key]);
    }

    try {
      const res = await register(formData); // Send FormData instead of JSON
      setMessage(res.data.message);
      setTimeout(() => navigate("/"), 1500); // redirect to login
    } catch (err) {
      setMessage("Registration failed. Please try again.");
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card shadow-lg">
        <div className="brand-header mb-4">
          <span className="brand-circle">S</span>
          <h2 className="ms-2">ShiftNex</h2>
        </div>
        <h5 className="mb-4 text-muted text-center">Create your account</h5>

        {message && <div className="alert alert-info">{message}</div>}
        <form onSubmit={handleRegister}>
          {["name", "email", "password", "department", "contact"].map((field) => (
            <div className="form-group mb-3" key={field}>
              <label>{field.charAt(0).toUpperCase() + field.slice(1)}</label>
              <input
                type={field === "password" ? "password" : "text"}
                name={field}
                className="form-control custom-input"
                value={form[field]}
                onChange={handleChange}
                placeholder={`Enter ${field}`}
                required
              />
            </div>
          ))}
          <div className="form-group mb-3">
            <label>Image</label>
            <input
              type="file"
              name="image"
              className="form-control custom-input"
              onChange={handleImageChange}
              accept="image/*" // Optional: Specify accepted file types
            />
          </div>
          <div className="form-group mb-3">
            <label>Role</label>
            <select
              name="role"
              className="form-select custom-input"
              value={form.role}
              onChange={handleChange}
            >
              <option value="ADMIN">Admin</option>
              <option value="MANAGER">Manager</option>
              <option value="EMPLOYEE">Employee</option>
            </select>
          </div>
          <button type="submit" className="btn btn-success w-100 fw-bold shadow-sm">
            Register
          </button>
        </form>
      </div>
    </div>
  );
};

export default RegisterPage;