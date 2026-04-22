import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const PrivateRoute = ({ children, allowedRoles = [] }) => {
  const { user, loading } = useContext(AuthContext);

  if (loading) {
    return <div className="container">Loading...</div>;
  }

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (allowedRoles.length > 0) {
    // Normalize role comparison (handle both string and enum formats)
    const userRole = user.role?.toString().toUpperCase();
    const hasAccess = allowedRoles.some(role => 
      role.toUpperCase() === userRole
    );
    
    if (!hasAccess) {
      return <Navigate to="/" />;
    }
  }

  return children;
};

export default PrivateRoute;
