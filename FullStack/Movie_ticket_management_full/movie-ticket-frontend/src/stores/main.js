import { defineStore } from 'pinia'
import { api } from '../api'
import config from '../config'

export const useStore = defineStore('main', {
  state: () => ({
    main: [],
    second: [],
    reportTitle: '',
    reportRows: [],
  }),
  actions: {
    async load(which) {
      this[which] = await api(config[which].path)
    },
    async save(which, item, id) {
      const path = config[which].path
      if (id) await api(`${path}/${id}`, 'PUT', item)
      else await api(path, 'POST', item)
      await this.load(which)
      // a booking changes the available seats, so refresh movies too
      if (which === 'second') await this.load('main')
    },
    async remove(which, id) {
      await api(`${config[which].path}/${id}`, 'DELETE')
      await this.load(which)
    },
    async runReport(report) {
      this.reportTitle = report.label
      const data = await api(report.path)
      this.reportRows = Array.isArray(data) ? data : []
    },
  },
})
