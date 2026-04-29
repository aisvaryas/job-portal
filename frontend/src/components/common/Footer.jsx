import '../../styles/Footer.css';

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-section">
          <h4>JobPortal</h4>
          <p>Connect talented professionals with opportunities.</p>
        </div>

        <div className="footer-section">
          <h4>For Job Seekers</h4>
          <ul>
            <li><a href="#jobs">Browse Jobs</a></li>
            <li><a href="#profile">Create Profile</a></li>
            <li><a href="#applications">Track Applications</a></li>
          </ul>
        </div>

        <div className="footer-section">
          <h4>For Employers</h4>
          <ul>
            <li><a href="#post-job">Post a Job</a></li>
            <li><a href="#find-talent">Find Talent</a></li>
            <li><a href="#company">Company Profile</a></li>
          </ul>
        </div>

        <div className="footer-section">
          <h4>Contact</h4>
          <ul>
            <li><a href="mailto:support@jobportal.com">support@jobportal.com</a></li>
            <li>Phone: +1 (555) 123-4567</li>
          </ul>
        </div>
      </div>

      <div className="footer-bottom">
        <p>&copy; 2026 JobPortal. All rights reserved.</p>
      </div>
    </footer>
  );
}

export default Footer;
