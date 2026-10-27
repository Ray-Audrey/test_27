<script setup>
import { reactive, ref, watch } from 'vue'

const props = defineProps({
  fields: Array,
  idName: String, // name of the id field (typed by you when adding)
  initial: Object, // item being edited (or null for a new one)
  refs: { type: Array, default: () => [] }, // options for 'ref' fields
})
const emit = defineEmits(['save', 'cancel'])

const form = reactive({})
const newId = ref('')

watch(
  () => props.initial,
  (item) => props.fields.forEach((f) => (form[f.name] = item?.[f.name] ?? '')),
  { immediate: true }
)

function submit() {
  const out = {}
  // ids are typed by hand (e.g. F101) so the $lookup queries can match them
  if (!props.initial) out[props.idName] = newId.value
  props.fields.forEach((f) => (out[f.name] = f.type === 'number' ? Number(form[f.name]) : form[f.name]))
  emit('save', out)
}
</script>

<template>
  <form @submit.prevent="submit">
    <label v-if="!initial">
      {{ idName }}
      <input v-model="newId" required />
    </label>
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
      <input v-else :type="f.type" :step="f.type === 'number' ? 'any' : undefined" v-model="form[f.name]" required />
    </label>
    <button type="submit">{{ initial ? 'Update' : 'Add' }}</button>
    <button v-if="initial" type="button" @click="emit('cancel')">Cancel</button>
  </form>
</template>
