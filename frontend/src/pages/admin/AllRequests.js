import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import '../customer/MyRequests.css';

const AllRequests = () => {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRequests();
  }, []);

  const fetchRequests = async () => {
    try {
      const response = await api.get('/admin/requests');
      setRequests(response.data);
    } catch (error) {
      console.error('Error fetching requests:', error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    const colors = {
      PENDING: '#ffc107',
      ACCEPTED: '#28a745',
      REJECTED: '#dc3545',
      COMPLETED: '#6c757d'
    };
    return colors[status] || '#666';
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h2>All Service Requests</h2>
      {requests.length === 0 ? (
        <p>No service requests found.</p>
      ) : (
        <div className="requests-list">
          {requests.map(request => (
            <div key={request.id} className="card">
              <div className="request-header">
                <h3>Request #{request.id}</h3>
                <span 
                  className="status-badge"
                  style={{ backgroundColor: getStatusColor(request.status) }}
                >
                  {request.status}
                </span>
              </div>
              <p><strong>Customer ID:</strong> {request.customer?.id || 'N/A'}</p>
              <p><strong>Entrepreneur ID:</strong> {request.entrepreneur?.id || 'N/A'}</p>
              <p><strong>Description:</strong> {request.serviceDescription}</p>
              <p><strong>Requested Date:</strong> {new Date(request.requestedDate).toLocaleString()}</p>
              {request.scheduledDate && (
                <p><strong>Scheduled Date:</strong> {new Date(request.scheduledDate).toLocaleString()}</p>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AllRequests;
