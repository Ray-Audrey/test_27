<template>
  <div>
    <h2>Student Marks Management</h2>

    <h3>Add Student Marks</h3>

    <input v-model="id" placeholder="Student ID">
    <input v-model="name" placeholder="Student Name">
    <input v-model="m1" placeholder="Mark 1">
    <input v-model="m2" placeholder="Mark 2">
    <input v-model="m3" placeholder="Mark 3">

    <button @click="addStudent">
      Add Student
    </button>

    <hr>

    <h3>Student Marks</h3>

    <table border="1">
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Mark1</th>
          <th>Mark2</th>
          <th>Mark3</th>
          <th>Sum</th>
          <th>Average</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="student in students" :key="student.id">
          <td>{{ student.id }}</td>
          <td>{{ student.name }}</td>
          <td>{{ student.m1 }}</td>
          <td>{{ student.m2 }}</td>
          <td>{{ student.m3 }}</td>
          <td>{{ getSum(student) }}</td>
          <td>{{ getAverage(student) }}</td>
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
const m1 = ref('')
const m2 = ref('')
const m3 = ref('')

function addStudent() {
  students.value.push({
    id: id.value,
    name: name.value,
    m1: m1.value,
    m2: m2.value,
    m3: m3.value
  })
}

function getSum(student) {
  return (
    Number(student.m1) +
    Number(student.m2) +
    Number(student.m3)
  )
}

function getAverage(student) {
  return (
    getSum(student) / 3
  ).toFixed(2)
}
</script>