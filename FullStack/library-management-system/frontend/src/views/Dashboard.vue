<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const router = useRouter()
const books = ref([])
const borrowed = ref([])
const title = ref('')
const author = ref('')
const message = ref('')
const error = ref('')
const borrowedIds = computed(() => new Set(borrowed.value.map(item => item.bookId)))

async function load() {
  const [bookResult, borrowedResult] = await Promise.all([api.get('/books'), api.get('/borrowed')])
  books.value = bookResult.data
  borrowed.value = borrowedResult.data
}
async function addBook() {
  error.value = ''
  message.value = ''
  try {
    await api.post('/books', { title: title.value, author: author.value })
    title.value = ''
    author.value = ''
    message.value = 'Book added.'
    await load()
  } catch (e) { error.value = e.response?.data?.message || 'Could not add book.' }
}
async function borrowBook(book) {
  error.value = ''
  message.value = ''
  try {
    await api.post(`/borrow/${book.id}`)
    message.value = `Borrowed "${book.title}".`
    await load()
  } catch (e) { error.value = e.response?.data?.message || 'Could not borrow book.' }
}
async function returnBook(record) {
  error.value = ''
  message.value = ''
  try {
    await api.delete(`/borrowed/${record.id}`)
    message.value = 'Book returned.'
    await load()
  } catch (e) { error.value = e.response?.data?.message || 'Could not return book.' }
}
function bookTitle(id) { return books.value.find(book => book.id === id)?.title || 'Book no longer listed' }
async function signOut() {
  await auth.logout()
  router.push('/login')
}
onMounted(async () => {
  try { await load() } catch { error.value = 'Unable to load library. Is the backend and MongoDB running?' }
})
</script>

<template>
  <div class="app-shell">
    <header class="topbar">
      <div class="brand"><span class="brand-icon">LM</span><span>Library Manager</span></div>
      <div class="user-area"><span>{{ auth.username }}</span><button class="secondary" @click="signOut">Sign out</button></div>
    </header>
    <main class="content">
      <div class="page-heading">
        <div><p class="eyebrow">LIBRARY</p><h1>Books</h1><p class="muted">Add books and track borrowing in one place.</p></div>
        <div class="stats"><div class="stat"><strong>{{ books.length }}</strong><span>Total books</span></div><div class="stat"><strong>{{ borrowed.length }}</strong><span>Borrowed</span></div></div>
      </div>

      <p v-if="message" class="notice">{{ message }}</p>
      <p v-if="error" class="error notice">{{ error }}</p>

      <section class="panel">
        <h2>Add a book</h2>
        <form class="add-form" @submit.prevent="addBook">
          <div><label>Book title</label><input v-model="title" placeholder="e.g. The Alchemist" required></div>
          <div><label>Author</label><input v-model="author" placeholder="e.g. Paulo Coelho" required></div>
          <button class="primary" type="submit">Add book</button>
        </form>
      </section>

      <section class="panel">
        <div class="section-title"><h2>Book collection</h2><span class="count">{{ books.length }} records</span></div>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Book</th><th>Author</th><th>Status</th><th>Action</th></tr></thead>
            <tbody>
              <tr v-for="book in books" :key="book.id">
                <td><strong>{{ book.title }}</strong><small>ID: {{ book.id }}</small></td>
                <td>{{ book.author || '—' }}</td>
                <td><span :class="['status', borrowedIds.has(book.id) ? 'borrowed' : 'available']">{{ borrowedIds.has(book.id) ? 'Borrowed' : 'Available' }}</span></td>
                <td><button v-if="!borrowedIds.has(book.id)" class="small-action" @click="borrowBook(book)">Borrow</button><span v-else class="muted">Currently borrowed</span></td>
              </tr>
              <tr v-if="books.length === 0"><td colspan="4" class="empty">No books yet. Add the first book above.</td></tr>
            </tbody>
          </table>
        </div>
      </section>

      <section class="panel">
        <div class="section-title"><h2>Borrowed books</h2><span class="count">{{ borrowed.length }} records</span></div>
        <div v-if="borrowed.length" class="borrowed-list">
          <div v-for="record in borrowed" :key="record.id" class="borrowed-row">
            <div><strong>{{ bookTitle(record.bookId) }}</strong><small>Book ID: {{ record.bookId }}</small></div>
            <button class="secondary" @click="returnBook(record)">Return book</button>
          </div>
        </div>
        <p v-else class="empty">No books are currently borrowed.</p>
      </section>
    </main>
    <footer>Library Management System · Vue + Spring Boot + MongoDB</footer>
  </div>
</template>
