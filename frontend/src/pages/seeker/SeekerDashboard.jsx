import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { applicationService } from '../../services/applicationService';
import '../../styles/Dashboard.css';

function SeekerDashboard() {
  const navigate = useNavigate();
  const [applications, setApplications] = useState([]);

  const [stats, setStats] = useState({
    totalApplications: 0,
    underReview: 0,
    shortlisted: 0,
    interviews: 0,
  });

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadApps = async () => {
      setLoading(true);
      const appList = await applicationService.getMyApplications();
      const mapped = appList.map((app) => ({
        id: app.id,
        company: app.companyName || 'Company',
        jobRole: app.jobTitle,
        appliedDate: app.appliedAt,
        status: app.status || 'APPLIED',
      }));
      setApplications(mapped);
      setStats({
        totalApplications: mapped.length,
        underReview: mapped.filter((a) => a.status === 'UNDER_REVIEW').length,
        shortlisted: mapped.filter((a) => a.status === 'SHORTLISTED').length,
        interviews: mapped.filter((a) => a.status === 'INTERVIEW').length,
      });
      setLoading(false);
    };
    loadApps();
  }, []);

  const getStatusBadgeClass = (status) => {
    const statusMap = {
      APPLIED: 'status-applied',
      UNDER_REVIEW: 'status-under-review',
      SHORTLISTED: 'status-shortlisted',
      INTERVIEW: 'status-interview',
      REJECTED: 'status-rejected',
      HIRED: 'status-shortlisted',
    };
    return statusMap[status] || '';
  };

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
  };

  const handleViewDetails = (appId) => {
    navigate(`/application/${appId}`);
  };

  if (loading) return <div className="loading">Loading dashboard...</div>;

  return (
    <div className="page-wrapper">
      <Header />
      <div className="dashboard-container">
        <div className="dashboard-header">
          <h1>My Dashboard</h1>
        </div>

        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-value">{stats.totalApplications}</div>
            <div className="stat-label">Total Applications</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">{stats.underReview}</div>
            <div className="stat-label">Under Review</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">{stats.shortlisted}</div>
            <div className="stat-label">Shortlisted</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">{stats.interviews}</div>
            <div className="stat-label">Interviews</div>
          </div>
        </div>

        <section className="applications-section">
          <div className="section-header">
            <h2>My Applications</h2>
            <p className="section-subtitle">Track your job applications and their status</p>
          </div>

          {applications.length === 0 ? (
            <div className="empty-state">
              <p>No applications yet. Start exploring jobs!</p>
            </div>
          ) : (
            <div className="applications-table-wrapper">
              <table className="applications-table">
                <thead>
                  <tr>
                    <th>Company Name</th>
                    <th>Job Role</th>
                    <th>Applied Date</th>
                    <th>Status</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {applications.map((app) => (
                    <tr key={app.id}>
                      <td className="company-name">{app.company}</td>
                      <td>{app.jobRole || app.jobTitle}</td>
                      <td>{formatDate(app.appliedDate)}</td>
                      <td>
                        <span className={`status-badge ${getStatusBadgeClass(app.status)}`}>
                          {app.status}
                        </span>
                      </td>
                      <td>
                        <button 
                          className="btn-action"
                          onClick={() => handleViewDetails(app.id)}
                        >
                          View Details
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </div>
      <Footer />
    </div>
  );
}

export default SeekerDashboard;
