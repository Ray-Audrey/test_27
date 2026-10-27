<template>

  <div>

    <h2>Order</h2>

    <nav class="order-nav">

      <RouterLink to="/order/menu">
        Food Menu
      </RouterLink>

      <RouterLink to="/order/cart">
        Cart
      </RouterLink>

    </nav>


    <!-- Nested pages appear here -->

    <RouterView />

  </div>

</template>


<script>

export default {

  name: 'Order'

}

</script>


<style scoped>

.order-nav {

  margin-bottom: 25px;

}

.order-nav a {

  margin-right: 20px;

  text-decoration: none;

}

</style>