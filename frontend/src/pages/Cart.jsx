import { useState } from 'react';

export default function Cart() {
  // Simulated cart state
  const [cartItems, setCartItems] = useState([
    { id: 1, name: 'Premium Wireless Headphones', price: 299.99, quantity: 1 },
    { id: 3, name: 'Mechanical Keyboard', price: 149.00, quantity: 2 },
  ]);

  const total = cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);

  return (
    <div className="page-container">
      <h1 style={{fontSize: '2.5rem', marginBottom: '2rem', color: 'var(--text-primary)'}}>Your Shopping Cart</h1>
      
      {cartItems.length === 0 ? (
        <div className="card" style={{padding: '3rem', textAlign: 'center'}}>
          <h2 style={{color: 'var(--text-primary)'}}>Your cart is empty</h2>
          <p style={{color: 'var(--text-secondary)'}}>Looks like you haven't added anything to your cart yet.</p>
        </div>
      ) : (
        <div style={{display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '2rem'}}>
          <div className="cart-items" style={{display: 'flex', flexDirection: 'column', gap: '1rem'}}>
            {cartItems.map(item => (
              <div key={item.id} className="card" style={{padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <div>
                  <h3 style={{margin: '0 0 0.5rem 0', color: 'var(--text-primary)'}}>{item.name}</h3>
                  <p style={{margin: 0, color: '#b12704', fontWeight: 'bold'}}>${item.price.toFixed(2)}</p>
                </div>
                <div style={{display: 'flex', alignItems: 'center', gap: '1rem'}}>
                  <span style={{background: 'var(--bg-body)', padding: '0.5rem 1rem', borderRadius: '6px', color: 'var(--text-primary)', border: '1px solid var(--border-color)'}}>Qty: {item.quantity}</span>
                  <button className="btn-accent" style={{padding: '0.5rem 1rem'}}>Remove</button>
                </div>
              </div>
            ))}
          </div>
          
          <div className="card" style={{padding: '2rem', height: 'fit-content', position: 'sticky', top: '100px'}}>
            <h2 style={{marginTop: 0, color: 'var(--text-primary)'}}>Order Summary</h2>
            <div style={{display: 'flex', justifyContent: 'space-between', marginBottom: '1rem', color: 'var(--text-secondary)'}}>
              <span>Subtotal</span>
              <span>${total.toFixed(2)}</span>
            </div>
            <div style={{display: 'flex', justifyContent: 'space-between', marginBottom: '1rem', color: 'var(--text-secondary)'}}>
              <span>Shipping</span>
              <span>Free</span>
            </div>
            <hr style={{borderColor: 'var(--border-color)', margin: '1.5rem 0'}} />
            <div style={{display: 'flex', justifyContent: 'space-between', marginBottom: '2rem', fontSize: '1.25rem', fontWeight: 'bold', color: 'var(--text-primary)'}}>
              <span>Total</span>
              <span style={{color: '#b12704'}}>${total.toFixed(2)}</span>
            </div>
            <button className="btn-primary" style={{width: '100%', padding: '1rem', fontSize: '1.1rem'}}>Proceed to Checkout</button>
          </div>
        </div>
      )}
    </div>
  );
}
