<template>
  <div>
    <div id="container">
      <GroupCreation />
      <GroupSelection />
    </div>
    <Notification v-bind:message="notification"/>
  </div>
</template>

<script>
import {global} from '../main.js'
import GroupCreation from './GroupCreation'
import GroupSelection from './GroupSelection'
import Notification from './Notification'

export default {
  name: 'GroupOverview',
  components: {Notification, GroupSelection, GroupCreation},
  created () {
    global.rpc.register('notifyGroupOverview', this.notifyGroupOverview)
    global.rpc.register('showIntro', this.showIntro)
  },
  data () {
    return {
      notification: ''
    }
  },
  methods: {
    notifyGroupOverview: function (data) {
      this.notification = data.error
    },
    showIntro: function () {
      this.$router.push('Intro')
    }
  }
}
</script>

<style scoped>
  #container {
    display: flex;
    flex-direction: row;
    margin-left: auto;
    margin-right: auto;
    margin-top: 5%;
    width: 85%;
    height: 79vh;
    min-width: 748px;
    min-height: 305px;
    max-height: 740px;
  }
</style>
