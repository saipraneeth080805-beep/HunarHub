import React, { useEffect, useState } from 'react';
import api from '../../services/api';
import './AdminTables.css';

const Entrepreneurs = () => {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [updatingId, setUpdatingId] = useState(null);

  const fetchEntrepreneurs = async () => {
    setLoading(true);
    try {
      const res = await api.get('/admin/entrepreneurs');
      setItems(res.data || []);
    } catch (e) {
      setItems([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEntrepreneurs();
  }, []);

  const updateStatus = async (id, action) => {
    setUpdatingId(id);
    try {
      await api.put(`/admin/entrepreneurs/${id}/${action}`);
      await fetchEntrepreneurs();
    } finally {
      setUpdatingId(null);
    }
  };

  if (loading) return <div>Loading entrepreneurs...</div>;

  return (
    <div>
      <div className="admin-section-header">
        <h2>All Entrepreneurs</h2>
        <span className="admin-section-subtitle">
          Total: {items.length}
        </span>
      </div>
      <div className="admin-table-wrapper">
        <table className="admin-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Skills</th>
              <th>Experience</th>
              <th>Status</th>
              <th>Earnings</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {items.map(e => (
              <tr key={e.id}>
                <td>{e.id}</td>
                <td>{e.name}</td>
                <td>{e.email}</td>
                <td>{e.skills || '-'}</td>
                <td>{e.experience || '-'}</td>
                <td>
                  <span className={`admin-badge status-${e.approvalStatus.toLowerCase()}`}>
                    {e.approvalStatus}
                  </span>
                </td>
                <td>{e.earnings != null ? `₹${e.earnings}` : '-'}</td>
                <td>
                  {e.approvalStatus === 'PENDING' ? (
                    <div style={{ display: 'flex', gap: 8 }}>
                      <button
                        className="btn btn-primary"
                        style={{ padding: '6px 10px' }}
                        disabled={updatingId === e.id}
                        onClick={() => updateStatus(e.id, 'approve')}
                      >
                        {updatingId === e.id ? 'Approving...' : 'Approve'}
                      </button>
                      <button
                        className="btn btn-danger"
                        style={{ padding: '6px 10px' }}
                        disabled={updatingId === e.id}
                        onClick={() => updateStatus(e.id, 'reject')}
                      >
                        {updatingId === e.id ? 'Rejecting...' : 'Reject'}
                      </button>
                    </div>
                  ) : (
                    <span style={{ fontSize: '0.8rem', color: '#718096' }}>—</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Entrepreneurs;

