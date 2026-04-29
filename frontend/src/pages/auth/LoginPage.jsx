import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import Navbar from '../../components/common/Navbar';
import '../../styles/Auth.css';

function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [userType, setUserType] = useState('JOB_SEEKER');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const session = await login(email, password, userType);
      const normalizedRole = session.userRole === 'JOB_SEEKER' ? 'SEEKER' : session.userRole;
      if (normalizedRole === 'SEEKER') {
        navigate('/profile');
      } else {
        navigate('/company-profile');
      }
    } catch (err) {
      setError(err.message || 'Login failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Navbar />
      <div className="auth-container">
        <div className="auth-card">
          <h2>Login to JobPortal</h2>

          <div className="user-type-toggle">
            <button
              type="button"
              className={`toggle-btn ${userType === 'JOB_SEEKER' ? 'active' : ''}`}
              onClick={() => setUserType('JOB_SEEKER')}
            >
              Job Seeker
            </button>
            <button
              type="button"
              className={`toggle-btn ${userType === 'EMPLOYER' ? 'active' : ''}`}
              onClick={() => setUserType('EMPLOYER')}
            >
              Employer
            </button>
          </div>

          <form onSubmit={handleSubmit}>
            {error && <div className="error-message">{error}</div>}

            <div className="form-group">
              <label htmlFor="email">Email Address</label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                placeholder="you@example.com"
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                placeholder="Enter your password"
              />
            </div>

            <div className="form-options">
              <label>
                <input type="checkbox" />
                Remember me
              </label>
              <Link to="/forgot-password">Forgot password?</Link>
            </div>

            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Logging in...' : 'Login'}
            </button>
          </form>

          <div className="auth-footer">
            Don't have an account?{' '}
            <Link to="/register">Register here</Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
