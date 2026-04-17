import React, { useState } from 'react';
import { PlusCircle, ShoppingCart } from 'lucide-react';

const OrdersPage: React.FC = () => {
  // Mock data for products and orders
  const [products] = useState([
    { id: 1, name: 'Tacos de Pastor', price: 25.0, category: 'Plancha' },
    { id: 2, name: 'Tacos de Suadero', price: 28.0, category: 'Plancha' },
    { id: 3, name: 'Quesadilla', price: 35.0, category: 'Taqueria' },
    { id: 4, name: 'Coca Cola', price: 20.0, category: 'Barra' },
  ]);

  const [currentOrder, setCurrentOrder] = useState<{ productId: number; quantity: number }[]>([]);

  const addToOrder = (productId: number) => {
    setCurrentOrder((prev) => {
      const existing = prev.find((p) => p.productId === productId);
      if (existing) {
        return prev.map((p) => (p.productId === productId ? { ...p, quantity: p.quantity + 1 } : p));
      }
      return [...prev, { productId, quantity: 1 }];
    });
  };

  const calculateTotal = () => {
    return currentOrder.reduce((total, item) => {
      const product = products.find((p) => p.id === item.productId);
      return total + (product ? product.price * item.quantity : 0);
    }, 0);
  };

  return (
    <div className="container mx-auto p-4 flex flex-col md:flex-row h-screen">
      {/* Product Grid */}
      <div className="md:w-2/3 pr-4 overflow-y-auto">
        <h1 className="text-3xl font-bold mb-6">Nuevo Pedido</h1>
        <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
          {products.map((product) => (
            <div
              key={product.id}
              className="bg-white p-4 rounded-lg shadow-md hover:shadow-lg transition-shadow cursor-pointer border border-gray-200"
              onClick={() => addToOrder(product.id)}
            >
              <h3 className="text-lg font-semibold">{product.name}</h3>
              <p className="text-gray-600">${product.price.toFixed(2)}</p>
              <span className="inline-block mt-2 px-2 py-1 text-xs font-semibold text-white bg-blue-500 rounded-full">
                {product.category}
              </span>
            </div>
          ))}
        </div>
      </div>

      {/* Order Summary */}
      <div className="md:w-1/3 bg-white p-6 rounded-lg shadow-xl border-l border-gray-200 flex flex-col">
        <h2 className="text-2xl font-bold mb-4 flex items-center">
          <ShoppingCart className="mr-2" /> Comanda
        </h2>

        <div className="flex-1 overflow-y-auto mb-4">
          {currentOrder.length === 0 ? (
            <p className="text-gray-500 text-center py-10">Agrega productos al pedido</p>
          ) : (
            <ul className="divide-y divide-gray-200">
              {currentOrder.map((item) => {
                const product = products.find((p) => p.id === item.productId);
                return (
                  <li key={item.productId} className="py-2 flex justify-between items-center">
                    <div>
                      <span className="font-medium">{product?.name}</span>
                      <div className="text-sm text-gray-500">x{item.quantity}</div>
                    </div>
                    <span className="font-semibold">
                      ${((product?.price || 0) * item.quantity).toFixed(2)}
                    </span>
                  </li>
                );
              })}
            </ul>
          )}
        </div>

        <div className="border-t border-gray-200 pt-4 mt-auto">
          <div className="flex justify-between items-center text-xl font-bold mb-4">
            <span>Total:</span>
            <span>${calculateTotal().toFixed(2)}</span>
          </div>
          <button
            className="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-4 rounded-lg flex justify-center items-center transition-colors"
            disabled={currentOrder.length === 0}
          >
            <PlusCircle className="mr-2" /> Generar Comanda
          </button>
        </div>
      </div>
    </div>
  );
};

export default OrdersPage;