<script setup>
import config from './config'
</script>

<template>
  <h2>{{ config.title }}</h2>
  <nav>
    <router-link to="/">{{ config.main.label }}</router-link>
    <router-link to="/second">{{ config.second.label }}</router-link>
    <router-link to="/reports">Reports</router-link>
  </nav>
  <hr />
  <router-view />
</template>
