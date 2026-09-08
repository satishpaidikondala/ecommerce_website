import { useState, useEffect, useContext } from 'react';
import { useParams } from 'react-router-dom';
import { ShoppingBag, Heart, Truck } from 'lucide-react';
import { CartContext } from '../context/CartContext';

export default function ProductDetails() {
  const { id } = useParams();
  const { addToCart } = useContext(CartContext);
  
  const [product, setProduct] = useState(null);
  const [selectedColor, setSelectedColor] = useState('White');
  const [selectedSize, setSelectedSize] = useState('41');
  const [mainImage, setMainImage] = useState('');
  const [activeTab, setActiveTab] = useState('Reviews');
  const [added, setAdded] = useState(false);

  useEffect(() => {
    // High-quality mock data for the minimalist fashion aesthetic
    const p = {
      id: id, 
      name: 'Shoes Reebok Zig Kinetica 3', 
      brand: 'Reebok',
      code: 'HR1325RO0-.-8',
      price: 199.00, 
      rating: 4.8, 
      reviewCount: 42,
      images: [
        'https://images.unsplash.com/photo-1608231387042-66d1773070a5?q=80&w=800&auto=format&fit=crop', 
        'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?q=80&w=800&auto=format&fit=crop', 
        'https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=800&auto=format&fit=crop', 
        'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?q=80&w=800&auto=format&fit=crop', 
      ],
      colors: ['White', 'Grey', 'Black'],
      sizes: ['40.5', '41', '42', '43', '43.5', '44', '44.5', '45', '46']
    };
    
    setProduct(p);
    setMainImage(p.images[0]);
  }, [id]);

  const handleAddToCart = () => {
    addToCart(product, selectedColor, selectedSize);
    setAdded(true);
    setTimeout(() => setAdded(false), 2000);
  };

  if (!product) return <div className="page-container">Loading...</div>;

  const renderStars = (rating) => {
    const full = Math.floor(rating || 4);
    const half = (rating || 4) % 1 !== 0;
    return '★'.repeat(full) + (half ? '⯨' : '') + '☆'.repeat(5 - full - (half ? 1 : 0));
  };

  return (
    <div className="page-container">
      <div className="text-small text-gray" style={{marginBottom: '2rem'}}>
        Clothes and shoes • Shoes • Reebok
      </div>

      <div className="product-view">
        {/* Left Column: Images */}
        <div>
          <div className="image-showcase">
            <img src={mainImage} alt={product.name} />
          </div>
          <div className="thumbnails">
            {product.images.map((img, idx) => (
              <div 
                key={idx} 
                className={`thumb ${mainImage === img ? 'active' : ''}`}
                onMouseEnter={() => setMainImage(img)}
                onClick={() => setMainImage(img)}
              >
                <img src={img} alt="thumbnail" />
              </div>
            ))}
            <div className="thumb" style={{background: 'transparent', border: '1px solid var(--border-light)', fontSize: '0.9rem', fontWeight: 500}}>
              +4 more
            </div>
          </div>
        </div>

        {/* Right Column: Product Details */}
        <div style={{paddingTop: '1rem'}}>
          <div className="brand-header">
            <div style={{display: 'flex', alignItems: 'center', gap: '0.5rem', fontWeight: 600}}>
              <div style={{width: '24px', height: '24px', background: '#000', borderRadius: '50%', color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '10px'}}>R</div>
              {product.brand}
            </div>
            <div className="text-gray text-small">{product.code}</div>
          </div>
          
          <h1 className="product-title">{product.name}</h1>
          
          <div style={{display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '2rem'}}>
            <span className="stars">{renderStars(product.rating)}</span>
            <span className="text-gray text-small">{product.reviewCount} reviews</span>
          </div>

          <div className="price-large">${product.price.toFixed(2)}</div>

          <div className="selector-label">Color <span style={{color: '#d1d1d1'}}>•</span> {selectedColor}</div>
          <div className="color-swatches">
            {product.colors.map((color, idx) => (
              <div 
                key={color} 
                className={`c-swatch ${selectedColor === color ? 'active' : ''}`}
                onClick={() => setSelectedColor(color)}
              >
                <img src={product.images[idx]} alt={color} />
              </div>
            ))}
          </div>

          <div className="selector-label" style={{marginTop: '2rem'}}>Size <span style={{color: '#d1d1d1'}}>•</span> EU Men</div>
          <div className="size-grid">
            {product.sizes.map(size => (
              <div 
                key={size}
                className={`size-btn ${selectedSize === size ? 'active' : ''}`}
                onClick={() => setSelectedSize(size)}
              >
                {size}
              </div>
            ))}
          </div>
          <div className="text-small" style={{color: '#a3c399', fontWeight: 500, marginTop: '1rem', cursor: 'pointer'}}>Size guide</div>

          <div className="action-row">
            <button className="btn-black" onClick={handleAddToCart} style={{background: added ? '#a3c399' : '#000', color: added ? '#000' : '#fff'}}>
              <ShoppingBag size={20} strokeWidth={2} />
              {added ? 'Added to Cart!' : 'Add to cart'}
            </button>
            <button className="btn-beige">
              <Heart size={20} strokeWidth={2} />
            </button>
          </div>

          <div className="text-small" style={{display: 'flex', alignItems: 'center', gap: '0.5rem', marginTop: '1.5rem', fontWeight: 500}}>
            <Truck size={18} /> Free delivery on orders over $30.0
          </div>
        </div>
      </div>

      {/* Bottom Tabs and Related Items */}
      <div className="tabs">
        <div className={`tab ${activeTab === 'Details' ? 'active' : ''}`} onClick={() => setActiveTab('Details')}>Details</div>
        <div className={`tab ${activeTab === 'Reviews' ? 'active' : ''}`} onClick={() => setActiveTab('Reviews')}>Reviews</div>
        <div className={`tab ${activeTab === 'Discussion' ? 'active' : ''}`} onClick={() => setActiveTab('Discussion')}>Discussion</div>
      </div>
      
      <div style={{marginBottom: '3rem'}}>
        {activeTab === 'Details' && <p>Excellent running shoes. It turns very sharply on the foot. Premium materials used throughout the construction ensuring longevity and comfort.</p>}
        {activeTab === 'Reviews' && <p><strong>Helen M.</strong> - Excellent running shoes. <br/><br/> <strong>Ann D.</strong> - Good shoes.</p>}
        {activeTab === 'Discussion' && <p>Join the discussion about Reebok Zig Kinetica 3.</p>}
      </div>

      <div className="minimal-grid">
        <div className="minimal-card">
          <div className="minimal-card-img">
            <img src="https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=400&auto=format&fit=crop" alt="shoe" />
          </div>
          <h4>Adidas Yeezy Boost 700</h4>
          <div className="price">$205.00</div>
        </div>
        <div className="minimal-card">
          <div className="minimal-card-img">
            <img src="https://images.unsplash.com/photo-1608231387042-66d1773070a5?q=80&w=400&auto=format&fit=crop" alt="shoe" />
          </div>
          <h4>New Balance BB550</h4>
          <div className="price">$210.00</div>
        </div>
        <div className="minimal-card">
          <div className="minimal-card-img">
            <img src="https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?q=80&w=400&auto=format&fit=crop" alt="shoe" />
          </div>
          <h4>ASICS Gel-Lyte III OG</h4>
          <div className="price">$196.50</div>
        </div>
        <div className="minimal-card">
          <div className="minimal-card-img">
            <img src="https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?q=80&w=400&auto=format&fit=crop" alt="shoe" />
          </div>
          <h4>Reebok Classic Leather</h4>
          <div className="price">$149.15</div>
        </div>
      </div>
    </div>
  );
}
