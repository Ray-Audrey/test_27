// The only file that differs between the projects.
export default {
  title: 'Movie Ticket Management',
  main: {
    label: 'Movies',
    path: '/api/movies',
    id: 'mid',
    fields: [
      { name: 'title', type: 'text' },
      { name: 'genre', type: 'select', options: ['Action', 'Comedy', 'Drama', 'Horror', 'Romance', 'Sci-Fi'] },
      { name: 'ticketPrice', type: 'number' },
      { name: 'availableSeats', type: 'number' },
    ],
  },
  second: {
    label: 'Bookings',
    path: '/api/bookings',
    id: 'bid',
    fields: [
      { name: 'movieId', type: 'ref' },
      { name: 'customerName', type: 'text' },
      { name: 'showTime', type: 'select', options: ['Morning', 'Matinee', 'Evening', 'Night'] },
      { name: 'seats', type: 'number' },
    ],
  },
  reports: [
    { label: 'Evening shows', path: '/api/movies/evening-bookings' },
    { label: 'Group bookings', path: '/api/movies/group-bookings' },
    { label: 'Action movies', path: '/api/movies/action-bookings' },
    { label: 'Premium night', path: '/api/movies/premium-night-bookings' },
    { label: 'Total seats', path: '/api/movies/total-seats' },
    { label: '2+ seats', path: '/api/movies/at-least-two-seats' },
    { label: 'Sorted by price', path: '/api/movies/sorted-by-price' },
    { label: 'Revenue', path: '/api/movies/revenue' },
  ],
}
