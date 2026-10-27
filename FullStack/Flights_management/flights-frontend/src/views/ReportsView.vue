<script setup>
import { useStore } from '../stores/main'
import config from '../config'
import DataTable from '../components/DataTable.vue'

const store = useStore()
</script>

<template>
  <h3>Reports</h3>
  <button v-for="r in config.reports" :key="r.path" @click="store.runReport(r)">{{ r.label }}</button>
  <h4 v-if="store.reportTitle">{{ store.reportTitle }}</h4>
  <DataTable v-if="store.reportTitle" :rows="store.reportRows" />
</template>
