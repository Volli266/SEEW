import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

// Store wird genutzt um Variablen und Werte in mehreren Komponenten aufrufen und nutzen zu können
export const store = new Vuex.Store({
  state: {
    nextPhase: '',
    currentPhase: '',
    myStopWatch: '',
    seconds: 0,
    minutes: 0
  },
  mutations: {
    add: state => {
      // Sekunden werden iteriert
      state.seconds++
      // Minuten iteriert, wenn Sekunden >= 60 sind
      if (state.seconds >= 60) {
        state.seconds = 0
        state.minutes++
      }
    }
  },
  /* Da Mutation aufgrund des Intervals zeitversetzt abläuft und für das Debugging werden
   * 'actions' genutzt
   */
  actions: {
    startWatch: context => {
      // Aufruf der mutation 'add' im Sekundentakt
      context.myStopWatch = setInterval(function () {
        context.commit('add')
      }, 1000)
    }
  }
})
