import React, { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import './Register.css';

const Register = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    role: 'CUSTOMER',
    businessCategory: '',
    skills: '',
    experience: '',
    description: '',
    shopName: '',
    ownerName: '',
    shopAddress: '',
    shopPhone: '',
    shopEmail: '',
    shopExperience: '',
    shopDescription: '',
  });
  const [error, setError] = useState('');
  const { register } = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    document.body.style.background = 'linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%)';
    return () => {
      document.body.style.background = '#f8f9fa';
    };
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    // Simple front-end validation for cobbler entrepreneurs
    if (formData.role === 'ENTREPRENEUR' && formData.businessCategory === 'Cobbler') {
      if (
        !formData.shopName.trim() ||
        !formData.ownerName.trim() ||
        !formData.shopAddress.trim() ||
        !formData.shopPhone.trim() ||
        !formData.shopExperience.trim() ||
        !formData.shopDescription.trim()
      ) {
        setError('Please fill all cobbler shop details to create your entrepreneur account.');
        return;
      }
    }

    const result = await register(formData);
    if (result.success) {
      navigate('/home');
    } else {
      setError(result.message);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-background">
        <div className="auth-decoration circle-1"></div>
        <div className="auth-decoration circle-2"></div>
        <div className="auth-decoration circle-3"></div>
      </div>
      <div className="auth-card register-card-expanded">
        <div className="auth-header">
          <h1 className="auth-title">Join HunarHub</h1>
          <p className="auth-subtitle">Create your account and start your journey</p>
        </div>
        
        {error && (
          <div className="error-message">
            <span className="error-icon">⚠️</span>
            <span>{error}</span>
          </div>
        )}
        
        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="name" className="form-label">
                Full Name
              </label>
              <input
                id="name"
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className="form-input"
                placeholder="Enter your full name"
                required
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="email" className="form-label">
                Email Address
              </label>
              <input
                id="email"
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className="form-input"
                placeholder="Enter your email"
                required
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="password" className="form-label">
                Password
              </label>
              <input
                id="password"
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                className="form-input"
                placeholder="Minimum 6 characters"
                required
                minLength="6"
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="role" className="form-label">
                I want to join as
              </label>
              <select 
                id="role"
                name="role" 
                value={formData.role} 
                onChange={handleChange}
                className="form-select"
              >
                <option value="CUSTOMER">Customer - Browse and purchase</option>
                <option value="ENTREPRENEUR">Entrepreneur - Sell products/services</option>
              </select>
            </div>
          </div>

          {formData.role === 'ENTREPRENEUR' && (
            <div className="entrepreneur-fields">
              <div className="section-divider">
                <span>Entrepreneur Details</span>
              </div>

              <div className="form-group">
                <label htmlFor="businessCategory" className="form-label">
                  Business category
                </label>
                <select
                  id="businessCategory"
                  name="businessCategory"
                  value={formData.businessCategory}
                  onChange={handleChange}
                  className="form-select"
                  required
                >
                  <option value="">Select your category</option>
                  <option value="Cobbler">Cobbler</option>
                  <option value="Tailor">Tailor</option>
                  <option value="Potter">Potter</option>
                  <option value="Artisan">Artisan</option>
                </select>
              </div>
              
              <div className="form-group">
                <label htmlFor="skills" className="form-label">
                  Skills & Expertise
                </label>
                <textarea
                  id="skills"
                  name="skills"
                  value={formData.skills}
                  onChange={handleChange}
                  className="form-textarea"
                  placeholder="e.g., Cobbler, Tailor, Potter, Artisan"
                  rows="3"
                />
              </div>

              <div className="form-group">
                <label htmlFor="experience" className="form-label">
                  Experience
                </label>
                <textarea
                  id="experience"
                  name="experience"
                  value={formData.experience}
                  onChange={handleChange}
                  className="form-textarea"
                  placeholder="Describe your years of experience and background"
                  rows="3"
                />
              </div>

              <div className="form-group">
                <label htmlFor="description" className="form-label">
                  About You
                </label>
                <textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  className="form-textarea"
                  placeholder="Tell customers about yourself and your work"
                  rows="4"
                />
              </div>

              {formData.businessCategory === 'Cobbler' && (
                <>
                  <div className="section-divider">
                    <span>Cobbler / Shop information</span>
                  </div>
                  <div className="form-group">
                    <label htmlFor="shopName" className="form-label">
                      Shop name
                    </label>
                    <input
                      id="shopName"
                      type="text"
                      name="shopName"
                      value={formData.shopName}
                      onChange={handleChange}
                      className="form-input"
                      placeholder="e.g., Classic Leather Shoe Repair"
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="ownerName" className="form-label">
                      Cobbler’s name
                    </label>
                    <input
                      id="ownerName"
                      type="text"
                      name="ownerName"
                      value={formData.ownerName}
                      onChange={handleChange}
                      className="form-input"
                      placeholder="Your full name"
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="shopAddress" className="form-label">
                      Location / address
                    </label>
                    <textarea
                      id="shopAddress"
                      name="shopAddress"
                      value={formData.shopAddress}
                      onChange={handleChange}
                      className="form-textarea"
                      rows="2"
                      placeholder="Full shop address"
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="shopPhone" className="form-label">
                      Phone number / WhatsApp
                    </label>
                    <input
                      id="shopPhone"
                      type="text"
                      name="shopPhone"
                      value={formData.shopPhone}
                      onChange={handleChange}
                      className="form-input"
                      placeholder="Contact number customers can reach you on"
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="shopEmail" className="form-label">
                      Shop email (optional)
                    </label>
                    <input
                      id="shopEmail"
                      type="email"
                      name="shopEmail"
                      value={formData.shopEmail}
                      onChange={handleChange}
                      className="form-input"
                      placeholder="Optional contact email"
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="shopExperience" className="form-label">
                      Years of experience
                    </label>
                    <input
                      id="shopExperience"
                      type="text"
                      name="shopExperience"
                      value={formData.shopExperience}
                      onChange={handleChange}
                      className="form-input"
                      placeholder="e.g., 20+ years"
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="shopDescription" className="form-label">
                      Short description about the shop
                    </label>
                    <textarea
                      id="shopDescription"
                      name="shopDescription"
                      value={formData.shopDescription}
                      onChange={handleChange}
                      className="form-textarea"
                      rows="3"
                      placeholder="e.g., We repair and make handmade leather shoes for over 20 years."
                    />
                  </div>
                </>
              )}
            </div>
          )}
          
          <button type="submit" className="auth-button auth-button-primary">
            <span>Create Account</span>
            <span className="button-arrow">→</span>
          </button>
        </form>
        
        <div className="auth-footer">
          <p className="auth-footer-text">
            Already have an account?{' '}
            <a href="/login" className="auth-link">Sign in here</a>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;
