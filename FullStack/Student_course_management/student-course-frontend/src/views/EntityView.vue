<script setup>
import { ref, computed, watch } from 'vue'
import { useStore } from '../stores/main'
import config from '../config'
import DataTable from '../components/DataTable.vue'
import ItemForm from '../components/ItemForm.vue'

const props = defineProps({ which: String }) // 'main' or 'second'

const store = useStore()
const editing = ref(null)

const cfg = computed(() => config[props.which])
const rows = computed(() => store[props.which])
// ids of the main collection, used by the dropdown in the second form
const refs = computed(() => store.main.map((m) => m[config.main.id]))
const canEdit = computed(() => props.which === 'main')

watch(
  () => props.which,
  () => {
    editing.value = null
    store.load(props.which)
    if (props.which === 'second') store.load('main')
  },
  { immediate: true }
)

async function save(item) {
  await store.save(props.which, item, editing.value?.[cfg.value.id])
  editing.value = null
}
</script>

<template>
  <h3>{{ cfg.label }}</h3>
  <ItemForm :fields="cfg.fields" :id-name="cfg.id" :initial="editing" :refs="refs" :key="editing?.[cfg.id]" @save="save" @cancel="editing = null" />
  <DataTable :rows="rows">
    <template v-if="canEdit" #actions="{ row }">
      <button @click="editing = row">Edit</button>
      <button @click="store.remove(which, row[cfg.id])">Delete</button>
    </template>
  </DataTable>
</template>
