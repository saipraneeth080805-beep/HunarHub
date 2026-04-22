import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import '../customer/MyOrders.css';

const AllOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await api.get('/admin/orders');
      setOrders(response.data);
    } catch (error) {
      console.error('Error fetching orders:', error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    const colors = {
      PLACED: '#007bff',
      CONFIRMED: '#28a745',
      SHIPPED: '#17a2b8',
      DELIVERED: '#6c757d',
      CANCELLED: '#dc3545'
    };
    return colors[status] || '#666';
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h2>All Orders</h2>
      {orders.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        <div className="orders-list">
          {orders.map(order => (
            <div key={order.id} className="card">
              <div className="order-header">
                <h3>Order #{order.id}</h3>
                <span 
                  className="status-badge"
                  style={{ backgroundColor: getStatusColor(order.status) }}
                >
                  {order.status}
                </span>
              </div>
              <p><strong>Customer:</strong> {order.customerName || order.customer?.name || 'N/A'}</p>
              <p><strong>Customer Email:</strong> {order.customerEmail || order.customer?.email || 'N/A'}</p>
              <p><strong>Product:</strong> {order.productName || order.product?.name || 'N/A'}</p>
              <p><strong>Quantity:</strong> {order.quantity}</p>
              <p><strong>Total Price:</strong> ₹{order.totalPrice}</p>
              <p><strong>Order Date:</strong> {new Date(order.orderDate).toLocaleString()}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AllOrders;
