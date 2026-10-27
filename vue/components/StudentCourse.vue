<template>
  <div>
    <h2>Student Course Management</h2>

    <h3>Insert Student</h3>
    <input v-model="id" placeholder="Student ID" />
    <input v-model="name" placeholder="Student Name" />
    <input v-model="course" placeholder="Course" />
    <button @click="insertRecord">Insert</button>

    <hr />

    <h3>Delete Student</h3>
    <input v-model="deleteId" placeholder="Student ID" />
    <button @click="deleteRecord">Delete</button>

    <hr />

    <h3>Update Student</h3>
    <input v-model="oldId" placeholder="Old ID" />
    <input v-model="newId" placeholder="New ID" />
    <input v-model="newName" placeholder="Student Name" />
    <input v-model="newCourse" placeholder="Course" />
    <button @click="updateRecord">Update</button>

    <hr />

    <h3>All Records</h3>

    <table border="1">
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Course</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="student in students" :key="student.id">
          <td>{{ student.id }}</td>
          <td>{{ student.name }}</td>
          <td>{{ student.course }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const students = ref([])

const id = ref('')
const name = ref('')
const course = ref('')

const deleteId = ref('')

const oldId = ref('')
const newId = ref('')
const newName = ref('')
const newCourse = ref('')

function insertRecord() {
  students.value.push({
    id: id.value,
    name: name.value,
    course: course.value
  })

  id.value = ''
  name.value = ''
  course.value = ''
}

function deleteRecord() {
  students.value = students.value.filter(
    s => s.id !== deleteId.value
  )

  deleteId.value = ''
}

function updateRecord() {
  const student = students.value.find(
    s => s.id === oldId.value
  )

  if (student) {
    student.id = newId.value
    student.name = newName.value
    student.course = newCourse.value
  }

  oldId.value = ''
  newId.value = ''
  newName.value = ''
  newCourse.value = ''
}
</script>

<style>
body {
  font-family: Arial, sans-serif;
}

input {
  margin: 5px;
  padding: 5px;
}

button {
  padding: 5px 10px;
  margin: 5px;
}

table {
  border-collapse: collapse;
}

th,
td {
  padding: 8px;
}
</style>