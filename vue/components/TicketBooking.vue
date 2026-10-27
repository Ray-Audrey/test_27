<template>
  <div>
    <h2>Ticket Booking System</h2>

    <h3>Book Ticket</h3>

    <input v-model="ticketId" placeholder="Ticket ID">
    <input v-model="passengerName" placeholder="Passenger Name">
    <input v-model="destination" placeholder="Destination">

    <button @click="bookTicket">
      Book Ticket
    </button>

    <hr>

    <h3>Cancel Ticket</h3>

    <input
      v-model="deleteTicketId"
      placeholder="Ticket ID"
    >

    <button @click="cancelTicket">
      Cancel Ticket
    </button>

    <hr>

    <h3>Update Ticket</h3>

    <input
      v-model="oldTicketId"
      placeholder="Old Ticket ID"
    >

    <input
      v-model="newTicketId"
      placeholder="New Ticket ID"
    >

    <input
      v-model="newPassengerName"
      placeholder="Passenger Name"
    >

    <input
      v-model="newDestination"
      placeholder="Destination"
    >

    <button @click="updateTicket">
      Update Ticket
    </button>

    <hr>

    <h3>All Ticket Bookings</h3>

    <table border="1">
      <thead>
        <tr>
          <th>Ticket ID</th>
          <th>Passenger Name</th>
          <th>Destination</th>
        </tr>
      </thead>

      <tbody>
        <tr
          v-for="ticket in tickets"
          :key="ticket.id"
        >
          <td>{{ ticket.id }}</td>
          <td>{{ ticket.name }}</td>
          <td>{{ ticket.destination }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const tickets = ref([])

const ticketId = ref('')
const passengerName = ref('')
const destination = ref('')

const deleteTicketId = ref('')

const oldTicketId = ref('')
const newTicketId = ref('')
const newPassengerName = ref('')
const newDestination = ref('')

function bookTicket() {
  tickets.value.push({
    id: ticketId.value,
    name: passengerName.value,
    destination: destination.value
  })

  ticketId.value = ''
  passengerName.value = ''
  destination.value = ''
}

function cancelTicket() {
  tickets.value = tickets.value.filter(
    ticket => ticket.id !== deleteTicketId.value
  )

  deleteTicketId.value = ''
}

function updateTicket() {
  const ticket = tickets.value.find(
    ticket => ticket.id === oldTicketId.value
  )

  if (ticket) {
    ticket.id = newTicketId.value
    ticket.name = newPassengerName.value
    ticket.destination = newDestination.value
  }

  oldTicketId.value = ''
  newTicketId.value = ''
  newPassengerName.value = ''
  newDestination.value = ''
}
</script>