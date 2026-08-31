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
      <h1 style={{fontSize: '2.5rem', marginBottom: '2rem'}}>Your Shopping Cart</h1>
      
      {cartItems.length === 0 ? (
        <div className="glass" style={{padding: '3rem', textAlign: 'center'}}>
          <h2>Your cart is empty</h2>
          <p style={{color: 'var(--text-secondary)'}}>Looks like you haven't added anything to your cart yet.</p>
        </div>
      ) : (
        <div style={{display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '2rem'}}>
          <div className="cart-items" style={{display: 'flex', flexDirection: 'column', gap: '1rem'}}>
            {cartItems.map(item => (
              <div key={item.id} className="glass" style={{padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <div>
                  <h3 style={{margin: '0 0 0.5rem 0'}}>{item.name}</h3>
                  <p style={{margin: 0, color: 'var(--accent)'}}>${item.price.toFixed(2)}</p>
                </div>
                <div style={{display: 'flex', alignItems: 'center', gap: '1rem'}}>
                  <span style={{background: 'rgba(255,255,255,0.1)', padding: '0.5rem 1rem', borderRadius: '8px'}}>Qty: {item.quantity}</span>
                  <button className="btn-accent" style={{padding: '0.5rem 1rem'}}>Remove</button>
                </div>
              </div>
            ))}
          </div>
          
          <div className="glass" style={{padding: '2rem', height: 'fit-content', position: 'sticky', top: '100px'}}>
            <h2 style={{marginTop: 0}}>Order Summary</h2>
            <div style={{display: 'flex', justifyContent: 'space-between', marginBottom: '1rem'}}>
              <span>Subtotal</span>
              <span>${total.toFixed(2)}</span>
            </div>
            <div style={{display: 'flex', justifyContent: 'space-between', marginBottom: '1rem'}}>
              <span>Shipping</span>
              <span>Free</span>
            </div>
            <hr style={{borderColor: 'var(--glass-border)', margin: '1.5rem 0'}} />
            <div style={{display: 'flex', justifyContent: 'space-between', marginBottom: '2rem', fontSize: '1.25rem', fontWeight: 'bold'}}>
              <span>Total</span>
              <span style={{color: 'var(--accent)'}}>${total.toFixed(2)}</span>
            </div>
            <button className="btn-primary" style={{width: '100%', padding: '1rem', fontSize: '1.1rem'}}>Proceed to Checkout</button>
          </div>
        </div>
      )}
    </div>
  );
}
