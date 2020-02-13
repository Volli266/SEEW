<!-- Author: Mariia Fortova, Karl-Augustin Jahnel -->

<template>
  <body>
  <transition name="bounceOfficer">
    <img v-show="showPoliceOfficer" class="officer" src="../assets/police.png" />
  </transition>
  <div class = "dialog">
    <SpeechBubble :show="showPermissiondialog1" text="Guten Tag, mein Name ist Herr Grunwald. Ein Gorilla treibt auf einem Hochhaus sein Unwesen. Wir brauchen mehr Schlagkraft - einen Dinosaurier! In 30-Echtzeitminuten müssen Sie diesen als Team entwickelt haben um unsere Stadt zu retten!"
                  bubble-pointer="bottomLeft" v-bind:bubble-top="130"
                  v-bind:bubble-width="350" v-bind:bubble-left="280"  ></SpeechBubble>
    <SpeechBubble :show="showPermissiondialog2" text="Der Dinosaurier wird modular aufgebaut sein um unterschiedliche Herausforderungen zu meistern. Der Weg zum Hochhaus führt durch den Großstadtdschungel. Dort warten der gefährliche Straßenverkehr und Irrwege."
                  bubble-pointer="bottomLeft" v-bind:bubble-top="130"
                  v-bind:bubble-width="350" v-bind:bubble-left="280"></SpeechBubble>
    <SpeechBubble :show="showPermissiondialog3" text="Ein weiteres großes Hindernis ist das Hochhaus. Ihr Dino passt nicht in den Fahrstuhl - er muss klettern! Wenn der Dino ein Mal oben ist, warten dort ein gefährlicher Gorilla auf ihn, den er in einem harten Kampf besiegt. Machen Sie sich an die Arbeit!"
                  bubble-pointer="bottomLeft" v-bind:bubble-top="130"
                  v-bind:bubble-width="350" v-bind:bubble-left="280"></SpeechBubble>
  </div>
  <template v-if="go === true">
    <template v-if="startpressed === false">
      <button v-on:click="starten" class="button start">Simulation Starten</button>
    </template>
    <template v-if="startpressed === true">
      <button v-on:click="wechseln" class="button switch">OK!</button>
    </template>
  </template>
    <template v-if="los === true">
      <button v-on:click="readyForSimulation" class="button weiter">LOS GEHT'S</button>
    </template>
  <Notification v-bind:message="notification" class="notification"/>
  </body>
</template>

<script>
import {global} from '../main.js'
import SpeechBubble from './SpeechBubble'
import Notification from './Notification'

export default {
  name: 'Intro',
  components: {SpeechBubble, Notification},
  created () {
    global.rpc.register('showSimulation', this.showSimulation)
    global.rpc.register('notifyIntro', this.notifyIntro)
  },
  data () {
    return {
      i: 0,
      delayPoliceDialog: 350, /* delay in ms, Erscheinen vom Officer und der Sprechblase */
      delayButton: 750, /* delay in ms, Erscheinen von Elementen nach dem Drücken des Buttons */
      delayGoOn: 1100,
      go: true,
      startpressed: false,
      showPoliceOfficer: false,
      showPermissiondialog1: false,
      showPermissiondialog2: false,
      showPermissiondialog3: false,
      los: false,
      notification: ''
    }
  },
  methods: {
    /* Die Methode wird dann aufgerufen, wenn der Button angeklickt wird */
    starten () {
      setTimeout(() => {
        this.startpressed = true
        this.showPoliceOfficer = true
        setTimeout(() => {
          this.showPermissiondialog1 = true
        }, this.delayPoliceDialog)
      }, this.delayButton)
    },
    /* Wird beim Klicken von "OK" aufgerufen */
    wechseln () {
      /* delay in JavaScript */
      setTimeout(() => {
        this.i = this.i + 1
        /* OK 1 Mal angeklickt -> wechsel der Sprechblase */
        if (this.i === 1) {
          this.showPermissiondialog1 = false
          this.showPermissiondialog2 = true
          this.showPermissiondialog3 = false
        }
        /* OK 2 Mal angeklickt -> wechsel der Sprechblase */
        if (this.i === 2) {
          this.showPermissiondialog1 = false
          this.showPermissiondialog2 = false
          this.showPermissiondialog3 = true
        }
        /* OK 1 Mal angeklickt -> alles auf der Seite wird verborgen */
        if (this.i === 3) {
          this.showPermissiondialog1 = false
          this.showPermissiondialog2 = false
          this.showPermissiondialog3 = false
          setTimeout(() => {
            this.showPoliceOfficer = false
          }, this.delayPoliceDialog)
          this.go = false
          setTimeout(() => {
            this.los = true
          }, this.delayGoOn)
        }
      }, this.delayButton)
    },
    showSimulation () {
      this.$router.push('Overview')
    },
    readyForSimulation () {
      global.rpc.call('readyForSimulation', null)
    },
    notifyIntro: function (data) {
      this.notification = data.error
    }
  }
}
</script>

<style scoped>
  @import '../assets/globalstyle.css';

  @keyframes bounceOfficer {
    0%{
      transform-origin: left;
      transform: translate(-200px, 0px);
    }
    60%{
      transform: translate(20px, 0px);
    }
  }
  .bounceOfficer-enter-active{
    animation: bounceOfficer 1s;
  }
  .bounceOfficer-leave-active{
    animation: bounceOfficer 0.7s reverse;
  }
  .dialog img{
    max-width:50%;
    height: auto;
    width: auto\9;
    position:absolute;
    bottom:180px;
    left:230px;
  }
  .officer{
    max-width:80%;
    height: auto;
    width: auto\9;
    position:absolute;
    bottom:0px;
    left:0px;
  }
  body{
    background: url(../assets/leipzig.jpg) no-repeat center center fixed;
    -webkit-background-size: cover;
    -moz-background-size: cover;
    -o-background-size: cover;
    background-size: cover;
  }
  .button{
    position: absolute;
  }
  .button.start {
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
  .button.switch {
    bottom:20px;
    right:20px;
  }
  .button.weiter {
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
</style>
