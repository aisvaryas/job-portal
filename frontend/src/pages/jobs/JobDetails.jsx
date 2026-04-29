import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import Navbar from '../../components/common/Navbar';
import Footer from '../../components/common/Footer';
import { jobService } from '../../services/jobService';
import '../../styles/JobDetails.css';

function JobDetails() {
  const { id } = useParams();
  const [job, setJob] = useState(null);
  const [saved, setSaved] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchJob = async () => {
      setLoading(true);
      setError('');
      try {
        const jobData = await jobService.getJobById(id);
        setJob(jobData);
      } catch (err) {
        setError(err?.response?.data?.message || 'Unable to load job details.');
      } finally {
        setLoading(false);
      }
    };
    fetchJob();
  }, [id]);

  const renderLines = (value) =>
    (value || '')
      .split(/\n|;/)
      .map((line) => line.trim())
      .filter(Boolean);

  if (loading) return <div className="loading">Loading job details...</div>;

  return (
    <div className="page-wrapper">
      <Navbar />
      <div className="job-details-container">
        <Link to="/jobs" className="back-link">Back to Jobs</Link>

        {job ? (
          <div className="job-detail">
            <div className="job-header">
              <div>
                <h1>{job.title}</h1>
                <p className="company">{job.companyName || job.employer?.companyName || 'Company'}</p>
              </div>
              <button
                className={`save-btn ${saved ? 'saved' : ''}`}
                onClick={() => setSaved(!saved)}
                type="button"
              >
                {saved ? 'Saved' : 'Save'}
              </button>
            </div>

            <div className="job-meta">
              <span className="meta-item">
                <span className="label">Location:</span>
                <span className="value">{job.location || 'N/A'}</span>
              </span>
              <span className="meta-item">
                <span className="label">Salary:</span>
                <span className="value">{job.salaryMin || 0} - {job.salaryMax || 0}</span>
              </span>
              <span className="meta-item">
                <span className="label">Type:</span>
                <span className="value">{job.jobType || 'N/A'}</span>
              </span>
              <span className="meta-item">
                <span className="label">Experience:</span>
                <span className="value">{job.experienceLevel || 'N/A'}</span>
              </span>
            </div>

            <section className="job-section">
              <h2>Description</h2>
              <p className="description">{job.description || 'No description provided.'}</p>
            </section>

            <section className="job-section">
              <h2>Requirements</h2>
              {renderLines(job.requirements).length > 0 ? (
                <ul className="requirements">
                  {renderLines(job.requirements).map((req) => (
                    <li key={req}>{req}</li>
                  ))}
                </ul>
              ) : (
                <p>No requirements provided.</p>
              )}
            </section>

            <section className="job-section">
              <h2>Skills Required</h2>
              <div className="skills-list">
                {(job.skillsRequired || '')
                  .split(',')
                  .map((skill) => skill.trim())
                  .filter(Boolean)
                  .map((skill) => (
                    <span key={skill} className="skill-badge">{skill}</span>
                  ))}
                {!job.skillsRequired && <p>No skills listed.</p>}
              </div>
            </section>

            <section className="job-section">
              <h2>Benefits</h2>
              {renderLines(job.benefits).length > 0 ? (
                <ul className="benefits">
                  {renderLines(job.benefits).map((benefit) => (
                    <li key={benefit}>{benefit}</li>
                  ))}
                </ul>
              ) : (
                <p>No benefits listed.</p>
              )}
            </section>

            <section className="job-section">
              <h2>About Company</h2>
              <div className="company-info">
                <p>{job.aboutCompany || job.employer?.industry || 'Company details are not available yet.'}</p>
                {job.employer?.website && (
                  <p>
                    Website:{' '}
                    <a href={job.employer.website} target="_blank" rel="noopener noreferrer">
                      {job.employer.website}
                    </a>
                  </p>
                )}
              </div>
            </section>

            <div className="job-actions">
              <Link to={`/jobs/apply/${job.id}`} className="btn-primary">
                Apply Now
              </Link>
              <button className="btn-secondary" type="button" onClick={() => setSaved(true)}>
                Save Job
              </button>
            </div>
          </div>
        ) : (
          <p>{error || 'Job not found'}</p>
        )}
      </div>
      <Footer />
    </div>
  );
}

export default JobDetails;
