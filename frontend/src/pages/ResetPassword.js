import React, { useState } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import api from '../services/api';
import './Login.css';

const ResetPassword = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');

  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [status, setStatus] = useState({ type: 'idle', message: '' });
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus({ type: 'idle', message: '' });

    if (!token) {
      setStatus({ type: 'error', message: 'Invalid or missing reset token.' });
      return;
    }
    if (password.length < 8) {
      setStatus({ type: 'error', message: 'Password must be at least 8 characters long.' });
      return;
    }
    if (password !== confirmPassword) {
      setStatus({ type: 'error', message: 'Passwords do not match.' });
      return;
    }

    setSubmitting(true);
    try {
      await api.post('/auth/reset-password', { token, newPassword: password });
      setStatus({
        type: 'success',
        message: 'Your password has been reset. You can now log in with the new password.',
      });
      setPassword('');
      setConfirmPassword('');
    } catch (error) {
      setStatus({
        type: 'error',
        message: error.response?.data?.message || 'Could not reset password. The link may be invalid or expired.',
      });
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-background">
        <div className="auth-decoration circle-1"></div>
        <div className="auth-decoration circle-2"></div>
        <div className="auth-decoration circle-3"></div>
      </div>
      <div className="auth-card">
        <div className="auth-header">
          <h1 className="auth-title">Reset Password</h1>
          <p className="auth-subtitle">Choose a strong new password for your account.</p>
        </div>

        {!token && (
          <div className="error-message">
            <span className="error-icon">⚠️</span>
            <span>This reset link is missing a token. Please request a new one.</span>
          </div>
        )}

        {status.type === 'error' && (
          <div className="error-message">
            <span className="error-icon">⚠️</span>
            <span>{status.message}</span>
          </div>
        )}
        {status.type === 'success' && (
          <div className="success-message">
            <span>{status.message}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label htmlFor="password" className="form-label">
              <span className="label-icon">🔒</span>
              New Password
            </label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="form-input"
              placeholder="Enter a new password"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword" className="form-label">
              <span className="label-icon">✅</span>
              Confirm New Password
            </label>
            <input
              id="confirmPassword"
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="form-input"
              placeholder="Re-enter the new password"
              required
            />
          </div>

          <button type="submit" className="auth-button auth-button-primary" disabled={submitting || !token}>
            <span>{submitting ? 'Updating password...' : 'Update password'}</span>
          </button>
        </form>

        <div className="auth-footer">
          <p className="auth-footer-text">
            Go back to{' '}
            <Link to="/login" className="auth-link">
              Login
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default ResetPassword;

