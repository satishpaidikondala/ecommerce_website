import { useEffect, useState } from 'react';
import api from '../api';
import ProductCard from '../components/ProductCard';

export default function Products() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulated fetching for design scaffolding
    // api.get('/products').then(res => setProducts(res.data));
    setProducts([
      { id: 1, name: 'Premium Wireless Headphones', price: 299.99, imageUrl: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=600&auto=format&fit=crop' },
      { id: 2, name: 'Minimalist Smartwatch', price: 199.50, imageUrl: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=600&auto=format&fit=crop' },
      { id: 3, name: 'Mechanical Keyboard', price: 149.00, imageUrl: 'https://images.unsplash.com/photo-1595225476474-87563907a212?q=80&w=600&auto=format&fit=crop' },
      { id: 4, name: 'Ergonomic Mouse', price: 79.99, imageUrl: 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?q=80&w=600&auto=format&fit=crop' },
      { id: 5, name: 'Noise-Cancelling Earbuds', price: 129.99, imageUrl: 'https://images.unsplash.com/photo-1606220588913-b3aacb4d2f46?q=80&w=600&auto=format&fit=crop' },
      { id: 6, name: '4K Ultra HD Monitor', price: 399.00, imageUrl: 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?q=80&w=600&auto=format&fit=crop' }
    ]);
    setLoading(false);
  }, []);

  return (
    <div className="page-container">
      <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem', flexWrap: 'wrap', gap: '1rem'}}>
        <h1 style={{fontSize: '2.5rem', margin: 0, color: 'var(--text-primary)'}}>All Products</h1>
        <div style={{display: 'flex', gap: '1rem'}}>
          <input type="text" placeholder="Search products..." className="input-field" style={{width: '300px'}} />
          <button className="btn-primary">Search</button>
        </div>
      </div>
      
      {loading ? (
        <p style={{textAlign: 'center', color: 'var(--text-secondary)'}}>Loading products...</p>
      ) : (
        <div className="product-grid">
          {products.map(p => (
             <ProductCard key={p.id} product={p} />
          ))}
        </div>
      )}
    </div>
  );
}
