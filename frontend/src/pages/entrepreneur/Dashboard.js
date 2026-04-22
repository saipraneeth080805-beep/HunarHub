import React from 'react';
import { Routes, Route, Link, useLocation } from 'react-router-dom';
import MyProducts from './MyProducts';
import IncomingRequests from './IncomingRequests';
import MyOrders from './MyOrders';
import './Dashboard.css';

const Dashboard = () => {
  const location = useLocation();

  return (
    <div className="container">
      <h1>Entrepreneur Dashboard</h1>
      <div className="dashboard-layout">
        <nav className="dashboard-nav">
          <Link 
            to="/entrepreneur/products" 
            className={location.pathname.includes('/products') ? 'active' : ''}
          >
            My Products
          </Link>
          <Link 
            to="/entrepreneur/requests" 
            className={location.pathname.includes('/requests') ? 'active' : ''}
          >
            Incoming Requests
          </Link>
          <Link 
            to="/entrepreneur/orders" 
            className={location.pathname.includes('/orders') ? 'active' : ''}
          >
            My Orders
          </Link>
        </nav>
        <div className="dashboard-content">
          <Routes>
            <Route path="/products" element={<MyProducts />} />
            <Route path="/requests" element={<IncomingRequests />} />
            <Route path="/orders" element={<MyOrders />} />
            <Route path="/" element={<MyProducts />} />
          </Routes>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
