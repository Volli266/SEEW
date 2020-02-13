// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import {RPC} from './rpc.js'
import { store } from './store'
import './components/SpeechBubble'
import Vuesax from 'vuesax'
import 'vuesax/dist/vuesax.css'

Vue.use(Vuesax)

Vue.config.productionTip = false

export const global = new Vue({
  data: {
    rpc: new RPC()
  }
})
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>',
  created: function () {
    global.rpc.register('returnToLogin', function () {
      router.push('Login')
    })
    global.rpc.open()
  }
})
