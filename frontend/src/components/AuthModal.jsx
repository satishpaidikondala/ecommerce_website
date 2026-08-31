import { useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import './AuthModal.css';

export default function AuthModal() {
  const { isAuthModalOpen, setIsAuthModalOpen, login, register } = useContext(AuthContext);
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    firstName: '', lastName: '', email: '', password: '', phone: ''
  });

  if (!isAuthModalOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isLogin) {
        await login(formData.email, formData.password);
      } else {
        await register(formData);
      }
    } catch (err) {
      alert('Authentication failed. Check credentials and try again.');
    }
  };

  return (
    <div className="modal-overlay" onClick={() => setIsAuthModalOpen(false)}>
      <div className="glass modal-content" onClick={e => e.stopPropagation()}>
        <h2>{isLogin ? 'Welcome Back' : 'Create Account'}</h2>
        <form onSubmit={handleSubmit} className="auth-form">
          {!isLogin && (
            <>
              <input type="text" placeholder="First Name" required 
                onChange={e => setFormData({...formData, firstName: e.target.value})} />
              <input type="text" placeholder="Last Name" required 
                onChange={e => setFormData({...formData, lastName: e.target.value})} />
              <input type="text" placeholder="Phone" required 
                onChange={e => setFormData({...formData, phone: e.target.value})} />
            </>
          )}
          <input type="email" placeholder="Email" required 
            onChange={e => setFormData({...formData, email: e.target.value})} />
          <input type="password" placeholder="Password" required 
            onChange={e => setFormData({...formData, password: e.target.value})} />
          <button type="submit" className="btn-primary" style={{marginTop: '1rem'}}>
            {isLogin ? 'Sign In' : 'Sign Up'}
          </button>
        </form>
        <p className="toggle-text">
          {isLogin ? "Don't have an account? " : "Already have an account? "}
          <span onClick={() => setIsLogin(!isLogin)} className="toggle-link">
            {isLogin ? 'Sign up' : 'Log in'}
          </span>
        </p>
      </div>
    </div>
  );
}
