<template>
  <div>
    <div id="top">
      <div class="top-el" v-on:click="showOverviewf"><v-icon color="white"> arrow_back</v-icon>{{showOverview ? phases[currentPhase] : "Sprintübersicht"}}</div>
      <div class="top-el2">
        {{showOverview ? "SPRINTÜBERSICHT" : phases[currentPhase].toUpperCase() }}
        <v-icon v-on:click="showAnimation" flat id="icon" size="31" title="Was bedeutet die Phase?">live_help</v-icon>
      </div>
      <div class="top-el" v-on:click="confirmPhase">Sprint-Phase abschließen <v-icon color="white">arrow_forward</v-icon></div>
    </div>
    <div v-if="showOverview">
      <div id="window" tabindex="0" ref="all" v-on:keypress.space="keyEvent">
        <div id="graphic" >
          <div v-on:click="focusOn('ProductBacklog')" id="ProductBacklog" class="phase productBacklog" v-bind:class="{ highlight: currentPhase === 0}">
            <label>Product-Backlog</label>
            <img class="img" id="imgProductBacklog" src="../assets/zwischensicht/Product-Backlog.png" alt="Screenshot"/>
          </div>
          <span class="arrow downRight">&#11185;</span>
          <div v-on:click="focusOn('Backlog')" id="Backlog" class="phase backlog" v-bind:class="{ highlight: currentPhase === 1}">
            <label>Sprint-Backlog</label>
            <img class="img" id="imgBacklog" src="../assets/zwischensicht/Sprint-Backlog.png" alt="Screenshot"/>
          </div>
          <span class="arrow upLeft"> &#11009;</span>
          <div v-on:click="focusOn('Estimation')" id="Estimation" class="phase estimation" v-bind:class="{ highlight: currentPhase === 2}">
            <label>Stories-Schätzen</label>
            <img class="img" id="imgEstimation" src="../assets/zwischensicht/Schaetzen.png" alt="Screenshot"/>
          </div>
          <span class="arrow rightUp"> &#11008;</span>
          <div v-on:click="focusOn('SprintProgress')" id="SprintProgress" class="phase sprintProgress" v-bind:class="{ highlight: currentPhase === 3}">
            <label>Sprintverlauf</label>
            <img class="img" id="imgSprint" src="../assets/zwischensicht/Sprintverlauf.png" alt="Screenshot"/>
          </div>
          <div class="arrow downLeftVerlaufReview"><div >&#11010;</div></div>
          <div v-on:click="focusOn('Review')" id="Review" class="phase review" v-bind:class="{ highlight: currentPhase === 4}">
            <label>Review</label>
            <img class="img" id="imgReview" src="../assets/zwischensicht/Review.png" alt="Screenshot"/>
          </div>
          <div class="arrow downRightReviewRetro"><div >&#11011;</div></div>
          <span class="arrow left">&#8678;</span>
          <div v-on:click="focusOn('Retro')" id="Retro" class="phase retro" v-bind:class="{ highlight: currentPhase === 5}">
            <label>Retrospektive</label>
            <img class="img" id="imgRetro" src="../assets/zwischensicht/Retro.png" alt="Screenshot"/>
          </div>
          <span class="arrow upRight">&#11189;</span>
          <div v-on:click="focusOn('Evaluation')" id="Evaluation" class="phase evaluation" v-bind:class="{ highlight: currentPhase === 6}">
            <label>Auswertung</label>
            <img class="img" id="imgProjektabschluss" src="../assets/zwischensicht/Auswertung.png" alt="Screenshot"/>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <ProductBacklog v-on:loadText="initText" v-if="currentPhase === 0"></ProductBacklog>
      <Backlog v-on:loadText="initText" v-if="currentPhase === 1"></Backlog>
      <SprintPlanningEstimation v-on:loadText="initText" v-if="currentPhase === 2"></SprintPlanningEstimation>
      <SprintProgress v-on:loadText="initText" v-if="currentPhase === 3"></SprintProgress>
      <Review v-on:loadText="initText" v-if="currentPhase === 4"></Review>
      <Retro v-on:loadText="initText" v-if="currentPhase === 5"></Retro>
      <Evaluation v-on:loadText="initText" v-if="currentPhase === 6"></Evaluation>
    </div>
    <div class="dialog">
      <transition name="bounce">
        <img v-show="showDino" class="dino" src="../assets/Dino.svg" style="z-index: 2"/>
      </transition>
      <SpeechBubble :show="showPermissiondialog1" v-bind:text="infoText"
                    bubble-pointer="rightTop" v-bind:bubble-top="160"
                    v-bind:bubble-width="400" v-bind:bubble-left="680" style="z-index: 2">
      </SpeechBubble>
    </div>
    <Notification v-bind:message="notification"></Notification>
  </div>
</template>

<script>

import {global} from '../main.js'
import ProductBacklog from './ProductBacklog.vue'
import SprintPlanningEstimation from './SprintPlanningEstimation.vue'
import SprintProgress from './SprintProgress.vue'
import Review from './Review'
import Retro from './Retro'
import Evaluation from './Evaluation'
import Backlog from './Backlog'
import Notification from './Notification'
import SpeechBubble from './SpeechBubble'

export default {
  name: 'ProzessZwischensicht',
  components: {
    SpeechBubble, Notification, Backlog, ProductBacklog, SprintPlanningEstimation, SprintProgress, Review, Retro, Evaluation},
  created: function () {
    global.rpc.register('updateSprintOverview', this.updateSprintOverview)
    global.rpc.register('notifySprintOverview', this.notifySprintOverview)
    global.rpc.call('requestUpdateSprintOverview', null)
  },
  data () {
    return {
      showOverview: true,
      currentPhase: 0,
      phases: ['Product-Backlog', 'Sprint-Backlog', 'Stories-Schätzen', 'Sprintverlauf', 'Review', 'Retrospektive', 'Auswertung'],
      counter: 0,
      rectTop: '',
      rectLeft: '',
      notification: '',
      infoText: 'Im Sprintübersicht könnt ihr die Vorgehensweise des Projekts sehen. Die Methode heißt "Scrum"! Sie ist sehr beliebt und wird weltweit für Dino-Entwicklung benutzt.',
      showDino: false,
      showPermissiondialog1: false
    }
  },
  methods: {
    showAnimation () {
      this.showDino = true
      this.showPermissiondialog1 = true
      setTimeout(() => {
        this.showDino = false
        this.showPermissiondialog1 = false
      }, 8000)
    },
    showOverviewf () {
      this.showOverview = !this.showOverview
      if (this.showOverview === true) {
        this.infoText = 'Im Sprintübersicht könnt ihr die Vorgehensweise des Projekts sehen. Die Methode heißt "Scrum"! Sie ist sehr beliebt und wird weltweit für Dino-Entwicklung benutzt.'
      }
      this.showDino = false
      this.showPermissiondialog1 = false
    },
    initText: function (infoFromChild) {
      this.infoText = infoFromChild
    },
    /**
     * ändert Variable show in msg (Übergabe durch Event von Navigation)
     * Ansichten werden aufgerufen, wenn show ihren Wert annimmt
     */
    change: function () {
      global.rpc.call('requestUpdate', null)
    },
    /**
     * Erhält aktuelle Phase vom Server und ruft highlightPhase() auf
     */
    updateSprintOverview: function (data) {
      this.currentPhase = data.currentPhase
    },
    notifySprintOverview: function (data) {
      this.notification = data.error
    },
    /**
     * markiert Phase nicht mehr
     * ruft nextPhase() im Server auf
     */
    confirmPhase: function () {
      global.rpc.call('confirmPhase', null)
      this.showDino = false
      this.showPermissiondialog1 = false
    },
    /**
     * Klicken auf aktuelle Phase öffnet Phase
     * falls ProductBacklog oder Backlog, wird Event an Elternelement (Overview) gesendet, wird dann dort aufgerufen
     */
    focusOn: function (actualPhase) {
      if (actualPhase === this.currentPhase) {
        if (actualPhase === 'ProductBacklog' || actualPhase === 'Backlog') {
          this.$emit('changeNav', 'BacklogNav')
        } else {
          this.change(actualPhase)
        }
      }
    }
  }
}
</script>

<style scoped>
  @import "../stylesheet.css";
  div, img {
    border-radius: 5px;
  }
  label {
    font-size: 1vw;
    font-weight: bold;
    border-bottom: solid;
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
  }
  .dino {
    width: 120px;
  }
  @keyframes bounce{
    0%{
      transform-origin: top;
      transform: translate(200px, 0px);
    }
    60%{
      transform: translate(-20px, 0px);
    }
  }
  .bounce-enter-active{
    animation: bounce 1s;
  }
  .bounce-leave-active{
    animation: bounce 0.7s reverse;
  }
  #icon{
    color:white;
  }
  #icon:hover{
    color: darkgreen;
    cursor: pointer;
  }
  .dialog img{
    position:absolute;
    top:100px;
    right:50px;
  }
  #top {
    background-color: #4CAF50;
    border-radius: 0;
    display: flex;
    justify-content: space-between;
    height: 80px;
  }
  .top-el, .top-el2 {
    color: white;
    padding: 20px;
    font-size: 20px;
  }
  .top-el:hover {
    background-color: #3e8e41;
    cursor: pointer;
  }

  #window {
    height: 100vh;
    z-index: 1;
    position: absolute;
  }
  .img {
    position: relative;
    width: 100%;
  }

  #graphic {
    width: 100%;
  }

  .phase {
    position: absolute;
    cursor: pointer;
    height: 8vw;
    width: 11vw;
    border-style: solid;
    box-shadow: 0 10px 20px rgba(0,0,0,0.19), 0 6px 6px rgba(0,0,0,0.23);
    display: flex;
    flex-direction: column;
  }

  .highlight {
    box-shadow: 0 0 25px 7px yellow;
  }

  .phase.productBacklog {
    left: 5vw;
    top: 40vh;
  }

  .phase.backlog {
    left: 27vw;
    top: 60vh;
  }

  .phase.estimation {
    left: 19vw;
    top: 25vh;
  }

  .phase.sprintProgress {
    left: 37vw;
    top: 13vh;
  }

  .phase.review {
    left: 54.5vw;
    top: 25vh;
  }

  .phase.retro {
    left: 47vw;
    top: 60vh;
  }

  .phase.evaluation {
    left: 70vw;
    top: 40vh;
  }

  .arrow {
    font-size: 4.7vw;
    position: absolute;
  }

  .arrow.rightUp {
    left: 31.5vw;
    top: 22vh;
  }

  .arrow.left {
    left: 41vw;
    top: 62vh;
  }

  .arrow.upLeft {
    left: 28vw;
    top: 43vh;
  }

  .arrow.downRight {
    left: 18.5vw;
    top: 56.5vh;
  }

  .arrow.upRight {
    left: 62.5vw;
    top: 56.5vh;
  }

  .arrow.downLeftVerlaufReview{
    left: 49.5vw;
    top: 23vh;
    overflow: hidden;
  }

  .arrow.downRightReviewRetro{
    left: 54.5vw;
    top: 43vh;
    overflow: hidden;
  }

</style>
