import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import './Landing.css';

const Landing = () => {
  // Public marketing page shown before login
  useEffect(() => {
    document.body.classList.add('landing-theme');
    return () => {
      document.body.classList.remove('landing-theme');
    };
  }, []);

  return (
    <div className="landing-container">
      {/* Hero Section */}
      <section className="hero">
        <div className="hero-content">
          <h1 className="hero-title">HunarHub</h1>
          <p className="hero-subtitle">Empowering Local Artisans & Connecting Communities</p>
          <p className="hero-description">
            A digital marketplace where local micro-entrepreneurs—cobblers, potters, tailors, artisans,
            and other local makers—meet customers who value authentic, handmade products and personalized services.
          </p>
          <div className="hero-buttons">
            <Link to="/register" className="btn btn-primary btn-large">
              Join as a Customer
            </Link>
            <Link to="/register" className="btn btn-secondary btn-large">
              Join as Entrepreneur
            </Link>
          </div>
        </div>
        <div className="hero-decoration">
          <div className="hero-info-card">
            <h3>Built for local communities</h3>
            <p>Discover trusted makers, place service requests, and support nearby talent.</p>
            <ul>
              <li>Verified entrepreneur profiles</li>
              <li>Products and service requests in one place</li>
              <li>In-app notifications for every update</li>
            </ul>
          </div>
        </div>
      </section>

      {/* About Section */}
      <section className="about-section">
        <div className="container">
          <h2 className="section-title">What is HunarHub?</h2>
          <div className="about-content">
            <div className="about-text">
              <p className="lead-text">
                HunarHub bridges the gap between traditional local businesses and modern digital commerce.
                We believe in preserving and promoting the skills of local artisans while making it easier
                for communities to discover and support them.
              </p>
              <p>
                Whether you're a customer looking for quality craftsmanship or an entrepreneur seeking
                to expand your reach, HunarHub provides a trusted, digital storefront that connects talent with opportunity.
              </p>
              <ul className="about-highlights">
                <li>Browse verified local artisans near you in a few clicks.</li>
                <li>Request custom work, repairs, or made‑to‑order products.</li>
                <li>Give artisans fair visibility without needing their own website.</li>
                <li>Keep money and opportunities flowing inside your community.</li>
              </ul>
            </div>
            <div className="about-stats">
              <div className="stat-item">
                <div className="stat-number">100+</div>
                <div className="stat-label">Local Artisans</div>
              </div>
              <div className="stat-item">
                <div className="stat-number">4</div>
                <div className="stat-label">Categories</div>
              </div>
              <div className="stat-item">
                <div className="stat-number">24/7</div>
                <div className="stat-label">Available</div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section">
        <div className="container">
          <h2 className="section-title">Why Choose HunarHub?</h2>
          <div className="features-grid">
            <div className="feature-card">
              <img
                className="feature-image"
                src="https://images.unsplash.com/photo-1521737604893-d14cc237f11d?auto=format&fit=crop&w=220&q=80"
                alt="Customers collaborating"
              />
              <h3>For Customers</h3>
              <ul className="feature-list">
                <li>Browse verified local entrepreneurs</li>
                <li>View detailed profiles and portfolios</li>
                <li>Place service requests easily</li>
                <li>Purchase handmade products</li>
                <li>Track orders and requests</li>
                <li>Leave reviews and ratings</li>
              </ul>
              <Link to="/register" className="feature-link">
                Get Started →
              </Link>
            </div>

            <div className="feature-card featured">
              <div className="feature-badge">Popular</div>
              <img
                className="feature-image"
                src="https://images.unsplash.com/photo-1452802447250-470a88ac82bc?auto=format&fit=crop&w=220&q=80"
                alt="Entrepreneur creating handmade work"
              />
              <h3>For Entrepreneurs</h3>
              <ul className="feature-list">
                <li>Create your professional profile</li>
                <li>Showcase your skills and experience</li>
                <li>List and manage products</li>
                <li>Accept or reject service requests</li>
                <li>Track orders and earnings</li>
                <li>Build your local reputation</li>
              </ul>
              <Link to="/register" className="feature-link">
                Join Now →
              </Link>
            </div>

            <div className="feature-card">
              <img
                className="feature-image"
                src="https://images.unsplash.com/photo-1563013544-824ae1b704d3?auto=format&fit=crop&w=220&q=80"
                alt="Secure trusted platform"
              />
              <h3>Trusted Platform</h3>
              <ul className="feature-list">
                <li>All entrepreneurs are verified</li>
                <li>Admin-approved profiles</li>
                <li>Secure transactions</li>
                <li>Quality assurance</li>
                <li>Community reviews</li>
                <li>Reliable service</li>
              </ul>
              <Link to="/entrepreneurs" className="feature-link">
                Explore →
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* How It Works Section */}
      <section className="how-it-works-section">
        <div className="container">
          <h2 className="section-title">How It Works</h2>
          <div className="steps-container">
            <div className="step-item">
              <div className="step-number">1</div>
              <h3>Register</h3>
              <p>Create your account as a customer or entrepreneur</p>
            </div>
            <div className="step-arrow">→</div>
            <div className="step-item">
              <div className="step-number">2</div>
              <h3>Explore</h3>
              <p>Browse entrepreneurs, products, and services</p>
            </div>
            <div className="step-arrow">→</div>
            <div className="step-item">
              <div className="step-number">3</div>
              <h3>Connect</h3>
              <p>Place orders or service requests</p>
            </div>
            <div className="step-arrow">→</div>
            <div className="step-item">
              <div className="step-number">4</div>
              <h3>Thrive</h3>
              <p>Support local businesses and grow together</p>
            </div>
          </div>
        </div>
      </section>

      {/* Categories Section */}
      <section className="categories-section">
        <div className="container">
          <h2 className="section-title">Our Categories</h2>
          <div className="categories-grid">
            <div className="category-card">
              <img
                className="category-image"
                src="https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=400&q=80"
                alt="Cobbler tools and shoes"
              />
              <h3>Cobbler</h3>
              <p>Expert shoe repair and customization</p>
            </div>
            <div className="category-card">
              <img
                className="category-image"
                src="https://images.unsplash.com/photo-1610701596007-11502861dcfa?auto=format&fit=crop&w=400&q=80"
                alt="Pottery and ceramics"
              />
              <h3>Potter</h3>
              <p>Handcrafted pottery and ceramics</p>
            </div>
            <div className="category-card">
              <img
                className="category-image"
                src="https://images.unsplash.com/photo-1594938298603-c8148c4dae35?auto=format&fit=crop&w=400&q=80"
                alt="Tailoring and stitching"
              />
              <h3>Tailor</h3>
              <p>Custom clothing and alterations</p>
            </div>
            <div className="category-card">
              <img
                className="category-image"
                src="https://images.unsplash.com/photo-1460661419201-fd4cecdf8a8b?auto=format&fit=crop&w=400&q=80"
                alt="Handmade artisan crafts"
              />
              <h3>Artisan</h3>
              <p>Unique handmade crafts and art</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Landing;

