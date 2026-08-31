import { useState } from 'react';
import { Link } from 'react-router-dom';

export default function Cart() {
  const [cartItems, setCartItems] = useState([
    { id: 1, name: 'Premium Wireless Headphones - Noise Cancelling, Over-Ear, 30h Battery', brand: 'SoundCore', price: 299.99, quantity: 1, color: 'Black', inStock: true, imageUrl: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=600&auto=format&fit=crop' },
    { id: 3, name: 'Mechanical Gaming Keyboard RGB', brand: 'TechLog', price: 149.00, quantity: 2, color: 'White', inStock: true, imageUrl: 'https://images.unsplash.com/photo-1595225476474-87563907a212?q=80&w=600&auto=format&fit=crop' },
  ]);

  const total = cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  const itemCount = cartItems.reduce((sum, item) => sum + item.quantity, 0);

  return (
    <div className="page-container" style={{background: 'transparent', maxWidth: '1500px'}}>
      
      {cartItems.length === 0 ? (
        <div className="card" style={{padding: '3rem', textAlign: 'center'}}>
          <h2 style={{color: 'var(--text-primary)', fontSize: '1.75rem', fontWeight: 500}}>Your Amazon Cart is empty.</h2>
          <p style={{color: 'var(--text-secondary)'}}>Check your Saved for later items below or <Link to="/products">continue shopping</Link>.</p>
        </div>
      ) : (
        <div className="product-details-layout" style={{gridTemplateColumns: '1fr 300px'}}>
          <div className="card" style={{padding: '1.5rem', display: 'flex', flexDirection: 'column', gap: '1rem', height: 'fit-content'}}>
            <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', borderBottom: '1px solid var(--border-color)', paddingBottom: '0.5rem'}}>
              <h1 style={{fontSize: '1.75rem', margin: 0, fontWeight: 400}}>Shopping Cart</h1>
              <span style={{color: 'var(--text-secondary)', fontSize: '0.9rem'}}>Price</span>
            </div>

            {cartItems.map(item => (
              <div key={item.id} style={{display: 'flex', gap: '1.5rem', borderBottom: '1px solid var(--border-color)', paddingBottom: '1.5rem', paddingTop: '1rem'}}>
                <div style={{width: '180px'}}>
                  <img src={item.imageUrl} alt={item.name} style={{width: '100%', height: 'auto', objectFit: 'contain'}} />
                </div>
                <div style={{flexGrow: 1}}>
                  <div style={{display: 'flex', justifyContent: 'space-between'}}>
                    <h3 style={{margin: '0 0 0.5rem 0', fontSize: '1.25rem', fontWeight: 500, lineHeight: 1.3}}>{item.name}</h3>
                    <strong style={{fontSize: '1.25rem'}}>${item.price.toFixed(2)}</strong>
                  </div>
                  <div style={{fontSize: '0.85rem', color: '#007600', marginBottom: '0.25rem'}}>{item.inStock ? 'In Stock' : 'Out of Stock'}</div>
                  <div style={{fontSize: '0.85rem', color: '#565959', marginBottom: '0.25rem'}}>Eligible for FREE Shipping & FREE Returns</div>
                  <div style={{fontSize: '0.85rem', color: '#565959', marginBottom: '0.25rem'}}><strong>Color:</strong> {item.color}</div>
                  
                  <div style={{display: 'flex', alignItems: 'center', gap: '1rem', marginTop: '1rem'}}>
                    <select value={item.quantity} onChange={() => {}} style={{padding: '0.3rem', borderRadius: '8px', border: '1px solid #d5d9d9', background: '#f0f2f2', outline: 'none'}}>
                      {[1,2,3,4,5,6,7,8,9,10].map(n => <option key={n} value={n}>Qty: {n}</option>)}
                    </select>
                    <div style={{borderLeft: '1px solid var(--border-color)', height: '1.2rem'}}></div>
                    <button style={{background: 'none', border: 'none', color: '#007185', padding: 0, fontSize: '0.85rem', boxShadow: 'none'}}>Delete</button>
                    <div style={{borderLeft: '1px solid var(--border-color)', height: '1.2rem'}}></div>
                    <button style={{background: 'none', border: 'none', color: '#007185', padding: 0, fontSize: '0.85rem', boxShadow: 'none'}}>Save for later</button>
                  </div>
                </div>
              </div>
            ))}
            
            <div style={{textAlign: 'right', fontSize: '1.2rem', paddingTop: '0.5rem'}}>
              Subtotal ({itemCount} item{itemCount !== 1 ? 's' : ''}): <strong style={{fontWeight: 600}}>${total.toFixed(2)}</strong>
            </div>
          </div>
          
          <div className="card" style={{padding: '1.5rem', height: 'fit-content', position: 'sticky', top: '100px'}}>
            <div style={{fontSize: '0.9rem', color: '#007600', marginBottom: '1rem', display: 'flex', gap: '0.5rem'}}>
              <span style={{fontSize: '1.2rem'}}>✓</span> <span>Your order qualifies for FREE Shipping.</span>
            </div>
            <div style={{fontSize: '1.2rem', marginBottom: '1.5rem'}}>
              Subtotal ({itemCount} item{itemCount !== 1 ? 's' : ''}): <strong style={{fontWeight: 600}}>${total.toFixed(2)}</strong>
            </div>
            
            <div style={{display: 'flex', gap: '0.5rem', marginBottom: '1rem', alignItems: 'center'}}>
              <input type="checkbox" id="gift" />
              <label htmlFor="gift" style={{fontSize: '0.9rem'}}>This order contains a gift</label>
            </div>

            <button className="btn-primary" style={{width: '100%', padding: '0.5rem', fontSize: '0.95rem', borderRadius: '8px'}}>Proceed to checkout</button>
          </div>
        </div>
      )}
    </div>
  );
}
