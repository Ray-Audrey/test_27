import { defineStore } from 'pinia'

export const useCartStore = defineStore('cart', {

  state: () => ({

    items: []

  }),


  getters: {

    totalItems: (state) => {

      return state.items.reduce(
        (total, item) => total + item.quantity,
        0
      )

    },


    totalBill: (state) => {

      return state.items.reduce(
        (total, item) =>
          total + item.price * item.quantity,
        0
      )

    }

  },


  actions: {

    addToCart(food) {

      const existingItem =
        this.items.find(
          item => item.id === food.id
        )


      if (existingItem) {

        existingItem.quantity++

      }

      else {

        this.items.push({

          ...food,

          quantity: 1

        })

      }

    },


    increaseQuantity(id) {

      const item =
        this.items.find(
          item => item.id === id
        )

      if (item) {

        item.quantity++

      }

    },


    decreaseQuantity(id) {

      const item =
        this.items.find(
          item => item.id === id
        )


      if (!item) {

        return

      }


      if (item.quantity > 1) {

        item.quantity--

      }

      else {

        this.removeFromCart(id)

      }

    },


    removeFromCart(id) {

      this.items =
        this.items.filter(
          item => item.id !== id
        )

    },


    clearCart() {

      this.items = []

    }

  }

})