<template>
  <div id="time">
    <h1>Verbleibende Zeit: {{ minutes }}:{{ seconds }}</h1>
  </div>
</template>

<script>
import {global} from '../main.js'
export default {
  name: 'Time',
  /**
   * registriert Funktion updateTime, sodass sie vom Server aufgerufen werden kann
   */
  created () {
    global.rpc.register('updateTime', this.updateTime)
  },
  data () {
    return {
      minutes: '0',
      seconds: '00'
    }
  },
  methods: {
    /**
     * ändert vom Server übergebene Werte in Minuten und Sekunden
     * @param data Übergabeparameter vom Server
     */
    updateTime: function (data) {
      this.seconds = data.currentTime % 60 < 10 ? '0' + (data.currentTime % 60) : data.currentTime % 60
      this.minutes = (data.currentTime - this.seconds) / 60
    }
  }
}
</script>

<style scoped>
  h1 {
    font-size: 20px;
    font-weight: lighter;
    /*margin: -11px -11px -11px -11px;*/
  }
  #time {
    color: white;
  }
</style>
