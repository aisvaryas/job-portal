import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { jobService } from '../../services/jobService';
import { resumeService } from '../../services/resumeService';
import { applicationService } from '../../services/applicationService';
import '../../styles/JobApplication.css';

function JobApplication() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    resume: '',
    coverLetter: '',
    phoneNumber: '',
    email: '',
  });
  const [customQuestions, setCustomQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [jobTitle, setJobTitle] = useState('');
  const [resumes, setResumes] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadJob = async () => {
      setLoading(true);
      try {
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        setFormData((prev) => ({ ...prev, email: user.email || '' }));
        const job = await jobService.getJobById(id);
        setJobTitle(job?.title || 'Job');
        const resumeList = await resumeService.getResumes(user.email);
        setResumes(resumeList);
      } finally {
        setLoading(false);
      }
    };
    loadJob();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError('');
    try {
      const user = JSON.parse(localStorage.getItem('user') || '{}');
      await applicationService.submitApplication(user.email, id, {
        resumeId: Number(formData.resume),
        coverLetter: formData.coverLetter,
        phoneNumber: formData.phoneNumber,
        email: formData.email,
      });
      navigate(`/applications`);
    } catch (error) {
      setError(error?.response?.data?.message || 'Application submission failed');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div className="loading">Loading application form...</div>;

  return (
    <div className="page-wrapper">
      <Header />
      <div className="application-container">
        <h1>Apply for This Job</h1>
        {error && <p className="error-message">{error}</p>}

        <form onSubmit={handleSubmit} className="application-form">
          <section>
            <h2>Your Information</h2>
            
            <div className="form-group">
              <label htmlFor="email">Email *</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="phoneNumber">Phone Number *</label>
              <input
                type="tel"
                id="phoneNumber"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                required
              />
            </div>
          </section>

          <section>
            <h2>Resume *</h2>
            <div className="resume-select">
              <select name="resume" value={formData.resume} onChange={handleChange} required>
                <option value="">Select a resume</option>
                {resumes.map((resume) => (
                  <option key={resume.id} value={resume.id}>
                    {resume.fileName}
                  </option>
                ))}
              </select>
              <p>Don't have a resume? <Link to="/profile">Upload one now</Link></p>
            </div>
          </section>

          <section>
            <h2>Cover Letter</h2>
            <textarea
              name="coverLetter"
              value={formData.coverLetter}
              onChange={handleChange}
              placeholder="Share why you're interested in this role..."
              rows="6"
            ></textarea>
          </section>

          {customQuestions.length > 0 && (
            <section>
              <h2>Custom Questions</h2>
              {customQuestions.map((question, index) => (
                <div key={index} className="form-group">
                  <label>{question.text}</label>
                  <textarea placeholder="Your answer..." rows="3"></textarea>
                </div>
              ))}
            </section>
          )}

          <div className="form-actions">
            <button type="submit" className="btn-primary" disabled={submitting}>
              {submitting ? 'Submitting...' : 'Submit Application'}
            </button>
            <Link to={`/jobs/${id}`} className="btn-secondary">
              Cancel
            </Link>
          </div>
        </form>
      </div>
      <Footer />
    </div>
  );
}

export default JobApplication;
