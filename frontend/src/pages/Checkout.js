import React, { useContext, useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { CartContext } from '../context/CartContext';
import api from '../services/api';
import { toast } from 'react-toastify';
import './Checkout.css';

const Checkout = () => {
  const { user } = useContext(AuthContext);
  const { cart, clearCart } = useContext(CartContext);
  const [placing, setPlacing] = useState(false);
  const paymentMethod = 'COD';
  const [address, setAddress] = useState({
    name: '',
    email: '',
    phone: '',
    addressLine1: '',
    addressLine2: '',
    city: '',
    state: '',
    district: '',
    pincode: '',
  });
  const navigate = useNavigate();

  useEffect(() => {
    if (user) {
      setAddress((prev) => ({
        ...prev,
        name: prev.name || user.name || '',
        email: prev.email || user.email || '',
      }));
    }
  }, [user]);

  const total = cart.reduce((sum, item) => sum + item.product.price * item.quantity, 0);

  if (!user) {
    return (
      <div className="checkout-page">
        <p>Please log in to continue to checkout.</p>
        <Link to="/login">Login</Link>
      </div>
    );
  }

  if (cart.length === 0) {
    return (
      <div className="checkout-page">
        <p>Your cart is empty.</p>
        <Link to="/home" className="btn btn-primary">Go to home</Link>
      </div>
    );
  }

  const handleCheckout = async () => {
    const required = ['name', 'email', 'phone', 'addressLine1', 'city', 'state', 'district', 'pincode'];
    const missing = required.filter((key) => !address[key].trim());
    if (missing.length) {
      toast.error('Please fill in all required address details.');
      return;
    }

    setPlacing(true);
    try {
      for (const item of cart) {
        await api.post('/orders', {
          productId: item.product.id,
          quantity: item.quantity,
          customerName: address.name,
          customerEmail: address.email,
          customerPhone: address.phone,
          addressLine1: address.addressLine1,
          addressLine2: address.addressLine2,
          city: address.city,
          state: address.state,
          district: address.district,
          pincode: address.pincode,
          paymentMethod,
        });
      }
      clearCart();
      toast.success('Order placed successfully!');
      navigate('/orders/my');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to place order');
    } finally {
      setPlacing(false);
    }
  };

  return (
    <div className="checkout-page">
      <h1 className="checkout-title">Checkout</h1>
      <div className="checkout-layout">
        <div className="checkout-main">
          <section className="checkout-card">
            <h2 className="checkout-section-heading">Delivery details</h2>
            <div className="checkout-address-grid">
              <div className="form-group">
                <label>Name</label>
                <input
                  type="text"
                  value={address.name}
                  onChange={(e) => setAddress({ ...address, name: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  value={address.email}
                  onChange={(e) => setAddress({ ...address, email: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Phone number</label>
                <input
                  type="tel"
                  value={address.phone}
                  onChange={(e) => setAddress({ ...address, phone: e.target.value })}
                />
              </div>
              <div className="form-group form-group-full">
                <label>Address line 1</label>
                <input
                  type="text"
                  value={address.addressLine1}
                  onChange={(e) => setAddress({ ...address, addressLine1: e.target.value })}
                />
              </div>
              <div className="form-group form-group-full">
                <label>Address line 2 (optional)</label>
                <input
                  type="text"
                  value={address.addressLine2}
                  onChange={(e) => setAddress({ ...address, addressLine2: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Town / City</label>
                <input
                  type="text"
                  value={address.city}
                  onChange={(e) => setAddress({ ...address, city: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>State</label>
                <input
                  type="text"
                  value={address.state}
                  onChange={(e) => setAddress({ ...address, state: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>District</label>
                <input
                  type="text"
                  value={address.district}
                  onChange={(e) => setAddress({ ...address, district: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Pincode</label>
                <input
                  type="text"
                  value={address.pincode}
                  onChange={(e) => setAddress({ ...address, pincode: e.target.value })}
                />
              </div>
            </div>
          </section>

          <section className="checkout-card">
            <h2 className="checkout-section-heading">Payment method</h2>
            <p style={{ margin: 0, fontSize: '0.95rem', color: '#4b5563' }}>
              Cash on delivery
            </p>
          </section>
        </div>

        <aside className="checkout-summary">
          <div className="checkout-card">
            <h2 className="checkout-section-heading">Order summary</h2>
            <ul className="checkout-items">
              {cart.map((item) => (
                <li key={item.product.id}>
                  <span>{item.product.name} × {item.quantity}</span>
                  <span>₹{(item.product.price * item.quantity).toFixed(2)}</span>
                </li>
              ))}
            </ul>
            <div className="checkout-total-row">
              <span>Total</span>
              <span className="checkout-total-amount">₹{total.toFixed(2)}</span>
            </div>
            <button
              className="btn btn-primary checkout-btn"
              onClick={handleCheckout}
              disabled={placing}
            >
              {placing ? 'Placing order...' : 'Checkout'}
            </button>
          </div>
        </aside>
      </div>
    </div>
  );
};

export default Checkout;

