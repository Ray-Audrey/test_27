<template>
  <div>
    <h2>Employee Leave Management</h2>

    <h3>Apply Leave</h3>

    <input v-model="empId" placeholder="Employee ID">
    <input v-model="empName" placeholder="Employee Name">
    <input v-model="leaveType" placeholder="Leave Type">

    <button @click="applyLeave">
      Apply Leave
    </button>

    <hr>

    <h3>Delete Leave</h3>

    <input v-model="deleteId" placeholder="Employee ID">

    <button @click="deleteLeave">
      Delete Leave
    </button>

    <hr>

    <h3>Update Leave</h3>

    <input v-model="oldId" placeholder="Old Employee ID">
    <input v-model="newId" placeholder="New Employee ID">
    <input v-model="newName" placeholder="Employee Name">
    <input v-model="newLeaveType" placeholder="Leave Type">

    <button @click="updateLeave">
      Update Leave
    </button>

    <hr>

    <h3>Applied Leaves</h3>

    <table border="1">
      <thead>
        <tr>
          <th>Employee ID</th>
          <th>Employee Name</th>
          <th>Leave Type</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="leave in leaves" :key="leave.id">
          <td>{{ leave.id }}</td>
          <td>{{ leave.name }}</td>
          <td>{{ leave.type }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const leaves = ref([])

const empId = ref('')
const empName = ref('')
const leaveType = ref('')

const deleteId = ref('')

const oldId = ref('')
const newId = ref('')
const newName = ref('')
const newLeaveType = ref('')

function applyLeave() {
  leaves.value.push({
    id: empId.value,
    name: empName.value,
    type: leaveType.value
  })

  empId.value = ''
  empName.value = ''
  leaveType.value = ''
}

function deleteLeave() {
  leaves.value = leaves.value.filter(
    leave => leave.id !== deleteId.value
  )

  deleteId.value = ''
}

function updateLeave() {
  const leave = leaves.value.find(
    leave => leave.id === oldId.value
  )

  if (leave) {
    leave.id = newId.value
    leave.name = newName.value
    leave.type = newLeaveType.value
  }

  oldId.value = ''
  newId.value = ''
  newName.value = ''
  newLeaveType.value = ''
}
</script>