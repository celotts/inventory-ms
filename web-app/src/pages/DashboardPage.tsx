import React from 'react';

const DashboardPage: React.FC = () => {
  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-xl font-semibold mb-2">Ventas del Día</h3>
          <p className="text-3xl font-bold text-green-600">$1,250.00</p>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-xl font-semibold mb-2">Pedidos Pendientes</h3>
          <p className="text-3xl font-bold text-yellow-600">5</p>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-xl font-semibold mb-2">Productos Críticos</h3>
          <p className="text-3xl font-bold text-red-600">3</p>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;