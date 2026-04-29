import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Footer from '../../components/common/Footer';
import Navbar from '../../components/common/Navbar';
import { jobService } from '../../services/jobService';
import '../../styles/JobsListing.css';

function JobsListing() {
  const [allJobs, setAllJobs] = useState([]);
  const [filteredJobs, setFilteredJobs] = useState([]);
  const [filters, setFilters] = useState({ location: '', experience: '' });
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [savedJobs, setSavedJobs] = useState([]);

  useEffect(() => {
    fetchJobs();
  }, []);

  useEffect(() => {
    filterJobs();
  }, [filters, searchTerm, allJobs]);

  const fetchJobs = async () => {
    setLoading(true);
    setError('');
    try {
      const jobs = await jobService.getAllJobs();
      setAllJobs(jobs);
      setFilteredJobs(jobs);
    } catch (err) {
      setError(err?.response?.data?.message || 'Unable to load jobs.');
    } finally {
      setLoading(false);
    }
  };

  const filterJobs = () => {
    let results = allJobs;
    const term = searchTerm.trim().toLowerCase();

    if (term) {
      results = results.filter((job) =>
        (job.title || '').toLowerCase().includes(term) ||
        (job.companyName || job.employer?.companyName || '').toLowerCase().includes(term) ||
        (job.skillsRequired || job.requirements || '').toLowerCase().includes(term)
      );
    }

    if (filters.location) {
      results = results.filter((job) =>
        (job.location || '').toLowerCase().includes(filters.location.toLowerCase())
      );
    }

    if (filters.experience) {
      results = results.filter((job) => normalizeExperience(job.experienceLevel) === filters.experience);
    }

    setFilteredJobs(results);
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const toggleSaveJob = (jobId) => {
    setSavedJobs((prev) =>
      prev.includes(jobId) ? prev.filter((id) => id !== jobId) : [...prev, jobId]
    );
  };

  const normalizeExperience = (exp) => {
    const value = (exp || '').toLowerCase();
    if (value.includes('entry')) return 'entry';
    if (value.includes('senior')) return 'senior';
    if (value.includes('mid')) return 'mid';
    return value;
  };

  const getExperienceLabel = (exp) => {
    const labels = {
      ENTRY: 'Entry Level',
      MID: 'Mid Level',
      SENIOR: 'Senior Level',
      entry: 'Entry Level',
      mid: 'Mid Level',
      senior: 'Senior Level',
    };
    return labels[exp] || exp || 'Not specified';
  };

  if (loading) return <div className="loading">Loading jobs...</div>;

  return (
    <div className="page-wrapper">
      <Navbar />
      <div className="jobs-listing-container">
        <div className="listing-header">
          <h1>Job Opportunities</h1>
          <p>Find your next career opportunity</p>
        </div>

        <div className="search-filter-section">
          <div className="search-bar">
            <input
              type="text"
              placeholder="Search by title, company, or skills..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
            <button className="btn-primary" type="button">
              Search
            </button>
          </div>

          <div className="filters">
            <input
              type="text"
              name="location"
              placeholder="Filter by location..."
              value={filters.location}
              onChange={handleFilterChange}
              className="filter-input"
            />
            <select
              name="experience"
              value={filters.experience}
              onChange={handleFilterChange}
              className="filter-select"
            >
              <option value="">All Experience Levels</option>
              <option value="entry">Entry Level</option>
              <option value="mid">Mid Level</option>
              <option value="senior">Senior Level</option>
            </select>
            {(searchTerm || filters.location || filters.experience) && (
              <button
                className="btn-clear"
                type="button"
                onClick={() => {
                  setSearchTerm('');
                  setFilters({ location: '', experience: '' });
                }}
              >
                Clear Filters
              </button>
            )}
          </div>
        </div>

        <div className="jobs-results">
          <div className="results-count">
            {filteredJobs.length > 0 ? (
              <p>
                Showing <strong>{filteredJobs.length}</strong> job
                {filteredJobs.length !== 1 ? 's' : ''}
              </p>
            ) : (
              <p>{error || 'No jobs found matching your criteria'}</p>
            )}
          </div>

          <div className="jobs-grid">
            {filteredJobs.length === 0 ? (
              <div className="no-jobs">
                <p>Try adjusting your search or filters to find the right job.</p>
              </div>
            ) : (
              filteredJobs.map((job) => (
                <div key={job.id} className="job-card">
                  <div className="job-card-header">
                    <div className="job-title-section">
                      <h3>{job.title}</h3>
                      <p className="company">{job.companyName || job.employer?.companyName || 'Company'}</p>
                    </div>
                    <button
                      className={`save-btn ${savedJobs.includes(job.id) ? 'saved' : ''}`}
                      onClick={() => toggleSaveJob(job.id)}
                      title={savedJobs.includes(job.id) ? 'Remove from saved' : 'Save job'}
                      type="button"
                    >
                      {savedJobs.includes(job.id) ? 'Saved' : 'Save'}
                    </button>
                  </div>

                  <div className="job-meta-info">
                    <span className="location">Location: {job.location || 'N/A'}</span>
                    <span className="salary">Salary: {job.salaryMin || 0} - {job.salaryMax || 0}</span>
                  </div>

                  <div className="job-tags">
                    <span className="tag">{job.jobType || 'N/A'}</span>
                    <span className="tag">{getExperienceLabel(job.experienceLevel)}</span>
                  </div>

                  <p className="job-description">{job.description}</p>

                  <div className="job-skills">
                    {(job.skillsRequired || job.requirements || '')
                      .split(',')
                      .map((skill) => skill.trim())
                      .filter(Boolean)
                      .slice(0, 4)
                      .map((skill) => (
                        <span key={skill} className="skill-badge">
                          {skill}
                        </span>
                      ))}
                  </div>

                  <div className="job-actions">
                    <Link to={`/jobs/${job.id}`} className="btn-secondary">
                      View Details
                    </Link>
                    <Link to={`/jobs/apply/${job.id}`} className="btn-primary">
                      Apply Now
                    </Link>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default JobsListing;
