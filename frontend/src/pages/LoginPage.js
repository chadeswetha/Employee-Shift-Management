import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../services/authService";
import "./Auth.css";

const LoginPage = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: "", password: "", role: "EMPLOYEE" });
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await login(form);
      localStorage.setItem("token", res.data.token);
      localStorage.setItem("userId", res.data.userId);
      localStorage.setItem("role", res.data.role);
      localStorage.setItem("email", res.data.email);

      if (res.data.role === "ADMIN") navigate("/admin-dashboard");
      else if (res.data.role === "MANAGER") navigate("/manager-dashboard");
      else navigate("/employee-dashboard");
    } catch (err) {
      setError("Invalid credentials or server error.");
    }
  };

  const goToRegister = () => navigate("/register");

  return (
    <div className="login-page">
      <div className="login-card shadow-lg">
        <div className="brand-header mb-4">
          <span className="brand-circle">S</span>
          <h2 className="ms-2">ShiftNex</h2>
        </div>
        <h5 className="mb-4 text-muted text-center">Welcome back, please login</h5>
        {error && <div className="alert alert-danger">{error}</div>}
        <form onSubmit={handleLogin}>
          <div className="form-group mb-3">
            <label>Email address</label>
            <input
              type="email"
              className="form-control custom-input"
              name="email"
              value={form.email}
              onChange={handleChange}
              placeholder="example@company.com"
              required
            />
          </div>

          <div className="form-group mb-3">
            <label>Password</label>
            <input
              type="password"
              className="form-control custom-input"
              name="password"
              value={form.password}
              onChange={handleChange}
              placeholder="••••••••"
              required
            />
          </div>

          <div className="form-group mb-4">
            <label>Role</label>
            <select
              name="role"
              className="form-select custom-input"
              value={form.role}
              onChange={handleChange}
            >
              <option value="EMPLOYEE">Employee</option>
              <option value="MANAGER">Manager</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>

          <button type="submit" className="btn btn-primary w-100 fw-bold shadow-sm">
            Sign In
          </button>

          {form.role === "ADMIN" && (
            <button
              type="button"
              onClick={goToRegister}
              className="btn btn-link mt-3 d-block text-center"
            >
              Register a new user
            </button>
          )}
        </form>
      </div>
    </div>
  );
};

export default LoginPage;