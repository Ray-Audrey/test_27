<template>

  <div>

    <h2>Available Food</h2>

    <hr>


    <div
      v-for="food in foods"
      :key="food.id"
      class="food-item"
    >

      <span class="food-name">

        {{ food.emoji }}
        {{ food.name }}

      </span>


      <span>

        ₹{{ food.price }}

      </span>


      <button
        @click="cart.addToCart(food)"
      >

        Add to Cart

      </button>

    </div>


    <hr>


    <p>

      🛒 Cart:
      {{ cart.totalItems }}
      items

    </p>

  </div>

</template>


<script>

import { useCartStore } from '../components/stores/cart'

export default {

  name: 'FoodMenu',


  data() {

    return {

      cart: useCartStore(),


      foods: [

        {
          id: 1,
          name: 'Pizza',
          emoji: '🍕',
          price: 250
        },

        {
          id: 2,
          name: 'Burger',
          emoji: '🍔',
          price: 150
        },

        {
          id: 3,
          name: 'Pasta',
          emoji: '🍝',
          price: 200
        },

        {
          id: 4,
          name: 'Coke',
          emoji: '🥤',
          price: 80
        }

      ]

    }

  }

}

</script>


<style scoped>

.food-item {

  display: flex;

  align-items: center;

  gap: 30px;

  padding: 15px 0;

}

.food-name {

  width: 200px;

  font-size: 18px;

}

button {

  padding: 8px 15px;

}

</style>