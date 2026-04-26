import React from 'react';
import { ChefHat, Timer } from 'lucide-react';

interface OrderItem {
  id: number;
  productName: string;
  quantity: number;
  notes?: string;
}

interface Order {
  id: number;
  tableNumber: string;
  status: 'PENDING' | 'PREPARING' | 'READY';
  items: OrderItem[];
  timestamp: string;
}

const KitchenDisplayPage: React.FC = () => {
  const [orders] = React.useState<Order[]>([
    {
      id: 101,
      tableNumber: 'MESA 4',
      status: 'PENDING',
      timestamp: '12:30 PM',
      items: [
        { id: 1, productName: 'Tacos Pastor', quantity: 3, notes: 'Sin piña' },
        { id: 2, productName: 'Gringa', quantity: 1 }
      ]
    },
    {
      id: 102,
      tableNumber: 'MESA 2',
      status: 'PREPARING',
      timestamp: '12:35 PM',
      items: [
        { id: 3, productName: 'Costra de Queso', quantity: 2 }
      ]
    }
  ]);

  const getStatusColor = (status: Order['status']) => {
    switch (status) {
      case 'PENDING': return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'PREPARING': return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'READY': return 'bg-green-100 text-green-800 border-green-200';
      default: return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6 flex items-center">
        <ChefHat className="mr-3" /> Monitor de Cocina
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {orders.map((order) => (
          <div key={order.id} className={`border rounded-lg shadow-lg p-4 ${getStatusColor(order.status)}`}>
            <div className="flex justify-between items-center mb-4 border-b pb-2">
              <span className="font-bold text-lg">#{order.id} - {order.tableNumber}</span>
              <span className="flex items-center text-sm">
                <Timer className="mr-1 w-4 h-4" /> {order.timestamp}
              </span>
            </div>

            <ul className="space-y-2">
              {order.items.map((item) => (
                <li key={item.id} className="flex justify-between items-start">
                  <div>
                    <span className="font-bold text-lg mr-2">{item.quantity}x</span>
                    <span className="text-lg">{item.productName}</span>
                    {item.notes && <p className="text-sm text-red-600 italic">Nota: {item.notes}</p>}
                  </div>
                </li>
              ))}
            </ul>

            <div className="mt-6 flex space-x-2">
              {order.status === 'PENDING' && (
                <button className="flex-1 bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded transition-colors">
                  Empezar
                </button>
              )}
              {order.status === 'PREPARING' && (
                <button className="flex-1 bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded transition-colors">
                  Listo
                </button>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default KitchenDisplayPage;