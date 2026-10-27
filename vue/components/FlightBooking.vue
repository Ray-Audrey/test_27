<template>
  <div>
    <h2>Flight Booking System</h2>

    <h3>Book Flight</h3>

    <input v-model="flightId" placeholder="Flight ID">
    <input v-model="passengerName" placeholder="Passenger Name">
    <input v-model="source" placeholder="Source">
    <input v-model="destination" placeholder="Destination">

    <button @click="bookFlight">
      Book Flight
    </button>

    <hr>

    <h3>Cancel Booking</h3>

    <input v-model="deleteFlightId" placeholder="Flight ID">

    <button @click="cancelBooking">
      Cancel
    </button>

    <hr>

    <h3>Update Booking</h3>

    <input v-model="oldFlightId" placeholder="Old Flight ID">
    <input v-model="newFlightId" placeholder="New Flight ID">
    <input v-model="newPassengerName" placeholder="Passenger Name">
    <input v-model="newSource" placeholder="Source">
    <input v-model="newDestination" placeholder="Destination">

    <button @click="updateBooking">
      Update
    </button>

    <hr>

    <h3>Flight Bookings</h3>

    <table border="1">
      <thead>
        <tr>
          <th>Flight ID</th>
          <th>Passenger</th>
          <th>Source</th>
          <th>Destination</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="booking in bookings" :key="booking.id">
          <td>{{ booking.id }}</td>
          <td>{{ booking.name }}</td>
          <td>{{ booking.source }}</td>
          <td>{{ booking.destination }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const bookings = ref([])

const flightId = ref('')
const passengerName = ref('')
const source = ref('')
const destination = ref('')

const deleteFlightId = ref('')

const oldFlightId = ref('')
const newFlightId = ref('')
const newPassengerName = ref('')
const newSource = ref('')
const newDestination = ref('')

function bookFlight() {
  bookings.value.push({
    id: flightId.value,
    name: passengerName.value,
    source: source.value,
    destination: destination.value
  })
}

function cancelBooking() {
  bookings.value = bookings.value.filter(
    booking => booking.id !== deleteFlightId.value
  )
}

function updateBooking() {
  const booking = bookings.value.find(
    booking => booking.id === oldFlightId.value
  )

  if (booking) {
    booking.id = newFlightId.value
    booking.name = newPassengerName.value
    booking.source = newSource.value
    booking.destination = newDestination.value
  }
}
</script>