import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { toast } from 'react-toastify';
import './IncomingRequests.css';

const IncomingRequests = () => {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRequests();
  }, []);

  const fetchRequests = async () => {
    try {
      const response = await api.get('/entrepreneur/requests/incoming');
      setRequests(response.data);
    } catch (error) {
      console.error('Error fetching requests:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAccept = async (requestId) => {
    try {
      await api.put(`/entrepreneur/requests/${requestId}/accept`);
      fetchRequests();
      toast.success('Request accepted!');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to accept request');
    }
  };

  const handleReject = async (requestId) => {
    try {
      await api.put(`/entrepreneur/requests/${requestId}/reject`);
      fetchRequests();
      toast.success('Request rejected!');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to reject request');
    }
  };

  const handleComplete = async (requestId) => {
    try {
      await api.put(`/entrepreneur/requests/${requestId}/complete`);
      fetchRequests();
      toast.success('Request marked as completed!');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to complete request');
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h2>Incoming Service Requests</h2>
      {requests.length === 0 ? (
        <p>No incoming requests.</p>
      ) : (
        <div className="requests-list">
          {requests.map(request => (
            <div key={request.id} className="card">
              <h3>Request from {request.customerName}</h3>
              <p><strong>Status:</strong> {request.status}</p>
              <p><strong>Description:</strong> {request.serviceDescription}</p>
              <p><strong>Requested Date:</strong> {new Date(request.requestedDate).toLocaleString()}</p>
              <div style={{ marginTop: '15px' }}>
                {request.status === 'PENDING' && (
                  <>
                    <button onClick={() => handleAccept(request.id)} className="btn btn-success" style={{ marginRight: '10px' }}>
                      Accept
                    </button>
                    <button onClick={() => handleReject(request.id)} className="btn btn-danger">
                      Reject
                    </button>
                  </>
                )}
                {request.status === 'ACCEPTED' && (
                  <button onClick={() => handleComplete(request.id)} className="btn btn-primary">
                    Mark as Completed
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default IncomingRequests;
