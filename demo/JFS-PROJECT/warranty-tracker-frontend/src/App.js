// src/App.js
import React from 'react';
import Dashboard from './components/Dashboard';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import RegisterPage from './components/RegisterPage';
import LoginPage from './components/LoginPage';
import UserList from './components/UserList';
import AddUserForm from './components/AddUserForm';
import AddProductForm from './components/AddProductForm';
import NotificationList from './components/NotificationList';
import ProductList from './components/product/ProductList';
import MLDashboardPage from './components/mlDashboard/MLDashboardPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/users" element={<UserList />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/add-user" element={<AddUserForm />} />
        <Route path="/add-product" element={<AddProductForm />} />
        <Route path="/notifications" element={<NotificationList />} />
        <Route path="/products" element={<ProductList />} />
        <Route path="/ml-dashboard" element={<MLDashboardPage />} />

      </Routes>
    </Router>
  );
}

export default App;