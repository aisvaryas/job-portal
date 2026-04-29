import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import '../../styles/JobSeekerProfile.css';

function JobSeekerPublicProfile() {
  const { id } = useParams();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // TODO: Fetch job seeker profile from API
    setTimeout(() => setLoading(false), 1000);
  }, [id]);

  if (loading) return <div className="loading">Loading profile...</div>;

  return (
    <div className="page-wrapper">
      <Header />
      <div className="public-profile-container">
        <Link to="/applicants" className="back-link">← Back</Link>

        {profile ? (
          <div className="profile">
            <div className="profile-header">
              <h1>{profile?.name}</h1>
              <p className="title">{profile?.currentRole}</p>
              <p className="location">{profile?.location}</p>
            </div>

            <div className="profile-sections">
              <section>
                <h2>About</h2>
                <p>{profile?.about}</p>
              </section>

              <section>
                <h2>Skills</h2>
                <div className="skills-list">
                  {profile?.skills?.map((skill) => (
                    <span key={skill} className="skill-tag">{skill}</span>
                  ))}
                </div>
              </section>

              <section>
                <h2>Experience</h2>
                {profile?.experience?.map((exp, index) => (
                  <div key={index} className="experience-item">
                    <h3>{exp.title}</h3>
                    <p className="company">{exp.company}</p>
                    <p className="duration">{exp.duration}</p>
                    <p>{exp.description}</p>
                  </div>
                ))}
              </section>

              <section>
                <h2>Education</h2>
                {profile?.education?.map((edu, index) => (
                  <div key={index} className="education-item">
                    <h3>{edu.degree}</h3>
                    <p className="school">{edu.school}</p>
                    <p className="year">{edu.year}</p>
                  </div>
                ))}
              </section>

              {profile?.resume && (
                <section>
                  <h2>Resume</h2>
                  <button className="btn-secondary">Download Resume</button>
                </section>
              )}
            </div>

            <div className="profile-actions">
              <button className="btn-primary">Send Message</button>
            </div>
          </div>
        ) : (
          <p>Profile not found</p>
        )}
      </div>
      <Footer />
    </div>
  );
}

export default JobSeekerPublicProfile;
