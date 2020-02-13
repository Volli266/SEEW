<template>
  <div class="root" id="background">
  <div class="aufgaben-container">
    <div class="flexbox">
      <button v-on:click="startSprintReview" class="button" >Review durchführen</button>
      <transition name="bounceOfficer">
        <img v-show="showPoliceOfficer" class="officer" src="../assets/police2.png" alt="Polizist"/>
      </transition>
      <SpeechBubble :show="showPermissionDialog" bubble-pointer="bottomLeft" v-bind:bubble-left="350"
                    v-bind:bubble-width="280" v-bind:bubble-top="200" v-bind:text="text"></SpeechBubble>
      <template v-if="buttonVisible===true">
        <button id="ok" v-on:click="next"><p>OK!</p></button>
      </template>
    </div>
    <div class="flexbox aufgaben">
      <v-toolbar card color="green" dark>
        <v-toolbar-title>NICHT ERLEDIGTE AUFGABEN</v-toolbar-title>
      </v-toolbar>
      <Task :key="task.taskId" :id="task.taskId" v-for="task in unfinished" v-bind:task="task"
        v-bind:review-phase="true"></Task>
    </div>
    <div class="flexbox aufgaben">
      <v-toolbar card color="green" dark>
        <v-toolbar-title>IN REVIEW</v-toolbar-title>
      </v-toolbar>
      <Task :key="task.taskId" :id="task.taskId" v-for="task in inReview" v-bind:task="task"
            v-bind:review-phase="true"></Task>
    </div>
    <div class="flexbox aufgaben">
      <v-toolbar card color="green" dark>
        <v-toolbar-title>ERFOLGREICH BEENDET</v-toolbar-title>
      </v-toolbar>
      <Task :key="task.taskId" :id="task.taskId" v-for="task in finished" v-bind:task="task"
            v-bind:review-phase="true"></Task>
    </div>
  </div>
  </div>
</template>

<script>
import {global} from '../main.js'
import Task from './Task'
import SpeechBubble from './SpeechBubble'
export default {
  components: { SpeechBubble, Task },
  name: 'Review',
  created: function () {
    global.rpc.register('updateSprintReview', this.updateSprintReview)
    global.rpc.call('requestUpdateSprintReview', null)
    this.$emit('loadText', this.infoText)
    setTimeout(() => {
      this.showPoliceOfficer = true
      setTimeout(() => {
        this.showPermissionDialog = true
        this.buttonVisible = true
      }, this.delayPoliceDialog)
    }, this.delayButton)
  },
  data () {
    return {
      infoText: 'Hier werden zusammen mit dem Kunden die Sprintergebnisse angeschaut. Wenn die Aufgabe erfolgreich war - wird sie akzeptiert. Entwickler und ich bekommen Punkte für entsprechende Skills. Nicht erledigte Aufgaben werden mit in den nächsten Sprint übernommen ',
      unfinished: [],
      inReview: [],
      finished: [],
      loading: false,
      testTask: {
        taskName: 'NAME',
        taskDescription: 'DESCRIPTION',
        taskStatus: 'STATUS',
        Sprint: '1',
        taskRequirements: 'MODUL 1, MODUL 2',
        taskDevelopers: {
          developer1: 'John Snow',
          developer2: 'Sansa Stark'
        }
      },
      showPoliceOfficer: false,
      showPermissionDialog: false,
      buttonVisible: false,
      delayPoliceDialog: 350, /* delay in ms, Erscheinen vom Officer und der Sprechblase */
      delayButton: 750, /* delay in ms, Erscheinen von Elementen nach dem Laden der Seite */
      i: 0,
      text: 'Der aktuelle Sprint ist schon zu Ende. Klicke auf \'REVIEW DURCHFÜHREN\' um Ergebnisse des Sprints auszuwerten.'
    }
  },
  methods: {
    back: function () {
      this.$emit('changeView', 'Between')
    },
    next: function () {
      /* i - ist eine Hilfsvariable für mögliche Erweiterung des Dialogs. */
      if (this.i === 0) {
        this.showPermissionDialog = false
        this.buttonVisible = false
        this.i += 1
      }
    },
    updateSprintReview: function (data) {
      this.unfinished = data.currentTasks.filter(function (t) {
        return t.taskStatus === 'taskReady' || t.taskStatus === 'taskInProgress'
      })
      this.inReview = data.currentTasks.filter(function (t) {
        return t.taskStatus === 'taskInReview'
      })
      this.finished = data.currentTasks.filter(function (t) {
        return t.taskStatus === 'taskFinished'
      })
    },
    startSprintReview: function () {
      global.rpc.call('startSprintReview', null)
    }
  }
}
</script>
<style scoped>
  @import '../assets/globalstyle.css';
  @import '../assets/Progress_Review.css';
  img{
    margin-top: 200px;
    width: 200px;
    float: left;
  }
  button{
    color: white;
  }
  p{
    text-align: center;
  }
  #ok{
    float: left;
    margin-top: 10px;
    margin-left: 40px;
  }
</style>
