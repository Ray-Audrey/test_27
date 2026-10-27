import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// /api calls are forwarded to the Spring Boot backend (no CORS setup needed)
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: { '/api': 'http://localhost:8080' },
  },
})
