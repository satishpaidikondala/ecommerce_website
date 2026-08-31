import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';
import ProductCard from '../components/ProductCard';

export default function Home() {
  const [products, setProducts] = useState([]);
  
  useEffect(() => {
    // Simulated fetching for trending products
    setProducts([
      { id: 1, name: 'Premium Wireless Headphones - Noise Cancelling', price: 299.99, rating: 4.8, imageUrl: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=600&auto=format&fit=crop' },
      { id: 2, name: 'Minimalist Smartwatch Series 7', price: 199.50, rating: 4.5, imageUrl: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=600&auto=format&fit=crop' },
      { id: 3, name: 'Mechanical Gaming Keyboard RGB', price: 149.00, rating: 4.9, imageUrl: 'https://images.unsplash.com/photo-1595225476474-87563907a212?q=80&w=600&auto=format&fit=crop' },
      { id: 4, name: 'Ergonomic Wireless Mouse', price: 79.99, rating: 4.2, imageUrl: 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?q=80&w=600&auto=format&fit=crop' },
    ]);
  }, []);

  const categories = [
    { title: 'Electronics', img: 'https://images.unsplash.com/photo-1498049794561-7780e7231661?q=80&w=600&auto=format&fit=crop' },
    { title: 'Home & Kitchen', img: 'https://images.unsplash.com/photo-1556910103-1c02745aae4d?q=80&w=600&auto=format&fit=crop' },
    { title: 'Fashion', img: 'https://images.unsplash.com/photo-1445205170230-053b83016050?q=80&w=600&auto=format&fit=crop' },
    { title: 'Beauty & Personal Care', img: 'https://images.unsplash.com/photo-1596462502278-27bf85033e5a?q=80&w=600&auto=format&fit=crop' }
  ];

  return (
    <>
      <section className="hero-carousel">
        <h1>Summer Deals</h1>
      </section>
      
      <section className="category-grid">
        {categories.map((cat, i) => (
          <div key={i} className="category-card">
            <h2>{cat.title}</h2>
            <div className="img-placeholder" style={{backgroundImage: `url(${cat.img})`}}></div>
            <Link to="/products" style={{fontSize: '0.9rem'}}>Shop now</Link>
          </div>
        ))}
      </section>

      <div className="page-container" style={{paddingTop: 0}}>
        <div className="card" style={{padding: '1.5rem'}}>
          <h2 style={{marginTop: 0}}>Trending now</h2>
          <div className="product-grid" style={{gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))'}}>
            {products.map(p => (
               <ProductCard key={p.id} product={p} />
            ))}
          </div>
        </div>
      </div>
    </>
  );
}
