<script setup>
import { reactive, watch } from 'vue'

const props = defineProps({
  fields: Array,
  initial: Object, // item being edited (or null for a new one)
  refs: { type: Array, default: () => [] }, // options for 'ref' fields
})
const emit = defineEmits(['save', 'cancel'])

const form = reactive({})

watch(
  () => props.initial,
  (item) => props.fields.forEach((f) => (form[f.name] = item?.[f.name] ?? '')),
  { immediate: true }
)

function submit() {
  const out = {}
  props.fields.forEach((f) => (out[f.name] = f.type === 'number' ? Number(form[f.name]) : form[f.name]))
  emit('save', out)
}
</script>

<template>
  <form @submit.prevent="submit">
    <label v-for="f in fields" :key="f.name">
      {{ f.name }}
      <select v-if="f.type === 'ref'" v-model="form[f.name]" required>
        <option value="" disabled>choose</option>
        <option v-for="o in refs" :key="o" :value="o">{{ o }}</option>
      </select>
      <select v-else-if="f.type === 'select'" v-model="form[f.name]" required>
        <option value="" disabled>choose</option>
        <option v-for="o in f.options" :key="o" :value="o">{{ o }}</option>
      </select>
      <input v-else :type="f.type" v-model="form[f.name]" required />
    </label>
    <button type="submit">{{ initial ? 'Update' : 'Add' }}</button>
    <button v-if="initial" type="button" @click="emit('cancel')">Cancel</button>
  </form>
</template>
