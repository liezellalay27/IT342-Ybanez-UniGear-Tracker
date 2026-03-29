import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCurrentUser, logout } from '../services/authService';
import './HomepageCatalog.css';
import logo from '../assets/UniGear Symbol.png';

const API_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

function HomepageCatalog() {
  const navigate = useNavigate();
  const currentUser = getCurrentUser();
  const isAdmin = currentUser?.role === 'ADMIN';
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [equipment, setEquipment] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadEquipment = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/login', { replace: true });
        return;
      }

      setLoading(true);
      setError('');

      try {
        const response = await fetch(`${API_URL}/equipment`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

        if (response.status === 401) {
          logout();
          navigate('/login', { replace: true });
          return;
        }

        if (!response.ok) {
          const message = await response.text();
          throw new Error(message || 'Failed to load equipment');
        }

        const data = await response.json();
        setEquipment(Array.isArray(data) ? data : []);
      } catch (err) {
        setError(err.message || 'Error connecting to server');
      } finally {
        setLoading(false);
      }
    };

    loadEquipment();
  }, [navigate]);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const categories = [
    'All',
    ...Array.from(new Set(equipment.map((item) => item.category))).sort((a, b) => a.localeCompare(b))
  ];

  const toDisplayStatus = (status) => {
    if (!status) {
      return 'Unknown';
    }
    return status.replace('_', ' ').replace(/\b\w/g, (char) => char.toUpperCase());
  };

  // Filter equipment based on search query and selected category
  const filteredEquipment = equipment.filter((item) => {
    const matchesSearch = item.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         item.category.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = selectedCategory === 'all' || 
                           item.category.toLowerCase() === selectedCategory.toLowerCase();
    return matchesSearch && matchesCategory;
  });

  if (loading) {
    return <div className="loading">Loading equipment...</div>;
  }

  return (
    <div className="homepage-container">
      {/* Navigation Header */}
      <header className="homepage-header">
        <div className="header-content">
          <div className="logo-section">
            <img src={logo} alt="Logo" className="header-logo" />
            <span className="header-title">UniGear Tracker</span>
          </div>
          <nav className="nav-links">
            <button onClick={() => navigate('/dashboard')} className="nav-link active">Catalog</button>
            {isAdmin ? (
              <>
                <button onClick={() => navigate('/admin?tab=equipment')} className="nav-link">Equipment</button>
                <button onClick={() => navigate('/admin?tab=users')} className="nav-link">Users</button>
                <button onClick={() => navigate('/admin?tab=borrowed')} className="nav-link">Borrowed</button>
                <button onClick={() => navigate('/admin?tab=requests')} className="nav-link">Requests</button>
                <button onClick={() => navigate('/profile')} className="nav-link">Profile</button>
              </>
            ) : (
              <>
                <button onClick={() => navigate('/my-requests')} className="nav-link">My Requests</button>
                <button onClick={() => navigate('/profile')} className="nav-link">Profile</button>
              </>
            )}
            <button onClick={handleLogout} className="logout-btn">Logout</button>
          </nav>
        </div>
      </header>

      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-content">
          <div className="hero-text">
            <h1 className="hero-title">University Equipment Catalog</h1>
            <p className="hero-subtitle">Browse and borrow laboratory equipment for your research and projects</p>
            
            {/* Search Bar */}
            <div className="search-container">
              <input
                type="text"
                className="search-input"
                placeholder="Search for equipment..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
              <button className="search-button" aria-label="Search">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <circle cx="11" cy="11" r="8"></circle>
                  <path d="m21 21-4.35-4.35"></path>
                </svg>
              </button>
            </div>

            {/* Quick Action Buttons */}
            <div className="quick-actions">
              <button className="action-btn" onClick={() => window.scrollTo({ top: 420, behavior: 'smooth' })}>Browse Equipment</button>
              <button className="action-btn" onClick={() => navigate('/my-requests')}>My Requests</button>
              <button className="action-btn" onClick={() => navigate('/my-requests')}>Request History</button>
            </div>
          </div>
          
          <div className="hero-logo">
            <img src={logo} alt="University Logo" className="u-logo" />
          </div>
        </div>
      </section>

      {/* Main Content */}
      <main className="catalog-main">
        {error && <div className="error-message">{error}</div>}

        {/* Category Filter */}
        <div className="filter-section">
          <div className="filter-header">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"></polygon>
            </svg>
            <span className="filter-title">Filter by Category</span>
          </div>
          <div className="category-filters">
            {categories.map((category) => (
              <button
                key={category}
                className={`category-btn ${selectedCategory === category.toLowerCase() ? 'active' : ''}`}
                onClick={() => setSelectedCategory(category.toLowerCase())}
              >
                {category}
              </button>
            ))}
          </div>
        </div>

        {/* Equipment Grid */}
        <div className="equipment-grid">
          {filteredEquipment.length > 0 ? (
            filteredEquipment.map((item) => (
              <div 
                key={item.id} 
                className="equipment-card"
                onClick={() => navigate(`/equipment/${item.id}`)}
              >
                <div className="equipment-image">
                  <div className="image-placeholder">
                    {item.name.charAt(0)}
                  </div>
                </div>
                <div className="equipment-info">
                  <h3 className="equipment-name">{item.name}</h3>
                  <p className="equipment-category">{item.category}</p>
                  <span className={`equipment-status ${item.status === 'AVAILABLE' ? 'available' : 'in-use'}`}>
                    {toDisplayStatus(item.status)}
                  </span>
                </div>
              </div>
            ))
          ) : (
            <div className="no-results">
              <p>No equipment found matching your search criteria.</p>
            </div>
          )}
        </div>
      </main>
    </div>
  );
}

export default HomepageCatalog;
