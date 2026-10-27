<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()
const students = ref([])
const selections = ref([])
const message = ref('')
const error = ref('')

const studentForm = ref({ name: '', email: '', department: '', cgpa: 0 })
const selectionForm = ref({ studentId: '', scholarshipName: '', amount: 0, status: 'Selected' })

async function loadData() {
  error.value = ''
  try {
    const [studentResponse, selectionResponse] = await Promise.all([
      api.get('/students'),
      api.get('/selections')
    ])
    students.value = studentResponse.data
    selections.value = selectionResponse.data
    if (!selectionForm.value.studentId && students.value.length) {
      selectionForm.value.studentId = students.value[0].id
    }
  } catch {
    error.value = 'Could not load data. Check that the backend and MongoDB are running.'
  }
}

async function addStudent() {
  message.value = ''
  error.value = ''
  try {
    await api.post('/students', studentForm.value)
    studentForm.value = { name: '', email: '', department: '', cgpa: 0 }
    message.value = 'Student added.'
    await loadData()
    if (students.value.length) selectionForm.value.studentId = students.value[0].id
  } catch {
    error.value = 'Could not add student.'
  }
}

async function deleteStudent(id) {
  if (!confirm('Delete this student?')) return
  try {
    await api.delete(`/students/${id}`)
    message.value = 'Student deleted.'
    await loadData()
  } catch {
    error.value = 'Could not delete student.'
  }
}

async function addSelection() {
  error.value = ''
  message.value = ''
  try {
    await api.post('/selections', selectionForm.value)
    message.value = 'Scholarship selection added.'
    selectionForm.value.scholarshipName = ''
    selectionForm.value.amount = 0
    await loadData()
  } catch {
    error.value = 'Could not add selection. Choose a student first.'
  }
}

async function deleteSelection(id) {
  if (!confirm('Delete this scholarship selection?')) return
  try {
    await api.delete(`/selections/${id}`)
    message.value = 'Selection deleted.'
    await loadData()
  } catch {
    error.value = 'Could not delete selection.'
  }
}

function studentName(id) {
  return students.value.find(s => s.id === id)?.name || 'Unknown student'
}

async function logout() {
  await auth.logout()
  router.push('/login')
}

onMounted(loadData)
</script>

<template>
  <div class="page">
    <header class="topbar">
      <div>
        <h1>Student Scholarship Manager</h1>
      </div>
      <div class="user-area">
        <span class="muted">Signed in as {{ auth.username }}</span>
        <button class="secondary" @click="logout">Log out</button>
      </div>
    </header>

    <p v-if="message" class="notice">{{ message }}</p>
    <p v-if="error" class="error notice">{{ error }}</p>

    <section class="panel">
      <h2>Add student</h2>
      <form class="form-grid" @submit.prevent="addStudent">
        <div><label>Full name</label><input v-model="studentForm.name" required placeholder="Asha Kumar" /></div>
        <div><label>Email</label><input v-model="studentForm.email" type="email" required placeholder="asha@example.com" /></div>
        <div><label>Department</label><input v-model="studentForm.department" required placeholder="CSE" /></div>
        <div><label>CGPA</label><input v-model.number="studentForm.cgpa" type="number" min="0" max="10" step="0.01" required /></div>
        <button class="primary" type="submit">Add student</button>
      </form>
    </section>

    <section class="panel">
      <div class="section-heading"><h2>Students</h2><span class="count">{{ students.length }}</span></div>
      <div class="table-wrap">
        <table>
          <thead><tr><th>Name</th><th>Email</th><th>Department</th><th>CGPA</th><th></th></tr></thead>
          <tbody>
            <tr v-for="s in students" :key="s.id">
              <td>{{ s.name }}</td><td>{{ s.email }}</td><td>{{ s.department }}</td><td>{{ s.cgpa }}</td>
              <td><button class="danger" @click="deleteStudent(s.id)">Delete</button></td>
            </tr>
            <tr v-if="!students.length"><td colspan="5" class="empty">No students added yet.</td></tr>
          </tbody>
        </table>
      </div>
    </section>

    <section class="panel">
      <h2>Add scholarship selection</h2>
      <form class="form-grid" @submit.prevent="addSelection">
        <div class="wide"><label>Student</label>
          <select v-model="selectionForm.studentId" required>
            <option disabled value="">Choose student</option>
            <option v-for="s in students" :key="s.id" :value="s.id">{{ s.name }} — {{ s.department }}</option>
          </select>
        </div>
        <div><label>Scholarship name</label><input v-model="selectionForm.scholarshipName" required placeholder="Merit Scholarship" /></div>
        <div><label>Amount (₹)</label><input v-model.number="selectionForm.amount" type="number" min="0" required /></div>
        <div><label>Status</label>
          <select v-model="selectionForm.status"><option>Selected</option><option>Pending</option><option>Rejected</option></select>
        </div>
        <button class="primary" type="submit" :disabled="!students.length">Add selection</button>
      </form>
    </section>

    <section class="panel">
      <div class="section-heading"><h2>Scholarship selections</h2><span class="count">{{ selections.length }}</span></div>
      <div class="table-wrap">
        <table>
          <thead><tr><th>Student</th><th>Scholarship</th><th>Amount</th><th>Status</th><th></th></tr></thead>
          <tbody>
            <tr v-for="s in selections" :key="s.id">
              <td>{{ studentName(s.studentId) }}</td><td>{{ s.scholarshipName }}</td>
              <td>₹{{ Number(s.amount).toLocaleString('en-IN') }}</td><td><span class="status">{{ s.status }}</span></td>
              <td><button class="danger" @click="deleteSelection(s.id)">Delete</button></td>
            </tr>
            <tr v-if="!selections.length"><td colspan="5" class="empty">No scholarship selections yet.</td></tr>
          </tbody>
        </table>
      </div>
    </section>
    
  </div>
</template>
