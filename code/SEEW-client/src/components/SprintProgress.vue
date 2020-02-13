<template>
  <div class="root" id="background">
    <!--
    <v-toolbar card color="green" dark>
      <v-toolbar-title>SPRINTVERLAUF</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-items>
        <v-spacer></v-spacer>
        <v-btn flat v-on:click="back" class="button">Sprintübersicht</v-btn>
        <v-btn flat v-on:click="end" class="button" >Sprint abschließen</v-btn>
      </v-toolbar-items>
    </v-toolbar>-->
    <v-toolbar card color="green" dark>
      <v-toolbar-title>Sprint {{sprint}} - Verbleibende Zeit: {{ hours }}h {{minutes}}m</v-toolbar-title>
      <button id="dailyBtn" v-if="dailyStatus === 0" v-on:click="startDaily">Daily durchführen (Zeitbedarf: {{ dailyTime }}m)</button>
      <div class="dailyDiv" v-else-if="dailyStatus === 1">Daily wird durchgeführt - Zeit: {{ dailyTime }}m</div>
      <div class="dailyDiv" v-else>Daily wurde durchgeführt</div>
    </v-toolbar>
    <div class="aufgaben-container">
      <div class="flexbox aufgaben">
        <v-toolbar card color="green" dark>
          <v-toolbar-title>AKTUELLE AUFGABEN</v-toolbar-title>
        </v-toolbar>
        <Task v-bind:key="task.taskId" v-for="task in ready" v-bind:task="task"
              v-bind:devOne="devOne" v-bind:devTwo="devTwo" v-bind:test="task.taskTest"></Task>
      </div>
      <div class="flexbox aufgaben">
        <v-toolbar card color="green" dark>
          <v-toolbar-title>IN ARBEIT</v-toolbar-title>
        </v-toolbar>
        <Task :key="task.taskId" :id="task.taskId" v-for="task in inProgress" v-bind:task="task"
              v-bind:devOne="devOne" v-bind:devTwo="devTwo" v-bind:test="task.taskTest"></Task>
      </div>
      <div class="flexbox aufgaben">
        <v-toolbar card color="green" dark>
          <v-toolbar-title>IN REVIEW</v-toolbar-title>
        </v-toolbar>
        <Task :key="task.taskId" :id="task.taskId" v-for="task in inReview" v-bind:task="task"
              v-bind:devOne="devOne" v-bind:devTwo="devTwo" v-bind:test="task.taskTest"></Task>
      </div>
    </div>
    </div>
</template>

<script>
import {global} from '../main.js'
import Task from './Task'

export default {
  name: 'SprintProgress',
  components: {
    Task
  },
  created: function () {
    global.rpc.register('updateSprintProgress', this.updateSprintProgress)
    global.rpc.call('requestUpdateSprintProgress', null)
    this.$emit('loadText', this.infoText)
  },
  data () {
    return {
      infoText: 'Im Sprintverlauf sollt ihr jede Aufgabe einem/mehreren Entwicklern zuweisen und bearbeiten. Um Fehlerwahrscheinlichkeit zu verringern, könnt ihr die Aufgaben testen und/oder ein Daily-Meeting durchführen. Beeilt euch!',
      ready: [],
      inProgress: [],
      tested: [],
      inReview: [],
      devOne: '',
      devTwo: '',
      hours: 0,
      minutes: 0,
      sprint: 0,
      dailyStatus: 0,
      dailyTime: 0
    }
  },
  methods: {
    back: function () {
      this.$emit('changeView', 'Between')
    },
    end: function () {},
    updateSprintProgress: function (data) {
      this.sprint = data.currentSprint + 1
      this.devOne = data.developerOne
      this.devTwo = data.developerTwo
      this.hours = Math.floor(data.currentSprintTime / 30)
      this.minutes = (data.currentSprintTime % 30) * 2
      this.dailyStatus = data.sprintDailyStatus
      this.dailyTime = data.sprintDailyTime * 2
      this.ready = data.currentTasks.filter(function (t) {
        return t.taskStatus === 'taskReady'
      })
      this.inProgress = data.currentTasks.filter(function (t) {
        return t.taskStatus === 'taskInProgress'
      })
      this.inReview = data.currentTasks.filter(function (t) {
        return t.taskStatus === 'taskInReview'
      })
    },
    startDaily: function () {
      global.rpc.call('startSprintDaily', null)
    }
  }

}
</script>

<style scoped>
  @import '../assets/globalstyle.css';
  @import '../assets/Progress_Review.css';
  h1{
    font-size: 50pt;
  }
  h2{
    font-size: 25pt;
  }
  #sprintTime{
    margin-top:10px;
    margin-left:10px;
    padding: 5px 5px 5px 5px;
    border-radius: 5px;
    float: left;
    background-color: #4CAF50;
    color: white;
    font-size: medium;
  }
  .root{
    margin: 0 auto;
    height: 100%;
    width: 100%;
  }
  #dailyBtn {
    color: white;
  }
  .dailyDiv {
    padding-left: 40px;
  }
</style>
