import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import './Login.css';

const ForgotPassword = () => {
  const [step, setStep] = useState('email'); // 'email' | 'reset'
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [status, setStatus] = useState({ type: 'idle', message: '' });
  const [submitting, setSubmitting] = useState(false);

  const handleVerifyEmail = async (e) => {
    e.preventDefault();
    setStatus({ type: 'idle', message: '' });
    setSubmitting(true);
    try {
      await api.post('/auth/forgot-password', { email });
      setStatus({
        type: 'success',
        message: 'Email found. Please choose a new password.',
      });
      setStep('reset');
    } catch (error) {
      setStatus({
        type: 'error',
        message: error.response?.data?.message || 'Could not verify email. Please try again.',
      });
    } finally {
      setSubmitting(false);
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    setStatus({ type: 'idle', message: '' });

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
      await api.post('/auth/reset-password', { email, newPassword: password });
      setStatus({
        type: 'success',
        message: 'Your password has been updated. You can now log in with the new password.',
      });
      setPassword('');
      setConfirmPassword('');
    } catch (error) {
      setStatus({
        type: 'error',
        message: error.response?.data?.message || 'Could not reset password. Please try again.',
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
          <h1 className="auth-title">Forgot Password</h1>
          {step === 'email' ? (
            <p className="auth-subtitle">
              Enter your registered email address. If we find an account, you can set a new password.
            </p>
          ) : (
            <p className="auth-subtitle">Choose a new password for this account.</p>
          )}
        </div>

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

        {step === 'email' ? (
          <form onSubmit={handleVerifyEmail} className="auth-form">
            <div className="form-group">
              <label htmlFor="email" className="form-label">
                <span className="label-icon">📧</span>
                Email
              </label>
              <input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="form-input"
                placeholder="Enter your registered email"
                required
              />
            </div>

            <button type="submit" className="auth-button auth-button-primary" disabled={submitting}>
              <span>{submitting ? 'Checking...' : 'Verify email'}</span>
            </button>
          </form>
        ) : (
          <form onSubmit={handleResetPassword} className="auth-form">
            <div className="form-group">
              <label className="form-label">
                <span className="label-icon">📧</span>
                Email
              </label>
              <input value={email} disabled className="form-input" />
            </div>

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

            <button type="submit" className="auth-button auth-button-primary" disabled={submitting}>
              <span>{submitting ? 'Updating...' : 'Update password'}</span>
            </button>
          </form>
        )}

        <div className="auth-footer">
          <p className="auth-footer-text">
            Remembered your password?{' '}
            <Link to="/login" className="auth-link">
              Back to login
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default ForgotPassword;

