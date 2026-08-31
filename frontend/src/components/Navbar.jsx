import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

export default function Navbar() {
  const { user, setIsAuthModalOpen, logout } = useContext(AuthContext);

  return (
    <nav className="glass navbar">
      <div className="nav-brand">
        <Link to="/">E-COMMERCE</Link>
      </div>
      <div className="nav-links">
        <Link to="/products">Products</Link>
        <Link to="/cart">Cart 🛒</Link>
        {user ? (
          <div style={{display: 'flex', alignItems: 'center', gap: '1rem'}}>
            <span style={{color: 'var(--text-secondary)'}}>Hi, {user.email.split('@')[0]}</span>
            <button className="btn-accent" onClick={logout}>Logout</button>
          </div>
        ) : (
          <button className="btn-primary" onClick={() => setIsAuthModalOpen(true)}>Sign In</button>
        )}
      </div>
    </nav>
  );
}
