import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { profileService } from '../../services/profileService';
import { jobService } from '../../services/jobService';
import '../../styles/CreateJob.css';

function CreateJob() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const editJobId = searchParams.get('edit');
  const [formData, setFormData] = useState({
    title: '',
    location: '',
    salaryMin: '',
    salaryMax: '',
    jobType: '',
    description: '',
    requirements: '',
    benefits: '',
    aboutCompany: '',
    experienceLevel: '',
    skillsRequired: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadJobForEdit = async () => {
      if (!editJobId) return;
      try {
        const job = await jobService.getJobById(editJobId);
        setFormData({
          title: job.title || '',
          location: job.location || '',
          salaryMin: job.salaryMin || '',
          salaryMax: job.salaryMax || '',
          jobType: job.jobType || '',
          description: job.description || '',
          requirements: job.requirements || '',
          benefits: job.benefits || '',
          aboutCompany: job.aboutCompany || '',
          experienceLevel: job.experienceLevel || '',
          skillsRequired: job.skillsRequired || '',
        });
      } catch (err) {
        setError(err?.response?.data?.message || 'Unable to load job for editing');
      }
    };
    loadJobForEdit();
  }, [editJobId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError('');
    try {
      const user = JSON.parse(localStorage.getItem('user') || '{}');
      const employer = await profileService.ensureEmployerProfile(user);

      const payload = {
        title: formData.title,
        location: formData.location,
        salaryMin: Number(formData.salaryMin || 0),
        salaryMax: Number(formData.salaryMax || 0),
        jobType: formData.jobType,
        description: formData.description,
        requirements: formData.requirements,
        benefits: formData.benefits,
        aboutCompany: formData.aboutCompany,
        experienceLevel: formData.experienceLevel,
        skillsRequired: formData.skillsRequired,
        deadline: null,
      };

      if (editJobId) {
        await jobService.updateJob(employer.id, editJobId, payload);
      } else {
        await jobService.createJob(employer.id, payload);
      }
      navigate('/dashboard/employer');
    } catch (err) {
      setError(err?.response?.data?.message || err.message || 'Unable to post job');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="page-wrapper">
      <Header />
      <div className="create-job-container">
        <h1>{editJobId ? 'Edit Job' : 'Post a New Job'}</h1>
        {error && <p className="error-message">{error}</p>}

        <form onSubmit={handleSubmit} className="job-form">
          <div className="form-group">
            <label htmlFor="title">Job Title *</label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
              placeholder="e.g., Senior React Developer"
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="location">Location *</label>
              <input
                type="text"
                id="location"
                name="location"
                value={formData.location}
                onChange={handleChange}
                required
                placeholder="e.g., San Francisco, CA"
              />
            </div>

            <div className="form-group">
              <label htmlFor="jobType">Job Type *</label>
              <select
                id="jobType"
                name="jobType"
                value={formData.jobType}
                onChange={handleChange}
                required
              >
                <option value="">Select job type</option>
                <option value="FULL_TIME">Full-time</option>
                <option value="PART_TIME">Part-time</option>
                <option value="CONTRACT">Contract</option>
                <option value="INTERNSHIP">Internship</option>
              </select>
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="salaryMin">Salary Min *</label>
              <input
                type="number"
                id="salaryMin"
                name="salaryMin"
                value={formData.salaryMin}
                onChange={handleChange}
                min="0"
                required
                placeholder="e.g., 400000"
              />
            </div>

            <div className="form-group">
              <label htmlFor="salaryMax">Salary Max *</label>
              <input
                type="number"
                id="salaryMax"
                name="salaryMax"
                value={formData.salaryMax}
                onChange={handleChange}
                min="0"
                required
                placeholder="e.g., 900000"
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="experienceLevel">Experience Level *</label>
              <select
                id="experienceLevel"
                name="experienceLevel"
                value={formData.experienceLevel}
                onChange={handleChange}
                required
              >
                <option value="">Select experience level</option>
                <option value="ENTRY">Entry Level</option>
                <option value="MID">Mid Level</option>
                <option value="SENIOR">Senior Level</option>
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="skillsRequired">Skills Required</label>
              <input
                type="text"
                id="skillsRequired"
                name="skillsRequired"
                value={formData.skillsRequired}
                onChange={handleChange}
                placeholder="e.g., Java, Spring Boot, MySQL"
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="description">Description *</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              required
              placeholder="Describe the responsibilities and day-to-day work"
              rows="6"
            ></textarea>
          </div>

          <div className="form-group">
            <label htmlFor="requirements">Requirements *</label>
            <textarea
              id="requirements"
              name="requirements"
              value={formData.requirements}
              onChange={handleChange}
              required
              placeholder="List qualifications, tools, and expectations"
              rows="4"
            ></textarea>
          </div>

          <div className="form-group">
            <label htmlFor="benefits">Benefits</label>
            <textarea
              id="benefits"
              name="benefits"
              value={formData.benefits}
              onChange={handleChange}
              placeholder="List compensation, leave, insurance, flexibility, or perks"
              rows="4"
            ></textarea>
          </div>

          <div className="form-group">
            <label htmlFor="aboutCompany">About Company</label>
            <textarea
              id="aboutCompany"
              name="aboutCompany"
              value={formData.aboutCompany}
              onChange={handleChange}
              placeholder="Share a short company overview"
              rows="4"
            ></textarea>
          </div>

          <div className="form-actions">
            <button type="submit" className="btn-primary" disabled={submitting}>
              {submitting ? 'Saving...' : editJobId ? 'Update Job' : 'Post Job'}
            </button>
            <button 
              type="button" 
              className="btn-secondary"
              onClick={() => navigate('/dashboard/employer')}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
      <Footer />
    </div>
  );
}

export default CreateJob;
