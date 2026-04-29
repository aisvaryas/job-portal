import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import '../../styles/SavedJobs.css';

function SavedJobs() {
  const [savedJobs, setSavedJobs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // TODO: Fetch saved jobs from API
    setTimeout(() => setLoading(false), 1000);
  }, []);

  const handleRemoveSavedJob = (jobId) => {
    // TODO: Remove from saved jobs
    setSavedJobs(savedJobs.filter(job => job.id !== jobId));
  };

  if (loading) return <div className="loading">Loading saved jobs...</div>;

  return (
    <div className="page-wrapper">
      <Header />
      <div className="saved-jobs-container">
        <h1>Saved Jobs</h1>

        <div className="saved-jobs-list">
          {savedJobs.length === 0 ? (
            <div className="no-data">
              <p>No saved jobs yet. Start saving your favorite jobs!</p>
              <Link to="/jobs" className="btn-primary">Browse Jobs</Link>
            </div>
          ) : (
            savedJobs.map((job) => (
              <div key={job.id} className="saved-job-card">
                <h3>{job.title}</h3>
                <p className="company">{job.company}</p>
                <p className="location">{job.location}</p>
                <p className="salary">${job.salary}</p>
                <div className="actions">
                  <Link to={`/jobs/${job.id}`} className="btn-secondary">
                    View Job
                  </Link>
                  <Link to={`/jobs/apply/${job.id}`} className="btn-primary">
                    Apply Now
                  </Link>
                  <button 
                    className="btn-danger"
                    onClick={() => handleRemoveSavedJob(job.id)}
                  >
                    Remove
                  </button>
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

export default SavedJobs;
