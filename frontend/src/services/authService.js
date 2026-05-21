// src/services/authService.js
import axios from "axios"; // Import the axios library for making HTTP requests

const API_URL = "http://localhost:8762/api/auth"; // Define the base URL for the authentication API

export const login = async (credentials) => {
    // Asynchronous function to handle user login
    const response = await axios.post(`${API_URL}/login`, credentials); // Make a POST request to the login endpoint with the provided credentials
    return response.data; // Return the data received in the response
};

export const register = async (userData) => {
    // Asynchronous function to handle user registration
    const response = await axios.post(`${API_URL}/register`, userData); // Make a POST request to the register endpoint with the provided user data
    console.log(userData) // Log the user data to the console (for debugging purposes)
    return response.data; // Return the data received in the response
};

// src/services/authService.js
export const logout = () => {
    // Function to handle user logout
    localStorage.removeItem("token"); // Remove the 'token' from local storage
   // Remove the 'email' from local storage
};