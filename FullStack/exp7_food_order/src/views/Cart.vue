<template>

  <div>

    <h2>
      🛒 Cart
      ({{ cart.totalItems }} items)
    </h2>

    <hr>


    <!-- Empty Cart -->

    <div v-if="cart.items.length === 0">

      <p>
        Your cart is empty.
      </p>

      <RouterLink to="/order/menu">

        <button>
          Go to Food Menu
        </button>

      </RouterLink>

    </div>


    <!-- Cart Items -->

    <div v-else>

      <div
        v-for="item in cart.items"
        :key="item.id"
        class="cart-item"
      >

        <span class="food">

          {{ item.emoji }}
          {{ item.name }}

        </span>


        <div class="quantity">

          <button
            @click="
              cart.decreaseQuantity(item.id)
            "
          >

            −

          </button>


          <span>
            {{ item.quantity }}
          </span>


          <button
            @click="
              cart.increaseQuantity(item.id)
            "
          >

            +

          </button>

        </div>


        <span>

          ₹{{ item.price * item.quantity }}

        </span>


        <button
          @click="
            cart.removeFromCart(item.id)
          "
        >

          Remove

        </button>

      </div>


      <hr>


      <div class="total">

        <h3>
          Total: ₹{{ cart.totalBill }}
        </h3>


        <button
          @click="cart.clearCart()"
        >

          Clear Cart

        </button>

      </div>

    </div>

  </div>

</template>


<script>

import { useCartStore } from '../components/stores/cart'

export default {

  name: 'Cart',


  data() {

    return {

      cart: useCartStore()

    }

  }

}

</script>


<style scoped>

.cart-item {

  display: flex;

  align-items: center;

  gap: 25px;

  padding: 15px 0;

  border-bottom: 1px solid #ddd;

}

.food {

  width: 150px;

  font-size: 18px;

}

.quantity {

  display: flex;

  align-items: center;

  gap: 10px;

}

.quantity button {

  width: 30px;

  height: 30px;

}

.total {

  text-align: right;

}

.total button {

  padding: 10px 20px;

}

</style>