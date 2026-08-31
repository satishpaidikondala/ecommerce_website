import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { CartContext } from '../context/CartContext';

export default function Cart() {
  const { cartItems, removeFromCart, updateQuantity, cartCount, cartTotal } = useContext(CartContext);

  return (
    <div className="page-container" style={{background: 'transparent', maxWidth: '1500px'}}>
      
      {cartItems.length === 0 ? (
        <div className="card" style={{padding: '3rem', textAlign: 'center'}}>
          <h2 style={{color: 'var(--text-primary)', fontSize: '1.75rem', fontWeight: 500}}>Your Cart is empty.</h2>
          <p style={{color: 'var(--text-secondary)'}}>Looks like you haven't added any minimalist fashion items yet. <Link to="/products" style={{color: '#ff4d4d', fontWeight: 600}}>Continue shopping</Link>.</p>
        </div>
      ) : (
        <div className="product-details-layout" style={{gridTemplateColumns: '1fr 350px', gap: '2rem'}}>
          <div className="card" style={{padding: '1.5rem', display: 'flex', flexDirection: 'column', gap: '1rem', height: 'fit-content'}}>
            <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', borderBottom: '1px solid var(--border-light)', paddingBottom: '0.5rem'}}>
              <h1 style={{fontSize: '1.75rem', margin: 0, fontWeight: 400}}>Shopping Cart</h1>
              <span style={{color: 'var(--text-secondary)', fontSize: '0.9rem'}}>Price</span>
            </div>

            {cartItems.map((item, index) => (
              <div key={index} style={{display: 'flex', gap: '1.5rem', borderBottom: '1px solid var(--border-light)', paddingBottom: '1.5rem', paddingTop: '1rem'}}>
                <div style={{width: '180px', background: 'var(--bg-soft)', borderRadius: '8px', padding: '1rem'}}>
                  <img src={item.images ? item.images[0] : item.imageUrl} alt={item.name} style={{width: '100%', height: 'auto', objectFit: 'contain'}} />
                </div>
                <div style={{flexGrow: 1}}>
                  <div style={{display: 'flex', justifyContent: 'space-between'}}>
                    <h3 style={{margin: '0 0 0.5rem 0', fontSize: '1.25rem', fontWeight: 500, lineHeight: 1.3}}>{item.name}</h3>
                    <strong style={{fontSize: '1.25rem'}}>${parseFloat(item.price).toFixed(2)}</strong>
                  </div>
                  <div style={{fontSize: '0.85rem', color: '#007600', marginBottom: '0.25rem'}}>In Stock</div>
                  <div style={{fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: '0.25rem'}}><strong>Color:</strong> {item.selectedColor || item.color}</div>
                  <div style={{fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: '0.25rem'}}><strong>Size:</strong> {item.selectedSize}</div>
                  
                  <div style={{display: 'flex', alignItems: 'center', gap: '1rem', marginTop: '1rem'}}>
                    <select 
                      value={item.quantity} 
                      onChange={(e) => updateQuantity(index, parseInt(e.target.value))} 
                      style={{padding: '0.3rem', borderRadius: '4px', border: '1px solid var(--border-light)', background: '#fff', outline: 'none', cursor: 'pointer'}}
                    >
                      {[1,2,3,4,5,6,7,8,9,10].map(n => <option key={n} value={n}>Qty: {n}</option>)}
                    </select>
                    <div style={{borderLeft: '1px solid var(--border-light)', height: '1.2rem'}}></div>
                    <button onClick={() => removeFromCart(index)} style={{background: 'none', border: 'none', color: '#ff4d4d', padding: 0, fontSize: '0.85rem', cursor: 'pointer'}}>Remove</button>
                  </div>
                </div>
              </div>
            ))}
            
            <div style={{textAlign: 'right', fontSize: '1.2rem', paddingTop: '0.5rem'}}>
              Subtotal ({cartCount} item{cartCount !== 1 ? 's' : ''}): <strong style={{fontWeight: 600}}>${cartTotal.toFixed(2)}</strong>
            </div>
          </div>
          
          <div className="card" style={{padding: '2rem', height: 'fit-content', position: 'sticky', top: '100px', background: 'var(--bg-soft)', borderRadius: '12px'}}>
            <div style={{fontSize: '1.2rem', marginBottom: '1.5rem'}}>
              Subtotal ({cartCount} item{cartCount !== 1 ? 's' : ''}): <br/>
              <strong style={{fontWeight: 700, fontSize: '2rem'}}>${cartTotal.toFixed(2)}</strong>
            </div>
            
            <button className="btn-black" style={{width: '100%', padding: '1rem', fontSize: '1rem', borderRadius: '8px'}}>Proceed to checkout</button>
            <div style={{marginTop: '1rem', fontSize: '0.8rem', color: 'var(--text-secondary)', textAlign: 'center'}}>
              Free delivery on orders over $30.0
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
