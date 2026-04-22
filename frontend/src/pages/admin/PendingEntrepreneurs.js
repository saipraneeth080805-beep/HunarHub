import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { toast } from 'react-toastify';
import './PendingEntrepreneurs.css';

const PendingEntrepreneurs = () => {
  const [entrepreneurs, setEntrepreneurs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchPendingEntrepreneurs();
  }, []);

  const fetchPendingEntrepreneurs = async () => {
    try {
      const response = await api.get('/admin/entrepreneurs/pending');
      setEntrepreneurs(response.data);
    } catch (error) {
      console.error('Error fetching pending entrepreneurs:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (entrepreneurId) => {
    try {
      await api.put(`/admin/entrepreneurs/${entrepreneurId}/approve`);
      fetchPendingEntrepreneurs();
      toast.success('Entrepreneur approved successfully!');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to approve entrepreneur');
    }
  };

  const handleReject = async (entrepreneurId) => {
    if (window.confirm('Are you sure you want to reject this entrepreneur?')) {
      try {
        await api.put(`/admin/entrepreneurs/${entrepreneurId}/reject`);
        fetchPendingEntrepreneurs();
        toast.success('Entrepreneur rejected.');
      } catch (error) {
        toast.error(error.response?.data?.message || 'Failed to reject entrepreneur');
      }
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h2>Pending Entrepreneur Approvals</h2>
      {entrepreneurs.length === 0 ? (
        <p>No pending entrepreneurs.</p>
      ) : (
        <div className="entrepreneurs-list">
          {entrepreneurs.map(entrepreneur => (
            <div key={entrepreneur.id} className="card">
              <h3>{entrepreneur.name}</h3>
              <p><strong>Email:</strong> {entrepreneur.email}</p>
              <p><strong>Skills:</strong> {entrepreneur.skills || 'N/A'}</p>
              <p><strong>Experience:</strong> {entrepreneur.experience || 'N/A'}</p>
              <p><strong>Description:</strong> {entrepreneur.description || 'N/A'}</p>
              <div style={{ marginTop: '15px' }}>
                <button 
                  onClick={() => handleApprove(entrepreneur.id)} 
                  className="btn btn-success"
                  style={{ marginRight: '10px' }}
                >
                  Approve
                </button>
                <button 
                  onClick={() => handleReject(entrepreneur.id)} 
                  className="btn btn-danger"
                >
                  Reject
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default PendingEntrepreneurs;
