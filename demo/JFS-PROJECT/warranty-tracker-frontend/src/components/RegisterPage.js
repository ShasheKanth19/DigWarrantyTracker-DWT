// src/components/RegisterPage.js
import React from 'react';

function RegisterPage() {
  return (
    <div>
      <h2>Register Page</h2>
      <form>
        <input type="text" placeholder="Username" /><br />
        <input type="email" placeholder="Email" /><br />
        <input type="password" placeholder="Password" /><br />
        <button type="submit">Register</button>
      </form>
    </div>
  );
}

export default RegisterPage;
