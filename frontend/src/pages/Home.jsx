import { useEffect, useState } from 'react';
import api from '../api';
import ProductCard from '../components/ProductCard';

export default function Home() {
  const [products, setProducts] = useState([]);
  
  useEffect(() => {
    // Simulated fetching for initial design scaffolding
    // In reality: api.get('/products').then(res => setProducts(res.data));
    setProducts([
      { id: 1, name: 'Premium Wireless Headphones', price: 299.99 },
      { id: 2, name: 'Minimalist Smartwatch', price: 199.50 },
      { id: 3, name: 'Mechanical Keyboard', price: 149.00 },
      { id: 4, name: 'Ergonomic Mouse', price: 79.99 },
    ]);
  }, []);

  return (
    <div className="page-container">
      <section className="hero">
        <h1 style={{fontSize: '3.5rem', marginBottom: '1rem', color: 'var(--text-primary)'}}>Discover the Extraordinary</h1>
        <p style={{fontSize: '1.25rem', color: 'var(--text-secondary)', marginBottom: '2rem'}}>Premium tech and lifestyle products curated just for you.</p>
        <button className="btn-primary">Shop Now</button>
      </section>
      
      <section className="featured">
        <h2>Trending Products</h2>
        <div className="product-grid">
          {products.map(p => (
             <ProductCard key={p.id} product={p} />
          ))}
        </div>
      </section>
    </div>
  );
}
