<template>
  <div>
    <div class="pbcontainer">
      <div class="pbitem">
        <h1>1</h1><br>
        {{ epics[epicOrder[0]] }}<br>
        <input v-on:keypress.enter="setEpic(epicOrder[0], $event.target.value - 1); clearText($event)" type="text"  placeholder="Priorität (1-3) eingeben">
      </div>
      <div class="pbitem">
        <h1>2</h1><br>
        {{ epics[epicOrder[1]] }}<br>
        <input v-on:keypress.enter="setEpic(epicOrder[1], $event.target.value - 1); clearText($event)" type="text"  placeholder="Priorität (1-3) eingeben">
      </div>
      <div class="pbitem">
        <h1>3</h1><br>
        {{ epics[epicOrder[2]] }}<br>
        <input v-on:keypress.enter="setEpic(epicOrder[2], $event.target.value - 1); clearText($event)" type="text"  placeholder="Priorität (1-3) eingeben">
      </div>
    </div>
  </div>
</template>

<script>
import {global} from '../main.js'
export default {
  created () {
    global.rpc.register('updateProductBacklog', this.updateProductBacklog)
    global.rpc.call('requestUpdateProductBacklog', null)
    this.$emit('loadText', this.infoText)
  },
  data () {
    return {
      epicOrder: [0, 1, 2],
      epics: ['Durch die Stadt bewegen', 'Hochhaus besteigen', 'Gorilla bekämpfen'],
      infoText: 'Hier seht ihr 3 Epics, die ich am Ende der Entwicklung schaffen muss. Denkt nach, in welcher Reihenfolge die Epics abgearbeitet werden. Das kann den gesamten Projektverlauf beeinflussen. Um die Reihenfolge zu ändern, schreib die Zahl in einen Epic und drück die Entertaste.'
    }
  },
  methods: {
    setEpic: function (from, epicPriority) {
      switch (from) {
        case 0: global.rpc.call('setEpicOne', {epicPriority})
          break
        case 1: global.rpc.call('setEpicTwo', {epicPriority})
          break
        case 2: global.rpc.call('setEpicThree', {epicPriority})
          break
      }
    },
    // Input wird auf leeren String gesetzt
    clearText: function (event) {
      event.target.value = ''
    },
    /**
     * Schickt Nachricht an Server, dass nächste Phase aufgerufen wird
     * sendet Event an Parent (Overview), und setzt backlogged auf true, sodass ProductBacklog nicht mehr aufgerufen werden kann
     */
    goOn: function () {
      global.rpc.call('confirmPhase', null)
    },
    updatePhase: function (data) {
      if (data.currentPhase === 1) {
        this.$emit('changeBacklog', null)
      }
    },
    updateProductBacklog: function (data) {
      this.epicOrder = data.epicOrder
    }
  }
}
</script>

<style scoped>
  @import "../stylesheet.css";

  input {
    font-family: Arial, Helvetica, sans-serif;
    height: 20%;
    font-size: 20px;
    margin-top: 12vh;
    border-radius: 3px;
    border-style: none;
    padding: 4px;
    background-color: white;
  }
  .pbcontainer {
    display: flex;
    justify-content: space-evenly;
    padding: 30px;
  }
  .pbitem {
    flex: 1;
    margin: 20px;
    padding: 20px;
    background-color: #3e8e41;
    font-size: 20px;
    border-radius: 10px;
  }

</style>
