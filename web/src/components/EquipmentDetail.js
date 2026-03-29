import React, { useEffect, useState, useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getCurrentUser, logout } from '../services/authService';
import './EquipmentDetail.css';
import logo from '../assets/UniGear Symbol.png';

const API_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';
const FETCH_TIMEOUT = 30000; // 30 second timeout

function EquipmentDetail() {
  const navigate = useNavigate();
  const { id } = useParams();
  const user = getCurrentUser();
  const [equipment, setEquipment] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const handleLogout = useCallback(() => {
    logout();
    navigate('/login');
  }, [navigate]);

  const toDisplayStatus = useCallback((status) => {
    if (!status) {
      return 'Unknown';
    }
    return status.replace('_', ' ').replace(/\b\w/g, (char) => char.toUpperCase());
  }, []);

  useEffect(() => {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), FETCH_TIMEOUT);

    const loadEquipmentDetail = async () => {
      const currentUser = getCurrentUser();
      if (!currentUser) {
        navigate('/login', { replace: true });
        return;
      }

      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/login', { replace: true });
        return;
      }

      setLoading(true);
      setError('');

      try {
        const response = await fetch(`${API_URL}/equipment/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`
          },
          signal: controller.signal
        });

        if (response.status === 401) {
          logout();
          navigate('/login', { replace: true });
          return;
        }

        if (!response.ok) {
          const message = await response.text();
          throw new Error(message || 'Failed to load equipment details');
        }

        const data = await response.json();
        setEquipment(data);
      } catch (err) {
        if (err.name === 'AbortError') {
          setError('Request timed out. Server is taking too long to respond.');
        } else {
          setError(err.message || 'Error connecting to server');
        }
      } finally {
        setLoading(false);
      }
    };

    loadEquipmentDetail();

    return () => {
      clearTimeout(timeoutId);
      controller.abort();
    };
  }, [id, navigate]);

  if (!user) {
    return null;
  }

  if (loading) {
    return <div className="loading">Loading equipment details...</div>;
  }

  if (!equipment) {
    return (
      <div className="detail-container">
        <p>{error || 'Equipment not found'}</p>
        <button onClick={() => navigate('/dashboard')}>Back to Catalog</button>
      </div>
    );
  }

  return (
    <div className="detail-container">
      {/* Navigation Header */}
      <header className="detail-header">
        <div className="header-content">
          <div className="logo-section">
            <img src={logo} alt="Logo" className="header-logo" />
            <span className="header-title">UniGear Tracker</span>
          </div>
          <nav className="nav-links">
            <button type="button" onClick={() => navigate('/dashboard')} className="nav-link">Catalog</button>
            {user?.role === 'ADMIN' ? (
              <>
                <button type="button" onClick={() => navigate('/admin?tab=equipment')} className="nav-link">Equipment</button>
                <button type="button" onClick={() => navigate('/admin?tab=users')} className="nav-link">Users</button>
                <button type="button" onClick={() => navigate('/admin?tab=borrowed')} className="nav-link">Borrowed</button>
                <button type="button" onClick={() => navigate('/admin?tab=requests')} className="nav-link">Requests</button>
                <button type="button" onClick={() => navigate('/profile')} className="nav-link">Profile</button>
              </>
            ) : (
              <>
                <button type="button" onClick={() => navigate('/my-requests')} className="nav-link">My Requests</button>
                <button type="button" onClick={() => navigate('/profile')} className="nav-link">Profile</button>
              </>
            )}
            <button onClick={handleLogout} className="logout-btn">Logout</button>
          </nav>
        </div>
      </header>

      {/* Main Content */}
      <main className="detail-main">
        {/* Back Button */}
        <button className="back-button" onClick={() => navigate('/dashboard')}>
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <path d="M19 12H5M12 19l-7-7 7-7"/>
          </svg>
          Back to Catalog
        </button>

        {/* Equipment Detail Card */}
        <div className="detail-card">
          {/* Image Section */}
          <div className="detail-image-section">
            <div className="detail-image-placeholder">
              {equipment.name.charAt(0)}
            </div>
          </div>

          {/* Status Badge */}
          <div className="detail-info-section">
            <span className={`detail-status-badge ${equipment.status === 'Available' ? 'available' : 'in-use'}`}>
              {toDisplayStatus(equipment.status)}
            </span>

            <h1 className="detail-title">{equipment.name}</h1>
            
            <p className="availability-text">
              {equipment.status === 'AVAILABLE' 
                ? 'This equipment is available for borrowing' 
                : 'This equipment is currently in use'}
            </p>

            <div className="detail-location">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                <circle cx="12" cy="10" r="3"></circle>
              </svg>
              <span>{equipment.location}</span>
            </div>

            <p className="detail-description">{equipment.description}</p>

            <div className="specifications-section">
              <h3>Technical Specifications</h3>
              <ul className="specifications-list">
                {(Array.isArray(equipment.specifications) ? equipment.specifications : []).map((spec, index) => (
                  <li key={index}>{spec}</li>
                ))}
              </ul>
            </div>

            <button 
              className="request-button"
              disabled={equipment.status !== 'AVAILABLE'}
              onClick={() => navigate('/my-requests', {
                state: {
                  equipmentName: equipment.name,
                  category: equipment.category,
                  quantity: 1
                }
              })}
            >
              Request to Borrow
            </button>
          </div>
        </div>
      </main>
    </div>
  );
}

export default React.memo(EquipmentDetail);
