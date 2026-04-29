import { useState, useEffect } from 'react';
import Header from '../../components/common/Header';
import Footer from '../../components/common/Footer';
import { companyService } from '../../services/companyService';
import '../../styles/CompanyProfile.css';

function CompanyProfile() {
  const [company, setCompany] = useState({
    name: '',
    description: '',
    website: '',
    industry: '',
    size: '',
    headquarters: '',
    socialLinks: {},
  });
  const [editing, setEditing] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const fetchCompany = async () => {
      setLoading(true);
      setError('');
      try {
        const data = await companyService.getCompanyProfile();
        setCompany({
          name: data?.companyName || '',
          description: data?.description || '',
          website: data?.website || '',
          industry: data?.industry || '',
          size: '',
          headquarters: data?.location || '',
          socialLinks: {},
          contactFirstName: data?.contactFirstName || '',
          contactLastName: data?.contactLastName || '',
          phone: data?.phone || '',
        });
      } catch (err) {
        setError(err?.response?.data?.message || 'Unable to fetch company profile');
        console.error('Company fetch error:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchCompany();
  }, []);

  const handleSave = async () => {
    setSaving(true);
    setError('');
    setSuccess('');
    try {
      await companyService.updateCompanyProfile({
        contactFirstName: company.contactFirstName,
        contactLastName: company.contactLastName,
        companyName: company.name,
        industry: company.industry,
        website: company.website,
        description: company.description,
        phone: company.phone,
        location: company.headquarters,
      });
      setEditing(false);
      setSuccess('Profile updated successfully!');
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      const errorMsg = err?.response?.data?.message || err?.response?.data?.data || 'Unable to save company profile';
      setError(errorMsg);
      console.error('Company update error:', err);
    } finally {
      setSaving(false);
    }
  };

  if (loading) return <div className="loading">Loading company profile...</div>;

  return (
    <div className="page-wrapper">
      <Header />
      <div className="company-profile-container">
        <div className="profile-header">
          <h1>Company Profile</h1>
          {error && <p className="error-message">{error}</p>}
          {success && <p className="success-message">{success}</p>}
          <button 
            className="btn-primary"
            onClick={() => setEditing(!editing)}
            disabled={saving}
          >
            {editing ? 'Cancel' : 'Edit Profile'}
          </button>
        </div>

        <div className="profile-content">
          <div className="profile-section">
            <h2>Company Information</h2>
            {!editing ? (
              <div className="info-display">
                <p><strong>Contact Name:</strong> {company.contactFirstName} {company.contactLastName}</p>
                <p><strong>Company Name:</strong> {company.name}</p>
                <p><strong>Description:</strong> {company.description}</p>
                <p><strong>Website:</strong> {company.website}</p>
                <p><strong>Industry:</strong> {company.industry}</p>
                <p><strong>Phone:</strong> {company.phone}</p>
                <p><strong>Location:</strong> {company.headquarters}</p>
              </div>
            ) : (
              <form className="info-form">
                <input 
                  type="text" 
                  placeholder="First Name" 
                  value={company.contactFirstName} 
                  onChange={(e) => setCompany((prev) => ({ ...prev, contactFirstName: e.target.value }))} 
                />
                <input 
                  type="text" 
                  placeholder="Last Name" 
                  value={company.contactLastName} 
                  onChange={(e) => setCompany((prev) => ({ ...prev, contactLastName: e.target.value }))} 
                />
                <input 
                  type="text" 
                  placeholder="Company Name" 
                  value={company.name} 
                  onChange={(e) => setCompany((prev) => ({ ...prev, name: e.target.value }))} 
                />
                <textarea 
                  placeholder="Company Description" 
                  value={company.description} 
                  onChange={(e) => setCompany((prev) => ({ ...prev, description: e.target.value }))}
                ></textarea>
                <input 
                  type="url" 
                  placeholder="Website URL" 
                  value={company.website} 
                  onChange={(e) => setCompany((prev) => ({ ...prev, website: e.target.value }))} 
                />
                <input 
                  type="text" 
                  placeholder="Industry" 
                  value={company.industry} 
                  onChange={(e) => setCompany((prev) => ({ ...prev, industry: e.target.value }))} 
                />
                <input 
                  type="tel" 
                  placeholder="Phone" 
                  value={company.phone} 
                  onChange={(e) => setCompany((prev) => ({ ...prev, phone: e.target.value }))} 
                />
                <input 
                  type="text" 
                  placeholder="Location/Headquarters" 
                  value={company.headquarters} 
                  onChange={(e) => setCompany((prev) => ({ ...prev, headquarters: e.target.value }))} 
                />
              </form>
            )}
          </div>

          {editing && (
            <button 
              className="btn-primary" 
              onClick={handleSave}
              disabled={saving}
            >
              {saving ? 'Saving...' : 'Save Changes'}
            </button>
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default CompanyProfile;
