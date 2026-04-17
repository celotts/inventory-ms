import React, { useState } from 'react';
import { Package, AlertTriangle, Plus, Search } from 'lucide-react';

interface Product {
  id: string;
  name: string;
  category: string;
  stock: number;
  minStock: number;
  unit: string;
}

const InventoryPage: React.FC = () => {
  const [products] = useState<Product[]>([
    { id: '1', name: 'Carne al Pastor', category: 'Insumos', stock: 15, minStock: 20, unit: 'kg' },
    { id: '2', name: 'Tortillas', category: 'Insumos', stock: 50, minStock: 10, unit: 'kg' },
    { id: '3', name: 'Cebolla', category: 'Insumos', stock: 2, minStock: 5, unit: 'kg' },
    { id: '4', name: 'Refresco Cola', category: 'Bebidas', stock: 100, minStock: 24, unit: 'pza' },
  ]);

  return (
    <div className="container mx-auto p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold flex items-center">
          <Package className="mr-3" /> Control de Inventario
        </h1>
        <button className="bg-primary text-white px-4 py-2 rounded-lg flex items-center hover:bg-gray-800 transition-colors">
          <Plus className="mr-2 w-5 h-5" /> Agregar Producto
        </button>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
        <div className="bg-white p-4 rounded-lg shadow border-l-4 border-blue-500">
          <p className="text-gray-500 text-sm uppercase font-bold">Total Productos</p>
          <p className="text-2xl font-bold">{products.length}</p>
        </div>
        <div className="bg-white p-4 rounded-lg shadow border-l-4 border-red-500">
          <p className="text-gray-500 text-sm uppercase font-bold">Stock Bajo</p>
          <p className="text-2xl font-bold text-red-600">
            {products.filter(p => p.stock <= p.minStock).length}
          </p>
        </div>
      </div>

      {/* Search and Filters */}
      <div className="bg-white p-4 rounded-lg shadow mb-6 flex flex-col md:flex-row gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 w-5 h-5" />
          <input
            type="text"
            placeholder="Buscar producto..."
            className="w-full pl-10 pr-4 py-2 border rounded-md focus:ring-2 focus:ring-primary focus:outline-none"
          />
        </div>
        <select className="border rounded-md px-4 py-2 bg-white">
          <option>Todas las categorías</option>
          <option>Insumos</option>
          <option>Bebidas</option>
          <option>Abarrotes</option>
        </select>
      </div>

      {/* Table */}
      <div className="bg-white shadow rounded-lg overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Producto</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Categoría</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Stock</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Mínimo</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estado</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {products.map((product) => (
              <tr key={product.id} className="hover:bg-gray-50 transition-colors">
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{product.name}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{product.category}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {product.stock} {product.unit}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {product.minStock} {product.unit}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  {product.stock <= product.minStock ? (
                    <span className="px-2 py-1 text-xs font-bold leading-5 rounded-full bg-red-100 text-red-800 flex items-center w-fit">
                      <AlertTriangle className="w-3 h-3 mr-1" /> Reordenar
                    </span>
                  ) : (
                    <span className="px-2 py-1 text-xs font-bold leading-5 rounded-full bg-green-100 text-green-800">
                      Ok
                    </span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default InventoryPage;