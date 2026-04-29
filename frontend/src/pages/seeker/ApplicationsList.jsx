import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { applicationService } from '../../services/applicationService';
import '../../styles/Applications.css';

function ApplicationsList() {
  const [applications, setApplications] = useState([]);
  const [filterStatus, setFilterStatus] = useState('all');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadApplications = async () => {
      setLoading(true);
      const all = await applicationService.getMyApplications();
      const mapped = all.map((app) => ({
        id: app.id,
        jobTitle: app.jobTitle,
        company: app.companyName || 'Company',
        status: app.status,
        appliedDate: app.appliedAt,
      }));
      const filtered = filterStatus === 'all'
        ? mapped
        : mapped.filter((app) => app.status.toLowerCase() === filterStatus.toLowerCase());
      setApplications(filtered);
      setLoading(false);
    };
    loadApplications();
  }, [filterStatus]);

  if (loading) return <div className="loading">Loading applications...</div>;

  return (
    <div className="page-wrapper">
      <Header />
      <div className="applications-container">
        <h1>My Applications</h1>

        <div className="filter-section">
          <label>Filter by Status:</label>
          <select 
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value)}
          >
            <option value="all">All</option>
            <option value="applied">Applied</option>
            <option value="reviewing">Reviewing</option>
            <option value="shortlisted">Shortlisted</option>
            <option value="hired">Hired</option>
            <option value="rejected">Rejected</option>
          </select>
        </div>

        <div className="applications-list">
          {applications.length === 0 ? (
            <div className="no-data">
              <p>No applications yet. Start exploring jobs!</p>
              <Link to="/jobs" className="btn-primary">Browse Jobs</Link>
            </div>
          ) : (
            applications.map((app) => (
              <div key={app.id} className="application-card">
                <h3>{app.jobTitle}</h3>
                <p>{app.company}</p>
                <span className={`status ${app.status}`}>{app.status}</span>
                <p className="date">Applied on {app.appliedDate}</p>
                <Link to={`/application/${app.id}`} className="btn-secondary">
                  View Details
                </Link>
              </div>
            ))
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default ApplicationsList;
