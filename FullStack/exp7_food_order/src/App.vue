<template>

  <div>

    <header>

      <h1>Online Food Ordering</h1>

      <nav>

        <RouterLink to="/">
          Home
        </RouterLink>

        <RouterLink to="/order">
          Order
        </RouterLink>

        <RouterLink to="/order/menu">
          Food Menu
        </RouterLink>

        <RouterLink to="/order/cart">
          Cart
        </RouterLink>

        <RouterLink to="/about">
          About
        </RouterLink>

      </nav>

    </header>


    <main>

      <RouterView />

    </main>

  </div>

</template>


<script>

export default {

  name: 'App'

}

</script>


<style>

body {

  margin: 0;

  font-family: Arial, sans-serif;

}

header {

  background: #222;

  color: white;

  padding: 20px;

  text-align: center;

}

nav {

  margin-top: 15px;

}

nav a {

  color: white;

  text-decoration: none;

  margin: 0 15px;

}

nav a.router-link-active {

  font-weight: bold;

  text-decoration: underline;

}

main {

  padding: 30px;

  max-width: 900px;

  margin: auto;

}

button {

  cursor: pointer;

}

</style>