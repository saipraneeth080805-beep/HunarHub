import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import { toast } from 'react-toastify';
import './MyOrders.css';

const MyOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [cancellingId, setCancellingId] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await api.get('/orders/my');
      const data = response.data;
      const list = data?.content ?? (Array.isArray(data) ? data : []);
      // Newest orders first
      list.sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate));
      setOrders(list);
    } catch (error) {
      console.error('Error fetching orders:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelOrder = async (orderId) => {
    setCancellingId(orderId);
    try {
      await api.put(`/orders/${orderId}/cancel`);
      toast.success('Order cancelled');
      fetchOrders();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to cancel order');
    } finally {
      setCancellingId(null);
    }
  };

  const getStatusColor = (status) => {
    const colors = {
      PLACED: '#007bff',
      CONFIRMED: '#28a745',
      SHIPPED: '#17a2b8',
      DELIVERED: '#6c757d',
      CANCELLED: '#dc3545',
      RETURN_REQUESTED: '#fd7e14'
    };
    return colors[status] || '#666';
  };

  if (loading) return <div className="container">Loading...</div>;

  return (
    <div className="container">
      <h1>My Orders</h1>
      {orders.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        <div className="orders-list">
          {orders.map(order => (
            <div
              key={order.id}
              className="card order-clickable"
              onClick={() =>
                order.productId &&
                navigate(`/products/${order.productId}`, { state: { order } })
              }
            >
              <div className="order-header">
                <h3>{order.productName}</h3>
                <span 
                  className="status-badge"
                  style={{ backgroundColor: getStatusColor(order.status) }}
                >
                  {order.status}
                </span>
              </div>
              <p><strong>Quantity:</strong> {order.quantity}</p>
              <p><strong>Total Price:</strong> ₹{order.totalPrice}</p>
              <p><strong>Order Date:</strong> {new Date(order.orderDate).toLocaleString()}</p>
              {(order.status === 'PLACED' || order.status === 'CONFIRMED') && (
                <div className="order-actions">
                  <button
                    type="button"
                    className="btn btn-danger btn-cancel"
                    onClick={() => handleCancelOrder(order.id)}
                    disabled={cancellingId === order.id}
                  >
                    {cancellingId === order.id ? 'Cancelling...' : 'Cancel order'}
                  </button>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default MyOrders;
