<script setup>
import { computed } from 'vue'

const props = defineProps({ rows: { type: Array, default: () => [] } })

// columns = every key found in the rows
const columns = computed(() => [...new Set(props.rows.flatMap((r) => Object.keys(r)))])

const show = (v) => (v !== null && typeof v === 'object' ? JSON.stringify(v) : v)
</script>

<template>
  <p v-if="!rows.length">No data.</p>
  <table v-else>
    <thead>
      <tr>
        <th v-for="c in columns" :key="c">{{ c }}</th>
        <th v-if="$slots.actions"></th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(r, i) in rows" :key="i">
        <td v-for="c in columns" :key="c">{{ show(r[c]) }}</td>
        <td v-if="$slots.actions"><slot name="actions" :row="r" /></td>
      </tr>
    </tbody>
  </table>
</template>
