<template>
  <div id="container">
    <div class="wholeBox">
      <div class="story-selection">

        <div class="way-options">
          <div v-for="(decision, index) in decisions" class="way" v-on:click="activeWay(index)" v-bind:class="{active: chosenDecision === index,
          notActive: chosenDecision !== index, blocked : (poolOneIsBlocked || poolTwoIsBlocked) && chosenDecision !== index, disabled : poolOneIsBlocked || poolTwoIsBlocked}" v-bind:key="index">
            <h2>{{decision.decisionName}}</h2>
          </div>
        </div>

        <div class="pool-names">
          <h2 class="pool-name">Pool 1</h2>
          <h2>Pool 2</h2>
        </div>

        <div class="pools">
          <div class="pool">
            <div class="box">
              <div class="list" v-for="(item) in poolOne" :key="item.id" @click="popupActivo=true, setSelection(item, 'poolOne')"
                   :class="{selected : selectionPoolOne === item.storyId, blocked : poolOneIsBlocked && selectionPoolOne !== item.storyId, disabled : poolOneIsBlocked}">
                <span class="storyText">{{item.storyName}}</span>
              </div>
            </div>
          </div>

          <div class="pool">
            <div class="box">
              <div class="list" v-for="(item) in poolTwo" :key="item.id" @click="popupActivo=true, setSelection(item, 'poolTwo')"
                   :class="{selected : selectionPoolTwo === item.storyId, blocked : poolTwoIsBlocked && selectionPoolTwo !== item.storyId, disabled : poolTwoIsBlocked}">
                <span class="storyText">{{item.storyName}}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="sprint">
        <h2>Aktueller Sprint</h2>
        <div class="box">
          <div class="list" v-for="(item, index) in sprintStories" :key="item.id" @click="popupActivo=true, setSelection(item, 'sprintStories'), indexSprint = index">
            <span class="storyText">{{item.storyName}}</span>
          </div>
        </div>
        <button class="button" @click="popupActivo2=true">Zeit angeben</button><br>
        <div>Angegebene Zeit: {{ sprintTime }}h</div><br>
        <div>
          <h2>Information:</h2>
          <p>Eine Stunde dauert 30 Echtzeitsekunden. Planen Sie auch Zeit für Tests ein.</p>
        </div>
      </div>
    </div>
    <vs-popup class="popupWindow"  :title="selectedStory.storyName" :active.sync="popupActivo">
      <div>{{ selectedStory.storyDescription }}</div>
      <br>
      <div>
        <h3>Abschlüsse:</h3>
        <div v-for="reward in selectedStory.storyRewards" v-bind:key="reward">
          {{ abilities[reward-1] }}
        </div>
      </div>
      <br>
      <div class="popupContent">
        <span class="aufgaben">Aufgaben</span>
        <div class="tasks">
          <div class="task" v-for="(task, index) in selectedStory.storyTasks" v-bind:key="index">
            <span class="taskText">{{task.taskName}} - {{task.taskTime}}h</span>
          </div>
        </div>
      </div>
      <button class="pushStory" @click="reactButton(), popupActivo=false">
        {{buttonText}}
      </button>
    </vs-popup>
    <vs-popup class="popupWindow" title="Sprint Zeitangabe" :active.sync="popupActivo2">
      <div class="popupContent2">
        <div class="timeInput">
          <span class="time">Zeit für den Sprint in Stunden angeben</span>
          <vs-input v-model="sprintTimeInput" class="input" placeholder="Stundenanzahl eingeben"/>
        </div>
        <button class="pushTime" @click="setSprintTime(), popupActivo2=false">
          {{buttonText2}}
        </button>
      </div>
    </vs-popup>
  </div>
</template>

<script>
import {global} from '../main.js'

export default {
// Array mit Liste der Storys
  name: 'Backlog',
  // stories werden in Array "stories" geschrieben
  created: function () {
    global.rpc.register('updateSprintBacklog', this.updateSprintBacklog)
    global.rpc.call('requestUpdateSprintBacklog', null)
    this.$emit('loadText', this.infoText)
  },
  data () {
    return {
      infoText: 'In dieser Phase sollt ihr euch entscheiden, wie genau ich das Epic durchkämpfen muss. Entscheidet euch für einen Weg. Aus jedem Pool sollt ihr jeweils eine Story wählen, die in diesem Sprint bearbeitet wird. Viel Erfolg!',
      popupActivo: false,
      popupActivo2: false,
      structure: [],
      decisions: [],
      chosenDecision: 0,
      poolOne: [],
      poolTwo: [],
      sprintStories: [],
      selectedStory: [],
      selectedArray: '',
      selectionPoolOne: -666,
      selectionPoolTwo: -666,
      buttonText: 'Zu Sprint hinzufügen',
      buttonText2: 'Bestätigen',
      poolOneIsBlocked: false,
      poolTwoIsBlocked: false,
      indexSprint: -666,
      sprintTimeInput: 0,
      sprintTime: 0,
      abilities: [
        'Interaktion mit Menschen',
        'Kommunikation mit Menschen',
        'Lesen',
        'Topographie',
        'Verkehrstraining für Fussgänger',
        'Grundsätzliche Bedienung von Kraftfahrzeugen',
        'Truck-Surfing',
        'Universalführerschein',
        'GK Fassaden Raufklettern',
        'Fahrstuhlbenutzung',
        'GK Fliegen',
        'Herabklettern',
        'Weitsprung',
        'Hangeln',
        'Streckenflug',
        'Hochhausklettern',
        'Tarnung',
        'Hilfe von Fensterputzern',
        'Höhenflug',
        'Krafttraining für Höhenflugsaurier',
        'Flugtechniken Shao Lang',
        'Kampftechniken Shao Lang',
        'Flugtechniken „Mitteldeutschland“',
        'Kampftechniken „Mitteldeutschland“',
        'Kampftechniken Peitschenschwanz',
        'Flugtechniken Krallen',
        'Fitness- & Schnelligkeitstraining „Nying Do“',
        'Kampftechnik „Nying Do“',
        'Ertüchtigungstraining „Sachsen“',
        'Allgemeines Kampftraining',
        'Bodenkampftechniken Krallen',
        'Bodenkampftechniken Peitschenschwanz',
        'Gorillasprache',
        'Groß und böse aussehen',
        'Gesteigertes Selbstbewusstsein',
        'Gorillas überzeugen',
        'Primaten Einschüchtern',
        'Schnelligkeits- & Reflextraining'
      ]
    }
  },
  methods: {
    // angegebene Sprintzeit wird in server eingetragen
    setSprintTime: function () {
      global.rpc.call('setSprintTime', { sprintTime: this.sprintTimeInput })
    },
    // Ausgewählter Weg wird gespeichert und dazugehörige Story-Pools
    activeWay: function (index) {
      this.chosenDecision = index
      this.poolOne = this.structure.decisions[index].storyPoolOne
      this.poolTwo = this.structure.decisions[index].storyPoolTwo
    },
    updateSprintBacklog: function (data) {
      this.sprintTime = data.sprintTime / 30
      this.updateVariables(data.epic)
      this.updateSprintStories(data.epic)
    },
    updateVariables: function (data) {
      this.structure = data
      this.decisions = data.decisions
      this.chosenDecision = data.chosenDecision
      this.poolOne = data.decisions[this.chosenDecision].storyPoolOne
      this.poolTwo = data.decisions[this.chosenDecision].storyPoolTwo
      this.updateChosenStories()
    },
    updateChosenStories: function () {
      this.chosenStoryOne = this.decisions[this.chosenDecision].chosenStoryOne
      this.chosenStoryTwo = this.decisions[this.chosenDecision].chosenStoryTwo
      if (this.chosenStoryOne) {
        this.poolOneIsBlocked = true
        this.selectionPoolOne = this.chosenStoryOne
      } else {
        this.poolOneIsBlocked = false
        this.selectionPoolOne = -666
      }
      if (this.chosenStoryTwo) {
        this.poolTwoIsBlocked = true
        this.selectionPoolTwo = this.chosenStoryTwo
      } else {
        this.poolTwoIsBlocked = false
        this.selectionPoolTwo = -666
      }
    },
    updateSprintStories: function () {
      this.sprintStories = []
      if (this.chosenStoryOne) {
        for (let i = 0; i < this.poolOne.length; i++) {
          if (this.poolOne[i].storyId === this.chosenStoryOne) {
            this.sprintStories.push(this.poolOne[i])
          }
        }
      }
      if (this.chosenStoryTwo) {
        for (let i = 0; i < this.poolTwo.length; i++) {
          if (this.poolTwo[i].storyId === this.chosenStoryTwo) {
            this.sprintStories.push(this.poolTwo[i])
          }
        }
      }
    },
    setSelection: function (item, selectedArray) {
      this.selectedStory = item
      this.selectedArray = selectedArray
      this.setHighlight(item)
      this.setButtonText(selectedArray)
    },
    setHighlight: function (data) {
      if (this.selectedArray === 'poolOne') {
        this.selectionPoolOne = data.storyId
      } else if (this.selectedArray === 'poolTwo') {
        this.selectionPoolTwo = data.storyId
      }
    },
    setButtonText: function (selectedArray) {
      if (selectedArray === 'sprintStories') {
        this.buttonText = 'Aus Sprint löschen'
      } else {
        this.buttonText = 'Zu Sprint hinzufügen'
      }
    },
    reactButton: function () {
      // sende Server Daten über gewählten Weg
      global.rpc.call('setDecision', { decisionId: this.chosenDecision })
      if (this.selectedArray === 'poolOne') {
        global.rpc.call('setStoryOne', this.selectedStory)
      } else if (this.selectedArray === 'poolTwo') {
        global.rpc.call('setStoryTwo', this.selectedStory)
      } else {
        this.deleteFromSprint()
      }
    },
    deleteFromSprint: function () {
      for (let i = 0; i < this.poolOne.length; i++) {
        if (this.poolOne[i].storyId === this.selectedStory.storyId) {
          global.rpc.call('setStoryOne', null)
        }
      }
      for (let i = 0; i < this.poolTwo.length; i++) {
        if (this.poolTwo[i].storyId === this.selectedStory.storyId) {
          global.rpc.call('setStoryTwo', null)
        }
      }
    }
  }
}
</script>

<style scoped>
  #container {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
  }
  h1 {
    text-align: center;
    font-weight: bold;
  }
  h2 {
    text-align: left;
  }
  .wholeBox {
    margin-top: 23px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    width:90%;
    margin-left: auto;
    margin-right: auto;
  }
  .story-selection {
    width: 55%;
  }
  .way-options {
    margin-top: 20px;
    display: flex;
    justify-content: space-evenly;
    flex-wrap: wrap;
  }
  .way {
    border-style: solid;
    border-radius: 5px;
    padding: 15px;
    margin-bottom: 15px;
  }
  .way:hover {
    cursor: pointer;
  }
  .active {
    background-color: limegreen;
  }
  .notActive {
    background-color: white;
  }
  .pool-names {
    margin-top: 5px;
    display: flex;
    flex-direction: row;
    width: 100%;
  }
  .pool-name {
    width: 55%;
  }
  .pools {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    width: 100%;
  }
  .pool {
    display: flex;
    flex-direction: row;
    width: 45%;
  }
  .sprint {
    margin-top: 20px;
    display: flex;
    flex-direction: column;
    width: 35%;
  }
  .box {
    margin-top: 10px;
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    width: 100%;
    height: 50vh;
    border-color: #efeded;
    border-style: solid;
    border-width: 5px;
    border-radius: 10px;
    overflow-y: scroll;
  }
  .blocked {
    color: darkgrey;
  }
  .disabled {
    pointer-events: none;
  }
  .list {
    background-color: #e0e0e0;
    border-radius: 5px;
    border-style: solid;
    border-width: 1px;
    text-align: left;
    padding: 10px;
  }
  .list:hover {
    cursor: pointer;
    background-color: #b5b5b5;
  }
  .selected {
    background-color: limegreen;
  }
  .storyText {
    font-family: Arial, Helvetica, sans-serif;
    font-size: 16px;
  }
  .popupWindow {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    font-size: 20px;
  }
  .popupContent {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
  .aufgaben {
    width: 30%;
    text-align: center;
    font-size: 25px;
  }
  .tasks {
    width: 70%;
    border-left-style: solid;
    padding-left: 10px;
    padding-top: 10px;
  }
  .task {
    margin-bottom: 10px;
    padding: 5px;
    background-color: #e0e0e0;
    border-radius: 5px;
  }
  .pushStory {
    float: right;
    border-style: solid;
    border-color: black;
    border-width: 1px;
    padding: 8px;
    border-radius: 5px;
  }
  button {
    background-color: #d8d8d8;
    width: 40%;
    padding: 10px;
    margin-right: auto;
    margin-left: auto;
    margin-top: 10%;
    border-radius: 10px;
    font-size: 18px;
    transition: 0.5s;
    outline: none;
    border-style: solid;
    border-color: black;
    border-width: 1px;
  }
  button:hover {
    background-color: limegreen;
    transform: scale(1.05);
    cursor: pointer;
    transition: 0.5s;
  }
  .pushStory:hover {
    background-color: limegreen;
    transition: 0.3s;
  }
  .input {
    float: left;
    width: 40%;
    margin: 10px;
  }
  .pushTime {
    width: 18%;
    float: right;
    border-style: solid;
    border-color: black;
    border-width: 1px;
    padding: 8px;
    border-radius: 5px;
    margin-top: 5%;
  }
  .pushTime:hover {
    background-color: limegreen;
    transition: 0.3s;
  }
  .time {
    width: 60%;
    text-align: center;
    font-size: 20px;
  }
  .popupContent2 {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
  }
  .timeInput {
    width: 70%;
    display: flex;
    flex-direction: row;
    align-items: center;
  }
  ::-webkit-scrollbar
  {
    width: 12px;
    background-color: #F5F5F5;
  }

  ::-webkit-scrollbar-track
  {
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
    border-radius: 10px;
    background-color: #F5F5F5;
  }

  ::-webkit-scrollbar-thumb
  {
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
    background-color: #555;
  }

  ::-webkit-scrollbar-thumb:hover {
    background-color: #999e9c;
  }
</style>
