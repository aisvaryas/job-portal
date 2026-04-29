import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { applicationService } from '../../services/applicationService';
import '../../styles/ApplicationDetails.css';

function ApplicationDetails() {
  const { id } = useParams();
  const [application, setApplication] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchApplication = async () => {
      setLoading(true);
      setError('');
      try {
        setApplication(await applicationService.getApplicationById(id));
      } catch (err) {
        setError(err?.response?.data?.message || 'Unable to load application details.');
      } finally {
        setLoading(false);
      }
    };
    fetchApplication();
  }, [id]);

  if (loading) return <div className="loading">Loading application details...</div>;

  return (
    <div className="page-wrapper">
      <Header />
      <div className="application-details-container">
        <Link to="/applications" className="back-link">Back to Applications</Link>

        {application ? (
          <div className="application-detail">
            <h1>{application.jobTitle}</h1>

            <div className="job-info">
              <p><strong>Company:</strong> {application.companyName}</p>
              <p><strong>Location:</strong> {application.location || 'N/A'}</p>
              <p><strong>Salary:</strong> {application.salaryMin || 0} - {application.salaryMax || 0}</p>
              <p><strong>Experience Required:</strong> {application.experienceLevel || 'N/A'}</p>
              <p><strong>Applied on:</strong> {new Date(application.appliedAt).toLocaleDateString()}</p>
              <p><strong>Status:</strong> <span className="status">{application.status}</span></p>
            </div>

            <section>
              <h2>Description</h2>
              <p>{application.description || 'No description provided.'}</p>
            </section>

            <section>
              <h2>Requirements</h2>
              <p>{application.requirements || 'No requirements provided.'}</p>
            </section>

            <section>
              <h2>Resume Submitted</h2>
              {application.resumeFileUrl ? (
                <a href={application.resumeFileUrl} target="_blank" rel="noopener noreferrer" className="btn-secondary">
                  Open {application.resumeFileName || 'Resume'}
                </a>
              ) : (
                <p>No resume link available.</p>
              )}
            </section>
          </div>
        ) : (
          <p>{error || 'Application not found'}</p>
        )}
      </div>
      <Footer />
    </div>
  );
}

export default ApplicationDetails;
