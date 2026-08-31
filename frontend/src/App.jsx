import { Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import AuthModal from './components/AuthModal';

function App() {
  return (
    <>
      <Navbar />
      <AuthModal />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/products" element={<div className="page-container"><h1>Products Page</h1></div>} />
        <Route path="/cart" element={<div className="page-container"><h1>Cart Page</h1></div>} />
      </Routes>
    </>
  );
}

export default App;
