import { useEffect, useMemo, useState } from 'react';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { profileService } from '../../services/profileService';
import { resumeService } from '../../services/resumeService';
import '../../styles/Profile.css';

const emptyExperience = {
  company: '',
  position: '',
  startDate: '',
  endDate: '',
  location: '',
  description: '',
};

const emptyEducation = {
  institution: '',
  degree: '',
  fieldOfStudy: '',
  description: '',
  graduationDate: '',
  grade: '',
};

function SeekerProfile() {
  const [profile, setProfile] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    location: '',
    headline: '',
    summary: '',
  });
  const [resumes, setResumes] = useState([]);
  const [skills, setSkills] = useState([]);
  const [education, setEducation] = useState([]);
  const [experience, setExperience] = useState([]);
  const [resumeFile, setResumeFile] = useState(null);
  const [skillText, setSkillText] = useState('');
  const [editingSkill, setEditingSkill] = useState(null);
  const [experienceForm, setExperienceForm] = useState(emptyExperience);
  const [educationForm, setEducationForm] = useState(emptyEducation);
  const [editingExperienceId, setEditingExperienceId] = useState(null);
  const [editingEducationId, setEditingEducationId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [toast, setToast] = useState('');

  const fullName = useMemo(
    () => `${profile.firstName || ''} ${profile.lastName || ''}`.trim() || 'Job Seeker',
    [profile.firstName, profile.lastName]
  );

  useEffect(() => {
    loadProfile();
  }, []);

  const showToast = (message) => {
    setToast(message);
    setTimeout(() => setToast(''), 2600);
  };

  const getErrorMessage = (err, fallback) => {
    const data = err?.response?.data;
    if (data?.data && typeof data.data === 'object') {
      return Object.values(data.data).join(', ');
    }
    return data?.message || err?.message || fallback;
  };

  const loadProfile = async () => {
    setLoading(true);
    setError('');
    try {
      const bundle = await profileService.getSeekerProfileBundle();
      const seeker = bundle.seeker || {};
      const email = seeker.email || bundle.email || '';
      const resumeList = email ? await resumeService.getResumes(email).catch(() => []) : [];

      setProfile({
        firstName: seeker.firstName || '',
        lastName: seeker.lastName || '',
        email,
        phone: seeker.phone || '',
        location: seeker.location || '',
        headline: seeker.headline || '',
        summary: seeker.summary || '',
      });
      setSkills(bundle.skills || []);
      setEducation(bundle.education || []);
      setExperience(bundle.experience || []);
      setResumes(resumeList);
    } catch (err) {
      setError(getErrorMessage(err, 'Unable to fetch profile'));
    } finally {
      setLoading(false);
    }
  };

  const saveProfile = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError('');
    try {
      const updated = await profileService.updateCurrentSeekerProfile({
        firstName: profile.firstName,
        lastName: profile.lastName || 'User',
        phone: profile.phone,
        location: profile.location,
        headline: profile.headline,
        summary: profile.summary,
      });
      setProfile((prev) => ({
        ...prev,
        firstName: updated.firstName || '',
        lastName: updated.lastName || '',
        email: updated.email || prev.email,
        phone: updated.phone || '',
        location: updated.location || '',
        headline: updated.headline || '',
        summary: updated.summary || '',
      }));
      showToast('Profile updated successfully');
    } catch (err) {
      setError(getErrorMessage(err, 'Unable to save profile'));
    } finally {
      setSaving(false);
    }
  };
const uploadResume = async () => {
  if (!resumeFile) return;

  try {
    await resumeService.uploadResume(
      resumeFile,
      resumes.length === 0,
      "PRIVATE"
    );

    setResumeFile(null);
    setResumes(await resumeService.getResumes());
    showToast("Resume uploaded successfully");
  } catch (err) {
    setError(getErrorMessage(err, "Unable to upload resume"));
  }
};

const deleteResume = async (resumeId) => {
  try {
    await resumeService.deleteResume(resumeId);
    setResumes((prev) => prev.filter((r) => r.id !== resumeId));
    showToast("Resume deleted successfully");
  } catch (err) {
    setError(getErrorMessage(err, "Unable to delete resume"));
  }
};

  const openResume = async (resumeId) => {
    setError('');
    try {
      await resumeService.openResume(resumeId);
    } catch (err) {
      setError(getErrorMessage(err, 'Unable to open resume'));
    }
  };

  const saveSkill = async () => {
    const value = skillText.trim();
    if (!value) return;
    setError('');
    try {
      if (editingSkill) {
        await profileService.updateSkill(profile.email, editingSkill.skillName, value);
        showToast('Skill updated successfully');
      } else {
        await profileService.addSkill(profile.email, value);
        showToast('Skill added successfully');
      }
      setSkillText('');
      setEditingSkill(null);
      await reloadLists();
    } catch (err) {
      setError(getErrorMessage(err, 'Unable to save skill'));
    }
  };

  const editSkill = (skill) => {
    setEditingSkill(skill);
    setSkillText(skill.skillName || '');
  };

  const deleteSkill = async (skillName) => {
    setError('');
    try {
      await profileService.deleteSkill(profile.email, skillName);
      setSkills((prev) => prev.filter((skill) => skill.skillName !== skillName));
      showToast('Skill deleted successfully');
    } catch (err) {
      setError(getErrorMessage(err, 'Unable to delete skill'));
    }
  };

  const saveExperience = async () => {
    setError('');
    try {
      if (editingExperienceId) {
        await profileService.updateExperience(profile.email, editingExperienceId, experienceForm);
        showToast('Experience updated successfully');
      } else {
        await profileService.addExperience(profile.email, experienceForm);
        showToast('Experience added successfully');
      }
      setExperienceForm(emptyExperience);
      setEditingExperienceId(null);
      await reloadLists();
    } catch (err) {
      setError(getErrorMessage(err, 'Unable to save experience'));
    }
  };

  const editExperience = (item) => {
    setEditingExperienceId(item.id);
    setExperienceForm({
      company: item.company || '',
      position: item.position || '',
      startDate: item.startDate || '',
      endDate: item.endDate || '',
      location: item.location || '',
      description: item.description || '',
    });
  };

  const deleteExperience = async (id) => {
    setError('');
    try {
      await profileService.deleteExperience(profile.email, id);
      setExperience((prev) => prev.filter((item) => item.id !== id));
      showToast('Experience deleted successfully');
    } catch (err) {
      setError(getErrorMessage(err, 'Unable to delete experience'));
    }
  };

  const saveEducation = async () => {
    setError('');
    try {
      if (editingEducationId) {
        await profileService.updateEducation(profile.email, editingEducationId, educationForm);
        showToast('Education updated successfully');
      } else {
        await profileService.addEducation(profile.email, educationForm);
        showToast('Education added successfully');
      }
      setEducationForm(emptyEducation);
      setEditingEducationId(null);
      await reloadLists();
    } catch (err) {
      setError(getErrorMessage(err, 'Unable to save education'));
    }
  };

  const editEducation = (item) => {
    setEditingEducationId(item.id);
    setEducationForm({
      institution: item.institution || '',
      degree: item.degree || '',
      fieldOfStudy: item.fieldOfStudy || '',
      description: item.description || '',
      graduationDate: item.graduationDate || '',
      grade: item.grade || '',
    });
  };

  const deleteEducation = async (id) => {
    setError('');
    try {
      await profileService.deleteEducation(profile.email, id);
      setEducation((prev) => prev.filter((item) => item.id !== id));
      showToast('Education deleted successfully');
    } catch (err) {
      setError(getErrorMessage(err, 'Unable to delete education'));
    }
  };

  const reloadLists = async () => {
    const bundle = await profileService.getSeekerProfileBundle();
    setSkills(bundle.skills || []);
    setEducation(bundle.education || []);
    setExperience(bundle.experience || []);
  };

  if (loading) return <div className="loading">Loading profile...</div>;

  return (
    <div className="page-wrapper">
      <Header />
      {toast && <div className="toast-message">{toast}</div>}
      <main className="profile-container">
        <section className="profile-hero">
          <div className="profile-avatar">{fullName.slice(0, 1).toUpperCase()}</div>
          <div>
            <h1>{fullName}</h1>
            <p>{profile.headline || 'Add a headline to introduce yourself'}</p>
            <span>{profile.location || 'Location not set'} | {profile.email}</span>
          </div>
        </section>

        {error && <div className="error-message profile-error">{error}</div>}

        <div className="profile-grid">
          <section className="profile-section profile-main-card">
            <div className="section-title-row">
              <h2>Personal Details</h2>
            </div>
            <form className="profile-form" onSubmit={saveProfile}>
              <div className="form-row">
                <label>
                  First Name
                  <input value={profile.firstName} onChange={(e) => setProfile((p) => ({ ...p, firstName: e.target.value }))} required />
                </label>
                <label>
                  Last Name
                  <input value={profile.lastName} onChange={(e) => setProfile((p) => ({ ...p, lastName: e.target.value }))} required />
                </label>
              </div>
              <div className="form-row">
                <label>
                  Email
                  <input value={profile.email} disabled />
                </label>
                <label>
                  Phone
                  <input value={profile.phone} onChange={(e) => setProfile((p) => ({ ...p, phone: e.target.value }))} placeholder="10 digit phone number" />
                </label>
              </div>
              <label>
                Location
                <input value={profile.location} onChange={(e) => setProfile((p) => ({ ...p, location: e.target.value }))} placeholder="City, Country" />
              </label>
              <label>
                Headline
                <input value={profile.headline} onChange={(e) => setProfile((p) => ({ ...p, headline: e.target.value }))} placeholder="Java Developer | Spring Boot | React" />
              </label>
              <label>
                Summary
                <textarea value={profile.summary} onChange={(e) => setProfile((p) => ({ ...p, summary: e.target.value }))} placeholder="Write a short professional summary" rows="4" />
              </label>
              <button className="btn-primary" type="submit" disabled={saving}>
                {saving ? 'Saving...' : 'Save Profile'}
              </button>
            </form>
          </section>

          <aside className="profile-section profile-side-card">
            <h2>Resume</h2>
            <div className="upload-row">
              <input type="file" accept=".pdf,.doc,.docx" onChange={(e) => setResumeFile(e.target.files?.[0] || null)} />
              <button className="btn-secondary" type="button" onClick={uploadResume}>Upload</button>
            </div>
            {resumeFile && <p className="muted">Selected: {resumeFile.name}</p>}
            <div className="resume-list">
              {resumes.length === 0 ? (
                <p className="muted">No resumes uploaded yet.</p>
              ) : resumes.map((resume) => (
                <div className="resume-item" key={resume.id}>
                  <div>
                    <strong>{resume.fileName}</strong>
                    <span>{resume.fileType} | {resume.visibility}</span>
                  </div>
                  <div className="inline-actions">
                    <button className="btn-link" type="button" onClick={() => openResume(resume.id)}>Open</button>
                    <button className="btn-link danger" type="button" onClick={() => deleteResume(resume.id)}>Delete</button>
                  </div>
                </div>
              ))}
            </div>
          </aside>

          <section className="profile-section">
            <div className="section-title-row">
              <h2>Skills</h2>
            </div>
            <div className="compact-form">
              <input value={skillText} onChange={(e) => setSkillText(e.target.value)} placeholder="Add skill" />
              <button className="btn-secondary" type="button" onClick={saveSkill}>{editingSkill ? 'Update' : 'Add'}</button>
              {editingSkill && <button className="btn-link" type="button" onClick={() => { setEditingSkill(null); setSkillText(''); }}>Cancel</button>}
            </div>
            <div className="skills-list">
              {skills.length === 0 ? <p className="muted">No skills added yet.</p> : skills.map((skill) => (
                <span className="skill-chip" key={skill.id || skill.skillName}>
                  {skill.skillName}
                  <button type="button" onClick={() => editSkill(skill)}>Edit</button>
                  <button type="button" onClick={() => deleteSkill(skill.skillName)}>Delete</button>
                </span>
              ))}
            </div>
          </section>

          <section className="profile-section">
            <div className="section-title-row">
              <h2>Experience</h2>
            </div>
            <div className="profile-form">
              <div className="form-row">
                <input placeholder="Company" value={experienceForm.company} onChange={(e) => setExperienceForm((p) => ({ ...p, company: e.target.value }))} />
                <input placeholder="Position" value={experienceForm.position} onChange={(e) => setExperienceForm((p) => ({ ...p, position: e.target.value }))} />
              </div>
              <div className="form-row">
                <input type="date" value={experienceForm.startDate} onChange={(e) => setExperienceForm((p) => ({ ...p, startDate: e.target.value }))} />
                <input type="date" value={experienceForm.endDate} onChange={(e) => setExperienceForm((p) => ({ ...p, endDate: e.target.value }))} />
              </div>
              <input placeholder="Location" value={experienceForm.location} onChange={(e) => setExperienceForm((p) => ({ ...p, location: e.target.value }))} />
              <textarea placeholder="Description" value={experienceForm.description} onChange={(e) => setExperienceForm((p) => ({ ...p, description: e.target.value }))} rows="3" />
              <div className="inline-actions">
                <button className="btn-secondary" type="button" onClick={saveExperience}>{editingExperienceId ? 'Update Experience' : 'Add Experience'}</button>
                {editingExperienceId && <button className="btn-link" type="button" onClick={() => { setEditingExperienceId(null); setExperienceForm(emptyExperience); }}>Cancel</button>}
              </div>
            </div>
            <div className="timeline-list">
              {experience.length === 0 ? <p className="muted">No experience added yet.</p> : experience.map((item) => (
                <article className="timeline-card" key={item.id}>
                  <div>
                    <h3>{item.position}</h3>
                    <p>{item.company} {item.location ? `- ${item.location}` : ''}</p>
                    <span>{item.startDate} {item.endDate ? `to ${item.endDate}` : 'to Present'}</span>
                    {item.description && <p className="card-description">{item.description}</p>}
                  </div>
                  <div className="inline-actions">
                    <button className="btn-link" type="button" onClick={() => editExperience(item)}>Edit</button>
                    <button className="btn-link danger" type="button" onClick={() => deleteExperience(item.id)}>Delete</button>
                  </div>
                </article>
              ))}
            </div>
          </section>

          <section className="profile-section">
            <div className="section-title-row">
              <h2>Education</h2>
            </div>
            <div className="profile-form">
              <div className="form-row">
                <input placeholder="Institution" value={educationForm.institution} onChange={(e) => setEducationForm((p) => ({ ...p, institution: e.target.value }))} />
                <input placeholder="Degree" value={educationForm.degree} onChange={(e) => setEducationForm((p) => ({ ...p, degree: e.target.value }))} />
              </div>
              <div className="form-row">
                <input placeholder="Field of Study" value={educationForm.fieldOfStudy} onChange={(e) => setEducationForm((p) => ({ ...p, fieldOfStudy: e.target.value }))} />
                <input type="date" value={educationForm.graduationDate} onChange={(e) => setEducationForm((p) => ({ ...p, graduationDate: e.target.value }))} />
              </div>
              <input placeholder="Grade" value={educationForm.grade} onChange={(e) => setEducationForm((p) => ({ ...p, grade: e.target.value }))} />
              <textarea placeholder="Description" value={educationForm.description} onChange={(e) => setEducationForm((p) => ({ ...p, description: e.target.value }))} rows="3" />
              <div className="inline-actions">
                <button className="btn-secondary" type="button" onClick={saveEducation}>{editingEducationId ? 'Update Education' : 'Add Education'}</button>
                {editingEducationId && <button className="btn-link" type="button" onClick={() => { setEditingEducationId(null); setEducationForm(emptyEducation); }}>Cancel</button>}
              </div>
            </div>
            <div className="card-list">
              {education.length === 0 ? <p className="muted">No education added yet.</p> : education.map((item) => (
                <article className="detail-card" key={item.id}>
                  <div>
                    <h3>{item.degree}</h3>
                    <p>{item.institution}</p>
                    <span>{item.fieldOfStudy} {item.graduationDate ? `| Graduated ${item.graduationDate}` : ''}</span>
                    {item.grade && <p className="card-description">Grade: {item.grade}</p>}
                    {item.description && <p className="card-description">{item.description}</p>}
                  </div>
                  <div className="inline-actions">
                    <button className="btn-link" type="button" onClick={() => editEducation(item)}>Edit</button>
                    <button className="btn-link danger" type="button" onClick={() => deleteEducation(item.id)}>Delete</button>
                  </div>
                </article>
              ))}
            </div>
          </section>
        </div>
      </main>
      <Footer />
    </div>
  );
}

export default SeekerProfile;
