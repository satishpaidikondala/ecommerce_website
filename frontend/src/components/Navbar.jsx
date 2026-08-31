import { Link } from 'react-router-dom';

export default function Navbar() {
  return (
    <nav className="glass navbar">
      <div className="nav-brand">
        <Link to="/">E-COMMERCE</Link>
      </div>
      <div className="nav-links">
        <Link to="/products">Products</Link>
        <Link to="/cart">Cart 🛒</Link>
        <button className="btn-primary">Sign In</button>
      </div>
    </nav>
  );
}
