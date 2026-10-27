<template>
  <div class="app-container">

    <div class="header">
      <h1>Vue CRUD Management System</h1>
      <p>Student, Employee, Inventory and Booking Management</p>
    </div>

    <div class="nav-buttons">

      <button
        class="nav-btn"
        @click="page='student'"
      >
        Student Course
      </button>

      <button
        class="nav-btn"
        @click="page='ticket'"
      >
        Ticket Booking
      </button>

      <button
        class="nav-btn"
        @click="page='leave'"
      >
        Employee Leave
      </button>

      <button
        class="nav-btn"
        @click="page='inventory'"
      >
        Inventory
      </button>

      <button
        class="nav-btn"
        @click="page='flight'"
      >
        Flight Booking
      </button>

      <button
        class="nav-btn"
        @click="page='marks'"
      >
        Student Marks
      </button>

    </div>

    <div class="content-card">

      <StudentCourse
        v-if="page==='student'"
      />

      <TicketBooking
        v-if="page==='ticket'"
      />

      <EmployeeLeave
        v-if="page==='leave'"
      />

      <InventoryManagement
        v-if="page==='inventory'"
      />

      <FlightBooking
        v-if="page==='flight'"
      />

      <StudentMarks
        v-if="page==='marks'"
      />

    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'

import StudentCourse from './components/StudentCourse.vue'
import TicketBooking from './components/TicketBooking.vue'
import EmployeeLeave from './components/EmployeeLeave.vue'
import InventoryManagement from './components/InventoryManagement.vue'
import FlightBooking from './components/FlightBooking.vue'
import StudentMarks from './components/StudentMarks.vue'

const page = ref('student')
</script>

<style>
*{
  margin:0;
  padding:0;
  box-sizing:border-box;
}

body{
  font-family:Segoe UI, sans-serif;
  background:#F5F0E8;
}

.app-container{
  max-width:1200px;
  margin:auto;
  padding:20px;
}

.header{
  text-align:center;
  margin-bottom:25px;
}

.header h1{
  color:#5D4037;
  margin-bottom:8px;
}

.header p{
  color:#7B6A58;
}

.nav-buttons{
  display:flex;
  flex-wrap:wrap;
  gap:12px;
  justify-content:center;
  margin-bottom:25px;
}

.nav-btn{
  border:none;
  background:#D7CCC8;
  color:#4E342E;
  padding:12px 18px;
  border-radius:8px;
  cursor:pointer;
  font-size:14px;
  transition:0.3s;
}

.nav-btn:hover{
  background:#C7B8B0;
}

.content-card{
  background:#FFFDF8;
  padding:25px;
  border-radius:12px;
  box-shadow:0 3px 10px rgba(0,0,0,0.08);
}
</style>
