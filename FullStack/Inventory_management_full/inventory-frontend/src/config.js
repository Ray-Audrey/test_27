// The only file that differs between the two projects.
export default {
  title: 'Inventory Management',
  main: {
    label: 'Products',
    path: '/api/products',
    id: 'pid',
    fields: [
      { name: 'name', type: 'text' },
      { name: 'category', type: 'select', options: ['Electronics', 'Furniture', 'Stationery', 'Grocery', 'Other'] },
      { name: 'price', type: 'number' },
      { name: 'quantity', type: 'number' },
    ],
  },
  second: {
    label: 'Stock Movements',
    path: '/api/stock',
    id: 'sid',
    fields: [
      { name: 'productId', type: 'ref' },
      { name: 'movementType', type: 'select', options: ['Purchase', 'Sale', 'Return'] },
      { name: 'quantity', type: 'number' },
    ],
  },
  reports: [
    { label: 'Purchased', path: '/api/products/purchased' },
    { label: 'Large movements', path: '/api/products/large-movements' },
    { label: 'Electronics', path: '/api/products/electronics-movements' },
    { label: 'Premium sales', path: '/api/products/premium-sales' },
    { label: 'Total moved', path: '/api/products/total-moved' },
    { label: '10+ units', path: '/api/products/bulk-movements' },
    { label: 'Sorted by price', path: '/api/products/sorted-by-price' },
    { label: 'Net movement', path: '/api/products/net-movement' },
  ],
}
