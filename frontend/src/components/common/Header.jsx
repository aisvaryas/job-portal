import { useState, useEffect } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import { notificationService } from '../../services/notificationService';
import '../../styles/Header.css';

function Header() {
  const navigate = useNavigate();
  const { logout, userRole, user } = useAuth();
  const [notificationOpen, setNotificationOpen] = useState(false);
  const [profileMenuOpen, setProfileMenuOpen] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    if (!user?.email) return undefined;

    loadNotifications();
    const interval = setInterval(loadNotifications, 30000);
    return () => clearInterval(interval);
  }, [user?.email]);

  const loadNotifications = async () => {
    try {
      const notifs = await notificationService.getUnreadNotifications();
      setNotifications(notifs);
      setUnreadCount(notifs.length);
    } catch (error) {
      console.error('Failed to load notifications:', error);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleViewProfile = () => {
    navigate(userRole === 'SEEKER' ? '/profile' : '/company-profile');
    setProfileMenuOpen(false);
  };

  const handleEditProfile = () => {
    navigate(userRole === 'SEEKER' ? '/profile' : '/company-profile');
    setProfileMenuOpen(false);
  };

  const handleNotificationClick = async (notification) => {
    if (!notification.isRead) {
      await notificationService.markAsRead(notification.id);
      await loadNotifications();
    }
    setNotificationOpen(false);
  };

  const dashboardPath = userRole === 'SEEKER' ? '/dashboard/seeker' : '/dashboard/employer';

  return (
    <header className="header">
      <div className="header-container">
        <div className="header-left">
          <Link to={dashboardPath} className="header-logo">
            JobPortal
          </Link>
        </div>

        <nav className="header-nav" aria-label="Dashboard navigation">
          {userRole === 'SEEKER' && (
            <ul className="nav-links">
              <li>
                <NavLink to="/jobs" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                  Search Jobs
                </NavLink>
              </li>
              <li>
                <NavLink to="/dashboard/seeker" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                  Dashboard
                </NavLink>
              </li>
              <li>
                <NavLink to="/applications" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                  Applications
                </NavLink>
              </li>
              <li>
                <NavLink to="/saved-jobs" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                  Saved
                </NavLink>
              </li>
            </ul>
          )}

          {userRole === 'EMPLOYER' && (
            <ul className="nav-links">
              <li>
                <NavLink to="/dashboard/employer" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                  Dashboard
                </NavLink>
              </li>
              <li>
                <NavLink to="/jobs/create" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                  Post Job
                </NavLink>
              </li>
              <li>
                <NavLink to="/company-profile" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
                  Company
                </NavLink>
              </li>
            </ul>
          )}
        </nav>

        <div className="header-actions">
          <div className="notification-container">
            <button
              className="notification-btn"
              onClick={() => setNotificationOpen(!notificationOpen)}
              title="Notifications"
              type="button"
            >
              <span className="notification-icon">!</span>
              {unreadCount > 0 && <span className="notification-badge">{unreadCount}</span>}
            </button>

            {notificationOpen && (
              <div className="notification-dropdown">
                {notifications.length === 0 ? (
                  <div className="no-notifications">
                    <p>No new notifications</p>
                  </div>
                ) : (
                  notifications.map((notif) => (
                    <button
                      key={notif.id}
                      className="notification-item"
                      onClick={() => handleNotificationClick(notif)}
                      type="button"
                    >
                      <span className="notification-indicator" style={{ backgroundColor: getNotificationColor(notif.type) }} />
                      <span className="notification-content">
                        <span>{notif.message}</span>
                        <small className="time">{formatTime(notif.createdAt)}</small>
                      </span>
                    </button>
                  ))
                )}
              </div>
            )}
          </div>

          <div className="profile-menu">
            <button
              className="profile-btn"
              onClick={() => setProfileMenuOpen(!profileMenuOpen)}
              title="Profile"
              type="button"
            >
              <span className="profile-icon">{(user?.email || 'U').slice(0, 1).toUpperCase()}</span>
            </button>

            {profileMenuOpen && (
              <div className="profile-dropdown">
                <button onClick={handleViewProfile} className="profile-option" type="button">
                  View Profile
                </button>
                <button onClick={handleEditProfile} className="profile-option" type="button">
                  Edit Profile
                </button>
              </div>
            )}
          </div>

          <button className="logout-btn" onClick={handleLogout} type="button">
            Logout
          </button>
        </div>
      </div>
    </header>
  );
}

function getNotificationColor(type) {
  const colors = {
    SHORTLISTED: '#10b981',
    REJECTED: '#ef4444',
    HIRED: '#8b5cf6',
    NEW_APPLICATION: '#2563eb',
    APPLIED: '#2563eb',
    APPLICATION: '#2563eb',
  };
  return colors[type] || '#6b7280';
}

function formatTime(dateString) {
  const date = new Date(dateString);
  const now = new Date();
  const diff = now - date;
  const minutes = Math.floor(diff / 60000);
  const hours = Math.floor(diff / 3600000);
  const days = Math.floor(diff / 86400000);

  if (minutes < 1) return 'just now';
  if (minutes < 60) return `${minutes} min ago`;
  if (hours < 24) return `${hours}h ago`;
  if (days < 7) return `${days}d ago`;
  return date.toLocaleDateString();
}

export default Header;
