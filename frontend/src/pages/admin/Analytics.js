import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { toast } from 'react-toastify';
import './Analytics.css';

const Analytics = () => {
  const [analytics, setAnalytics] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchAnalytics();
  }, []);

  const fetchAnalytics = async () => {
    try {
      const response = await api.get('/admin/analytics');
      setAnalytics(response.data);
    } catch (error) {
      toast.error('Failed to load analytics');
      console.error('Error fetching analytics:', error);
    } finally {
      setLoading(false);
    }
  };

  // Helpers for vertical bar charts
  const getBarHeight = (count, total) => {
    if (!total || !count) return '4px';
    const MAX = 140;
    const MIN = 16;
    const ratio = count / total;
    const height = MIN + ratio * (MAX - MIN);
    return `${height}px`;
  };

  const renderBarChart = (dataMap, total, extraBarClass = '') => {
    const entries = Object.entries(dataMap || {});
    if (!entries.length || !total) {
      return <p className="chart-empty">No data yet.</p>;
    }

    return (
      <div className="chart-bars">
        {entries.map(([key, value]) => (
          <div key={key} className="chart-bar-item">
            <div className={`chart-bar-graph ${extraBarClass}`} style={{ height: getBarHeight(value, total) }}>
              <span className="chart-bar-value">{value}</span>
            </div>
            <span className="chart-bar-label">{key}</span>
          </div>
        ))}
      </div>
    );
  };

  if (loading) {
    return (
      <div className="analytics-container">
        <div className="loading-state">
          <div className="loading-spinner"></div>
          <p>Loading analytics...</p>
        </div>
      </div>
    );
  }

  if (!analytics) {
    return (
      <div className="analytics-container">
        <p>Failed to load analytics</p>
      </div>
    );
  }

  return (
    <div className="analytics-container">
      <h2 className="section-title">Platform Analytics & Reports</h2>
      
      <div className="stats-grid">
        <div className="stat-card" onClick={() => navigate('/admin/users')}>
          <div className="stat-icon">TU</div>
          <div className="stat-content">
            <h3>Total Users</h3>
            <p className="stat-value">{analytics.totalUsers || 0}</p>
          </div>
        </div>

        <div className="stat-card" onClick={() => navigate('/admin/users')}>
          <div className="stat-icon">CU</div>
          <div className="stat-content">
            <h3>Customers</h3>
            <p className="stat-value">{analytics.totalCustomers || 0}</p>
          </div>
        </div>

        <div className="stat-card" onClick={() => navigate('/admin/entrepreneurs')}>
          <div className="stat-icon">EN</div>
          <div className="stat-content">
            <h3>Entrepreneurs</h3>
            <p className="stat-value">{analytics.totalEntrepreneurs || 0}</p>
            <p className="stat-subtext">
              {analytics.pendingEntrepreneurs || 0} pending, {analytics.approvedEntrepreneurs || 0} approved
            </p>
          </div>
        </div>

        <div className="stat-card" onClick={() => navigate('/admin/orders')}>
          <div className="stat-icon">OR</div>
          <div className="stat-content">
            <h3>Total Orders</h3>
            <p className="stat-value">{analytics.totalOrders || 0}</p>
          </div>
        </div>

        <div className="stat-card" onClick={() => navigate('/admin/requests')}>
          <div className="stat-icon">SR</div>
          <div className="stat-content">
            <h3>Service Requests</h3>
            <p className="stat-value">{analytics.totalServiceRequests || 0}</p>
          </div>
        </div>

        <div className="stat-card" onClick={() => navigate('/admin/categories')}>
          <div className="stat-icon">PR</div>
          <div className="stat-content">
            <h3>Total Products</h3>
            <p className="stat-value">{analytics.totalProducts || 0}</p>
          </div>
        </div>

        <div className="stat-card revenue" onClick={() => navigate('/admin/orders')}>
          <div className="stat-icon">₹</div>
          <div className="stat-content">
            <h3>Total Revenue</h3>
            <p className="stat-value">₹{analytics.totalRevenue?.toFixed(2) || '0.00'}</p>
          </div>
        </div>

        <div className="stat-card dispute" onClick={() => navigate('/admin/disputes')}>
          <div className="stat-icon">!</div>
          <div className="stat-content">
            <h3>Disputes</h3>
            <p className="stat-value">{analytics.totalDisputes || 0}</p>
            <p className="stat-subtext">
              {analytics.openDisputes || 0} open
            </p>
          </div>
        </div>
      </div>

      {/* Orders by Status */}
      {analytics.ordersByStatus && (
        <div className="chart-section">
          <h3 className="chart-title">Orders by Status</h3>
          {renderBarChart(analytics.ordersByStatus, analytics.totalOrders)}
        </div>
      )}

      {/* Requests by Status */}
      {analytics.requestsByStatus && (
        <div className="chart-section">
          <h3 className="chart-title">Service Requests by Status</h3>
          {renderBarChart(analytics.requestsByStatus, analytics.totalServiceRequests)}
        </div>
      )}

      {/* Disputes by Status */}
      {analytics.disputesByStatus && (
        <div className="chart-section">
          <h3 className="chart-title">Disputes by Status</h3>
          {renderBarChart(analytics.disputesByStatus, analytics.totalDisputes, 'dispute-bar')}
        </div>
      )}

      {/* Users by Role */}
      {analytics.usersByRole && (
        <div className="chart-section">
          <h3 className="chart-title">Users by Role</h3>
          {renderBarChart(analytics.usersByRole, analytics.totalUsers)}
        </div>
      )}
    </div>
  );
};

export default Analytics;
