import { useEffect, useState } from 'react';
import ProductCard from '../components/ProductCard';

export default function Products() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulated fetching with fashion/shoe data
    setProducts([
      { id: 1, name: 'Adidas Yeezy Boost 700', price: 205.00, imageUrl: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=400&auto=format&fit=crop' },
      { id: 2, name: 'New Balance BB550', price: 210.00, imageUrl: 'https://images.unsplash.com/photo-1608231387042-66d1773070a5?q=80&w=400&auto=format&fit=crop' },
      { id: 3, name: 'ASICS Gel-Lyte III OG', price: 196.50, imageUrl: 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?q=80&w=400&auto=format&fit=crop' },
      { id: 4, name: 'Reebok Classic Leather', price: 149.15, imageUrl: 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?q=80&w=400&auto=format&fit=crop' },
      { id: 5, name: 'Nike Air Max 270', price: 160.00, imageUrl: 'https://images.unsplash.com/photo-1579338559194-a162d19bf842?q=80&w=400&auto=format&fit=crop' },
      { id: 6, name: 'Puma RS-X3', price: 110.00, imageUrl: 'https://images.unsplash.com/photo-1608231387042-66d1773070a5?q=80&w=400&auto=format&fit=crop' }
    ]);
    setLoading(false);
  }, []);

  return (
    <div className="page-container">
      <div className="text-small text-gray" style={{marginBottom: '2rem'}}>
        Home • All Products
      </div>
      
      <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: '2rem'}}>
        <h1 style={{fontSize: '2.5rem'}}>New Arrivals</h1>
        <div style={{display: 'flex', gap: '1rem'}}>
          <select style={{padding: '0.5rem 1rem', borderRadius: '20px', border: '1px solid var(--border-light)', outline: 'none'}}>
            <option>Sort by: Featured</option>
            <option>Price: Low to High</option>
            <option>Price: High to Low</option>
          </select>
        </div>
      </div>
      
      {loading ? (
        <p style={{textAlign: 'center', color: 'var(--text-secondary)'}}>Loading results...</p>
      ) : (
        <div className="minimal-grid">
          {products.map(p => (
             <ProductCard key={p.id} product={p} />
          ))}
        </div>
      )}
    </div>
  );
}
