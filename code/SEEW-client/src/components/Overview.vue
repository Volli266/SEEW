<template>
  <div class="container">
    <div class="navigation">
      <Navigation v-on:changeNav="updateNav($event)" :navig="nav" />
    </div>
    <div class="content">
      <!--<div class="timer"><Time></Time></div>
      <Cockpit v-if="this.nav === 'Cockpit'"></Cockpit>
      <Dashboard v-else-if="this.nav === 'Dashboard'"></Dashboard>
      <ProductBacklog v-else-if="(this.nav === 'BacklogNav') && !this.backlogged" v-on:changeBacklog="changeBacklog()"></ProductBacklog>
      <Backlog v-else-if="(this.nav === 'BacklogNav') && this.backlogged"></Backlog>-->
      <ProzessZwischensicht v-if="this.nav === 'Sprint'" v-on:changeNav="updateNav($event)"></ProzessZwischensicht>
      <DevelopView v-else-if="this.nav === 'DevelopView'"></DevelopView>
      <DinoView v-else></DinoView>
      <Notification v-bind:message="notification" class="notification"/>
  </div>
  </div>
</template>

<script>
import {global} from '../main.js'
import Navigation from './Navigation.vue'
import Cockpit from './Cockpit.vue'
import Dashboard from './Dashboard.vue'
import Backlog from './Backlog.vue'
import ProductBacklog from './ProductBacklog.vue'
import ProzessZwischensicht from './ProzessZwischensicht.vue'
import DinoView from './DinoView.vue'
import DevelopView from './DevelopView.vue'
import Time from './Time.vue'
import Notification from './Notification'

export default {
  name: 'Overview',
  components: {Navigation, Cockpit, Dashboard, Backlog, ProductBacklog, ProzessZwischensicht, DinoView, DevelopView, Time, Notification},
  created: function () {
    global.rpc.register('notifyOverview', this.notifyOverview)
  },
  data () {
    return {
      nav: 'Sprint',
      backlogged: false,
      notification: ''
    }
  },
  methods: {
    /**
     * wird durch Navigation oder Prozesszwischensicht aufgerufen, ändert Ansicht
     * @param nav Name der Zielnavigation
     */
    updateNav: function (nav) {
      this.nav = nav
    },
    /**
     * wird aufgerufen, wenn ProductBacklog fertig ist
     */
    changeBacklog: function () {
      this.backlogged = true
    },
    notifyOverview: function (data) {
      this.notification = data.error
    }
  }
}
</script>

<style scoped>
  @import "../stylesheet.css";
  .container {
    max-width: none;
    margin: 0;
    padding: 0;
    display: flex;
    flex-direction: row;
  }
  .navigation {
    flex: 1;
    border-right: solid gray 1px;
  }
  .content {
    flex: 9;
  }
</style>
