export default function ProductCard({ product }) {
  return (
    <div className="glass product-card">
      <div className="product-image" style={{ backgroundImage: `url(${product.imageUrl || 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=600&auto=format&fit=crop'})` }}>
      </div>
      <div className="product-info">
        <h3>{product.name}</h3>
        <p className="price">${product.price}</p>
        <button className="btn-accent" style={{width: '100%'}}>Add to Cart</button>
      </div>
    </div>
  );
}
