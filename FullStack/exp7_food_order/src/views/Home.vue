<template>

  <div class="home">

    <h2>Welcome to Online Food Ordering</h2>

    <p>
      Order your favourite food quickly and easily.
    </p>

    <RouterLink to="/order/menu">
      <button>
        View Food Menu
      </button>
    </RouterLink>

  </div>

</template>


<script>

export default {

  name: 'Home'

}

</script>


<style scoped>

.home {

  text-align: center;

}

button {

  padding: 10px 20px;

}

</style>