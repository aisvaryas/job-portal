import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { jobService } from '../../services/jobService';
import '../../styles/ApplicantsList.css';

function ApplicantsList() {
  const { id } = useParams();
  const [applicants, setApplicants] = useState([]);
  const [filterStatus, setFilterStatus] = useState('all');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchApplicants = async () => {
      setLoading(true);
      setError('');
      try {
        const all = await jobService.getApplicationsByJob(id);
        setApplicants(
          filterStatus === 'all'
            ? all
            : all.filter((app) => (app.status || '').toLowerCase() === filterStatus)
        );
      } catch (err) {
        setError(err?.response?.data?.message || 'Unable to load applicants.');
      } finally {
        setLoading(false);
      }
    };
    fetchApplicants();
  }, [id, filterStatus]);

  if (loading) return <div className="loading">Loading applicants...</div>;

  return (
    <div className="page-wrapper">
      <Header />
      <div className="applicants-container">
        <h1>Applicants</h1>

        <div className="filter-section">
          <label>Filter by Status:</label>
          <select value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
            <option value="all">All</option>
            <option value="applied">Applied</option>
            <option value="shortlisted">Shortlisted</option>
            <option value="interview">Interview</option>
            <option value="rejected">Rejected</option>
            <option value="hired">Hired</option>
          </select>
        </div>

        <div className="applicants-list">
          {error && <p className="error-message">{error}</p>}
          {applicants.length === 0 ? (
            <p>No applicants yet for this job.</p>
          ) : (
            applicants.map((applicant) => (
              <div key={applicant.id} className="applicant-card">
                <div className="applicant-info">
                  <h3>{applicant.applicantName}</h3>
                  <p className="title">{applicant.seekerHeadline || applicant.jobTitle}</p>
                  <span className={`status ${applicant.status}`}>{applicant.status}</span>
                </div>
                <div className="applicant-actions">
                  <Link to={`/applicant/${applicant.id}`} className="btn-secondary">
                    View Applicant
                  </Link>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default ApplicantsList;
