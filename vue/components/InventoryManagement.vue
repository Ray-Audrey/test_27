<template>
  <div>
    <h2>Inventory Management</h2>

    <h3>Add Item</h3>

    <input v-model="itemId" placeholder="Item ID">
    <input v-model="itemName" placeholder="Item Name">
    <input v-model="price" placeholder="Price">
    <input v-model="quantity" placeholder="Quantity">

    <button @click="addItem">
      Add Item
    </button>

    <hr>

    <h3>Delete Item</h3>

    <input v-model="deleteId" placeholder="Item ID">

    <button @click="deleteItem">
      Delete
    </button>

    <hr>

    <h3>Update Item</h3>

    <input v-model="oldId" placeholder="Old Item ID">
    <input v-model="newId" placeholder="New Item ID">
    <input v-model="newName" placeholder="Item Name">
    <input v-model="newPrice" placeholder="Price">
    <input v-model="newQuantity" placeholder="Quantity">

    <button @click="updateItem">
      Update
    </button>

    <hr>

    <h3>Inventory List</h3>

    <table border="1">
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Price</th>
          <th>Quantity</th>
          <th>Total Value</th>
          <th>Increase Stock</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="item in inventory" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.name }}</td>
          <td>{{ item.price }}</td>
          <td>{{ item.quantity }}</td>
          <td>{{ totalValue(item) }}</td>

          <td>
            <button @click="increaseStock(item)">
              +10
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const inventory = ref([])

const itemId = ref('')
const itemName = ref('')
const price = ref('')
const quantity = ref('')

const deleteId = ref('')

const oldId = ref('')
const newId = ref('')
const newName = ref('')
const newPrice = ref('')
const newQuantity = ref('')

function addItem() {
  inventory.value.push({
    id: itemId.value,
    name: itemName.value,
    price: price.value,
    quantity: quantity.value
  })

  itemId.value = ''
  itemName.value = ''
  price.value = ''
  quantity.value = ''
}

function deleteItem() {
  inventory.value = inventory.value.filter(
    item => item.id !== deleteId.value
  )

  deleteId.value = ''
}

function updateItem() {
  const item = inventory.value.find(
    item => item.id === oldId.value
  )

  if (item) {
    item.id = newId.value
    item.name = newName.value
    item.price = newPrice.value
    item.quantity = newQuantity.value
  }
}

function increaseStock(item) {
  item.quantity = Number(item.quantity) + 10
}

function totalValue(item) {
  return Number(item.price) * Number(item.quantity)
}
</script>