<template>
  <div class="task">
    <div class="left-side">
      <img src="../assets/Story_Task/task_icon.png">
    </div>
    <div class="main">
      <div id="leftmain">
        <div id="name">
          <TaskBigView v-bind:task-object="task" v-bind:devOne="devOne" v-bind:devTwo="devTwo"></TaskBigView></div>
        <!--<div id="description">{{task.taskDescription}}</div>-->
        <div id="story"><p></p></div>
        <div>Fortschritt: {{task.taskCompletion}}%</div>
        <div>Fehlerwahrscheinlichkeit: {{task.taskFailureProbability}}%</div>
      </div>
      <div id="rightmain" v-if="!reviewPhase">
        <v-btn v-show="!inReview && ready" @click="bearbeiten">bearbeiten</v-btn>
        <v-btn v-show="!inReview && !ready" v-on:click="beenden">Arbeit unterbrechen</v-btn>
        <v-btn v-show="!inReview && !ready && !test" v-on:click="startTesting">testen</v-btn>
        <v-btn v-show="!inReview && !ready && test" v-on:click="stopTesting">Testen unterbrechen</v-btn>
      </div>
    </div>
    <div class="right-side">
      <img src="../assets/Story_Task/dummy-user.jpg">
    </div>
  </div>
</template>

<script>
import TaskBigView from './TaskBigView'
import {global} from '../main.js'
export default {
  name: 'Task',
  components: {TaskBigView},
  props: {
    task: {
      type: Object,
      required: false
    },
    test: {
      type: Boolean,
      required: false,
      default: false
    },
    devOne: {
      type: Object,
      required: false
    },
    devTwo: {
      type: Object,
      required: false
    },
    reviewPhase: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data () {
    return {
      dialog: false,
      ready: true,
      inReview: false
    }
  },
  methods: {
    bearbeiten: function () {
      global.rpc.call('setTaskInProgress', this.task)
    },
    startTesting: function () {
      global.rpc.call('setTaskTest', this.task)
    },
    stopTesting: function () {
      global.rpc.call('setTaskNotTest', this.task)
    },
    beenden: function () {
      global.rpc.call('setTaskReady', this.task)
    }
  },
  created () {
    /* Zuweisen von Variablen zum Anzeigen der richtigen Buttons */
    if (this.task.taskStatus === 'taskReady') {
      this.ready = true
    } else if (this.task.taskStatus === 'taskInProgress') {
      this.ready = false
    }
    if (this.task.taskStatus === 'taskInReview') {
      this.inReview = true
    }
    if (this.task.taskTest) {
      this.test = true
    } else {
      this.test = false
    }
  }
}
</script>

<style scoped>
  .task{
    display:flex;
    flex-grow: 1;
    flex-direction: row;
    justify-content: space-around;
    flex-wrap: wrap;
    min-height: 40px;
    border-radius: 5px;
    border-color: #4CAF50;
    border-style: solid;
    border-width: 2px;
    margin-top: -2px;
    padding: 3px 3px 3px 3px;
    background-color: cornsilk;
  }
  .main{
    display:flex;
    order:2;
    flex:10;
    align-content: flex-start;
    text-align: left;
  }
  #leftmain, #rightmain{
    flex: 1;
  }
  #name{
    font-style: normal;
    font-weight: bold;
  }
  #name:hover{
    color: red;
  }
  #description{
    font-style: italic;
    font-weight: lighter;
  }
  p{
  }
  .right-side{
    order:3;
    flex:1;
    align-content: flex-end;
  }
  .left-side{
    order: 1;
    flex:1;
    align-content: flex-start;
  }
  .left-side img{
    width: 15px;
  }
  .right-side img{
    width: 30px;
  }
</style>
