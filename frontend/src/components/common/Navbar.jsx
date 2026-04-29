import { Link } from 'react-router-dom';
import '../../styles/Navbar.css';

function Navbar() {
  return (
    <nav className="navbar">
      <div className="nav-container">
        <Link to="/" className="nav-logo">
          JobPortal
        </Link>
        <ul className="nav-menu">
          <li className="nav-item">
            <Link to="/jobs" className="nav-link">
              Browse Jobs
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/login" className="nav-link">
              Login
            </Link>
          </li>
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
