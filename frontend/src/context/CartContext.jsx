import { createContext, useState, useEffect } from 'react';

export const CartContext = createContext(null);

export const CartProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState(() => {
    const saved = localStorage.getItem('ecommerce_cart');
    return saved ? JSON.parse(saved) : [];
  });

  useEffect(() => {
    localStorage.setItem('ecommerce_cart', JSON.stringify(cartItems));
  }, [cartItems]);

  const addToCart = (product, selectedColor, selectedSize) => {
    setCartItems(prev => {
      // Check if item with same id, color, and size exists
      const existingItemIndex = prev.findIndex(item => 
        item.id === product.id && 
        item.selectedColor === selectedColor && 
        item.selectedSize === selectedSize
      );

      if (existingItemIndex >= 0) {
        const newCart = [...prev];
        newCart[existingItemIndex].quantity += 1;
        return newCart;
      }

      return [...prev, { ...product, selectedColor, selectedSize, quantity: 1 }];
    });
  };

  const removeFromCart = (index) => {
    setCartItems(prev => prev.filter((_, i) => i !== index));
  };

  const updateQuantity = (index, newQuantity) => {
    if (newQuantity < 1) return;
    setCartItems(prev => {
      const newCart = [...prev];
      newCart[index].quantity = newQuantity;
      return newCart;
    });
  };

  const cartCount = cartItems.reduce((sum, item) => sum + item.quantity, 0);
  const cartTotal = cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);

  return (
    <CartContext.Provider value={{ cartItems, addToCart, removeFromCart, updateQuantity, cartCount, cartTotal }}>
      {children}
    </CartContext.Provider>
  );
};
