import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { profileService } from '../../services/profileService';
import { jobService } from '../../services/jobService';
import '../../styles/Dashboard.css';

function EmployerDashboard() {
  const navigate = useNavigate();
  const [stats, setStats] = useState({
    totalJobsPosted: 0,
    activeJobs: 0,
    applicationsReceived: 0,
    shortlistedCandidates: 0,
  });
  const [jobs, setJobs] = useState([]);
  const [recentApplicants, setRecentApplicants] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    setError('');
    try {
      const employer = await profileService.getCurrentEmployerProfile();
      const employerJobs = await jobService.getEmployerJobs(employer.id);
      const applicantLists = await Promise.all(
        employerJobs.map((job) => jobService.getApplicationsByJob(job.id).catch(() => []))
      );
      const applicants = applicantLists.flat();

      setJobs(employerJobs);
      setRecentApplicants(applicants.slice(0, 6));
      setStats({
        totalJobsPosted: employerJobs.length,
        activeJobs: employerJobs.filter((job) => (job.status || '').toUpperCase() === 'OPEN').length,
        applicationsReceived: applicants.length,
        shortlistedCandidates: applicants.filter((app) => app.status === 'SHORTLISTED').length,
      });
    } catch (err) {
      setError(err?.response?.data?.message || 'Unable to load employer dashboard.');
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => new Date(dateString).toLocaleDateString();

  const getStatusBadge = (status) => ((status || '').toUpperCase() === 'OPEN' ? 'status-open' : 'status-closed');

  const updateJobStatus = async (jobId, nextStatus) => {
    try {
      if (nextStatus === 'CLOSED') {
        await jobService.closeJob(jobId);
      } else {
        await jobService.reopenJob(jobId);
      }
      await fetchData();
    } catch (err) {
      setError(err?.response?.data?.message || 'Unable to update job status.');
    }
  };

  const deleteJob = async (jobId) => {
    try {
      await jobService.deleteJob(jobId);
      await fetchData();
    } catch (err) {
      setError(err?.response?.data?.message || 'Unable to delete job.');
    }
  };

  if (loading) return <div className="loading">Loading dashboard...</div>;

  return (
    <div className="page-wrapper">
      <Header />
      <div className="dashboard-container">
        <div className="dashboard-header">
          <h1>Employer Dashboard</h1>
          {error && <p className="error-message">{error}</p>}
        </div>

        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-value">{stats.totalJobsPosted}</div>
            <div className="stat-label">Jobs Posted</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">{stats.activeJobs}</div>
            <div className="stat-label">Active Jobs</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">{stats.applicationsReceived}</div>
            <div className="stat-label">Applications</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">{stats.shortlistedCandidates}</div>
            <div className="stat-label">Shortlisted</div>
          </div>
        </div>

        <section className="applications-section">
          <div className="section-header">
            <h2>My Posted Jobs</h2>
            <p className="section-subtitle">Manage jobs and review applicants</p>
          </div>

          <div className="job-status-table-wrapper">
            <table className="job-status-table">
              <thead>
                <tr>
                  <th>Job Title</th>
                  <th>Status</th>
                  <th>Total Applicants</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {jobs.length === 0 ? (
                  <tr>
                    <td colSpan="4">No jobs posted yet.</td>
                  </tr>
                ) : (
                  jobs.map((job) => (
                    <tr key={job.id}>
                      <td className="job-title">{job.title}</td>
                      <td>
                        <span className={`status-badge ${getStatusBadge(job.status)}`}>{job.status}</span>
                      </td>
                      <td className="app-count">{job.applicantCount || 0}</td>
                      <td className="dashboard-actions">
                        <Link to={`/job/${job.id}/applicants`} className="btn-secondary">View Applicants</Link>
                        <button type="button" className="btn-secondary" onClick={() => navigate(`/jobs/create?edit=${job.id}`)}>Edit Job</button>
                        {(job.status || '').toUpperCase() === 'OPEN' ? (
                          <button type="button" className="btn-secondary" onClick={() => updateJobStatus(job.id, 'CLOSED')}>Close Job</button>
                        ) : (
                          <button type="button" className="btn-secondary" onClick={() => updateJobStatus(job.id, 'OPEN')}>Reopen Job</button>
                        )}
                        <button type="button" className="btn-danger" onClick={() => deleteJob(job.id)}>Delete Job</button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </section>

        <section className="applications-section">
          <div className="section-header">
            <h2>Recent Applicants</h2>
            <p className="section-subtitle">Latest candidates applying to your jobs</p>
          </div>

          {recentApplicants.length === 0 ? (
            <div className="empty-state">
              <p>No applications yet.</p>
            </div>
          ) : (
            <div className="applicants-grid">
              {recentApplicants.map((applicant) => (
                <div key={applicant.id} className="applicant-card">
                  <div className="applicant-avatar">{(applicant.applicantName || 'A').slice(0, 1)}</div>
                  <div className="applicant-header">
                    <h3>{applicant.applicantName}</h3>
                    <p className="job-title-small">{applicant.jobTitle}</p>
                  </div>
                  <div className="applicant-details">
                    <div className="detail-row">
                      <span className="label">Status:</span>
                      <span className="value">{applicant.status}</span>
                    </div>
                    <div className="detail-row">
                      <span className="label">Applied:</span>
                      <span className="value">{formatDate(applicant.appliedAt)}</span>
                    </div>
                  </div>
                  <Link to={`/applicant/${applicant.id}`} className="btn-view-profile">View Applicant</Link>
                </div>
              ))}
            </div>
          )}
        </section>
      </div>
      <Footer />
    </div>
  );
}

export default EmployerDashboard;
