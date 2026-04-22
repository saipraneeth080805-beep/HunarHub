import React, { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { toast } from 'react-toastify';
import '../Login.css';
import './AdminLogin.css';

const AdminLogin = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    document.body.style.background = 'linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%)';
    return () => {
      document.body.style.background = '#f8f9fa';
    };
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const result = await login(email, password);
    if (result.success) {
      // Check user role from result or localStorage
      const user = result.user || JSON.parse(localStorage.getItem('user') || '{}');
      const userRole = user.role?.toString().toUpperCase();
      
      if (userRole === 'ADMIN') {
        toast.success('Admin login successful!');
        navigate('/admin');
      } else {
        setError('You are not authorized to access admin panel');
        toast.error('Access denied. Admin credentials required.');
        setTimeout(() => {
          window.location.href = '/admin/login';
        }, 2000);
      }
    } else {
      setError(result.message);
      toast.error(result.message);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-background">
        <div className="auth-decoration circle-1"></div>
        <div className="auth-decoration circle-2"></div>
        <div className="auth-decoration circle-3"></div>
      </div>
      <div className="auth-card admin-login-card">
        <div className="auth-header">
          <h1 className="auth-title">Admin Login</h1>
          <p className="auth-subtitle">Super Admin Access Portal</p>
        </div>
        
        {error && (
          <div className="error-message">
            <span className="error-icon">⚠️</span>
            <span>{error}</span>
          </div>
        )}
        
        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label htmlFor="email" className="form-label">
              <span className="label-icon">👤</span>
              Admin Email or Username
            </label>
            <input
              id="email"
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="form-input"
              placeholder="Enter admin email or username"
              required
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="password" className="form-label">
              <span className="label-icon">🔒</span>
              Admin Password
            </label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="form-input"
              placeholder="Enter admin password"
              required
            />
          </div>
          
          <button type="submit" className="auth-button auth-button-primary">
            <span>Login as Admin</span>
            <span className="button-arrow">→</span>
          </button>
        </form>
        
        <div className="auth-footer">
          <p className="auth-footer-text">
            Regular user?{' '}
            <a href="/login" className="auth-link">Login here</a>
          </p>
        </div>
      </div>
    </div>
  );
};

export default AdminLogin;
