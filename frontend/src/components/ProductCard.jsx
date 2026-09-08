import { Link } from 'react-router-dom';

export default function ProductCard({ product }) {
  return (
    <div className="minimal-card">
      <Link to={`/product/${product.id}`} style={{color: 'inherit'}}>
        <div className="minimal-card-img">
          <img src={product.imageUrl} alt={product.name} />
        </div>
        <h4>{product.name}</h4>
        <div className="price">${parseFloat(product.price).toFixed(2)}</div>
      </Link>
    </div>
  );
}
