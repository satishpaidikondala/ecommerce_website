import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { ShoppingBag, Heart, User, Search } from 'lucide-react';

export default function Navbar() {
  const { user, setIsAuthModalOpen } = useContext(AuthContext);

  return (
    <div className="page-container">
      <nav className="navbar-minimal">
        <div className="nav-logo">
          <Link to="/">BR.<span style={{fontSize: '1.2rem'}}>F</span></Link>
        </div>
        
        <div className="nav-search-bar">
          <Search size={18} color="var(--text-secondary)" />
          <input type="text" placeholder="Search" />
        </div>

        <div className="nav-icons">
          <div className="icon-btn">
            <ShoppingBag size={22} strokeWidth={1.5} />
            <div className="cart-badge">4</div>
            <span>Cart</span>
          </div>
          <div className="icon-btn">
            <Heart size={22} strokeWidth={1.5} />
            <span>Favorites</span>
          </div>
          <div className="icon-btn" onClick={() => !user && setIsAuthModalOpen(true)}>
            {user ? (
              <div style={{width: '35px', height: '35px', borderRadius: '50%', background: '#c4c4c4', overflow: 'hidden'}}>
                <User size={35} color="#fff" />
              </div>
            ) : (
              <User size={22} strokeWidth={1.5} />
            )}
          </div>
        </div>
      </nav>

      <div className="nav-links-center" style={{marginBottom: '2rem'}}>
        <Link to="/products">Women</Link>
        <Link to="/products">Men</Link>
        <Link to="/products">Kids</Link>
        <Link to="/products">Sports</Link>
        <Link to="/products">Brands</Link>
        <Link to="/products">New</Link>
        <Link to="/products" className="text-red">Sale</Link>
      </div>
    </div>
  );
}
