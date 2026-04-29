import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { applicationService } from '../../services/applicationService';
import '../../styles/ApplicantDetails.css';

function ApplicantDetails() {
  const { id } = useParams();
  const [applicant, setApplicant] = useState(null);
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchApplicant();
  }, [id]);

  const fetchApplicant = async () => {
    setLoading(true);
    setError('');
    try {
      setApplicant(await applicationService.getApplicationById(id));
    } catch (err) {
      setError(err?.response?.data?.message || 'Unable to load applicant details.');
    } finally {
      setLoading(false);
    }
  };

  const handleStatusChange = async (newStatus) => {
    setUpdating(true);
    setError('');
    try {
      const updated = await applicationService.updateApplicationStatus(id, newStatus);
      setApplicant(updated);
    } catch (err) {
      setError(err?.response?.data?.message || 'Unable to update application status.');
    } finally {
      setUpdating(false);
    }
  };

  if (loading) return <div className="loading">Loading applicant details...</div>;

  return (
    <div className="page-wrapper">
      <Header />
      <div className="applicant-details-container">
        <Link to={applicant?.jobId ? `/job/${applicant.jobId}/applicants` : '/dashboard/employer'} className="back-link">
          Back to Applicants
        </Link>

        {applicant ? (
          <div className="applicant-detail">
            <h1>{applicant.applicantName}</h1>
            <p className="title">{applicant.seekerHeadline || `Applied for ${applicant.jobTitle}`}</p>
            {error && <p className="error-message">{error}</p>}

            <div className="applicant-overview">
              <div className="overview-item">
                <strong>Email:</strong>
                <span>{applicant.seekerEmail || applicant.email}</span>
              </div>
              <div className="overview-item">
                <strong>Phone:</strong>
                <span>{applicant.seekerPhone || applicant.phoneNumber || 'N/A'}</span>
              </div>
              <div className="overview-item">
                <strong>Location:</strong>
                <span>{applicant.seekerLocation || 'N/A'}</span>
              </div>
              <div className="overview-item">
                <strong>Applied Date:</strong>
                <span>{new Date(applicant.appliedAt).toLocaleDateString()}</span>
              </div>
              <div className="overview-item">
                <strong>Status:</strong>
                <span>{applicant.status}</span>
              </div>
            </div>

            <section>
              <h2>Resume</h2>
              {applicant.resumeFileUrl ? (
                <a href={applicant.resumeFileUrl} target="_blank" rel="noopener noreferrer" className="btn-secondary">
                  Open {applicant.resumeFileName || 'Resume'}
                </a>
              ) : (
                <p>No resume available.</p>
              )}
            </section>

            <section>
              <h2>Skills</h2>
              <div className="skills-list">
                {(applicant.skills || []).length > 0 ? (
                  applicant.skills.map((skill) => <span key={skill} className="skill-badge">{skill}</span>)
                ) : (
                  <p>No skills listed.</p>
                )}
              </div>
            </section>

            <section>
              <h2>Experience</h2>
              {(applicant.experience || []).length > 0 ? (
                <ul>
                  {applicant.experience.map((item) => <li key={item}>{item}</li>)}
                </ul>
              ) : (
                <p>No experience listed.</p>
              )}
            </section>

            <section>
              <h2>Application Status</h2>
              <div className="status-actions">
                <button className="btn-secondary" disabled={updating} onClick={() => handleStatusChange('SHORTLISTED')} type="button">
                  Shortlist
                </button>
                <button className="btn-secondary" disabled={updating} onClick={() => handleStatusChange('REJECTED')} type="button">
                  Reject
                </button>
                <button className="btn-primary" disabled={updating} onClick={() => handleStatusChange('HIRED')} type="button">
                  Hire
                </button>
              </div>
            </section>

            <section>
              <h2>View Full Profile</h2>
              <Link to={`/job-seeker/${applicant.seekerId}`} className="btn-primary">
                View Public Profile
              </Link>
            </section>
          </div>
        ) : (
          <p>{error || 'Applicant not found'}</p>
        )}
      </div>
      <Footer />
    </div>
  );
}

export default ApplicantDetails;
