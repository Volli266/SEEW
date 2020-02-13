<template>
  <div class="navigation-container">
    <div id="time">
      <Time></Time>
    </div>
    <div class="navigation-item">
      <img src="../assets/Navigation/SprintVerlauf.svg"  v-on:click="change('Sprint')" id="Sprint" alt="Sprint" title="Sprintübersicht" >
    </div>
    <div  class="navigation-item">
      <img src="../assets/Navigation/Developer.svg"  v-on:click="change('DevelopView')" id="DevelopView" alt="Entwickler" title="Deine Entiwckler">
    </div>
    <div class="navigation-item">
      <img v-on:click="change('DinoView')" id="DinoView" src="../assets/Dino.svg" alt="Dino" title="Euer Dino">
    </div>
  </div>

</template>

<script>
import Time from './Time'
export default {
  name: 'Navigation',
  components: {Time},
  data () {
    return {
      currentNavigation: 'Sprint'
    }
  },
  /**
   * 'Sprint' wird farbig markiert
   */
  mounted: function () {
    document.getElementById(this.currentNavigation).style.boxShadow = '0 0 25px 7px yellow'
  },
  methods: {
    /**
     * sendet Event an Parent (Overview), dass Ansicht geändert wird
     *
     * @param nav jeweiliger Name des geklickten divs
     */
    change: function (nav) {
      this.$emit('changeNav', nav)
    },
    /**
     * markiert neue aktive Ansicht gelb und die alte Ansicht wird nicht mehr markiert
     */
    highlight: function () {
      document.getElementById(this.currentNavigation).style.boxShadow = ''
      document.getElementById(this.navig).style.boxShadow = '0 0 25px 7px yellow'
      this.currentNavigation = this.navig
    }
  },
  /**
   * wird aufgerufen, wenn sich Wert von navig ändert
   */
  watch: {
    navig: function () {
      this.highlight()
    }
  },
  props: ['navig']
}
</script>

<style scoped>
  @import '../stylesheet.css';

  .navigation-container {
    height: 100vh;
  }

  .navigation-item {
    cursor: pointer;
    width: 120px;
    margin: 50px 0px 0px 10px;
    justify-content: flex-start;
    align-items: center;
  }
  #Sprint, #DevelopView, #DinoView{
    width: 110px;
  }

  #Sprint:hover, #DevelopView:hover, #DinoView:hover {
    box-shadow: 0 10px 20px rgba(0,0,0,0.19), 0 6px 6px rgba(0,0,0,0.23);
  }

  #time {
    padding-top: 5px;
    background-color: #4CAF50;
    height: 75px;
  }
</style>
