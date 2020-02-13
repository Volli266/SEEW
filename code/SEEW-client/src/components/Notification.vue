<template>
  <div id="notification-container">
    <div id="notification" v-show="message.length > 0" v-bind:style="{ backgroundColor: color}">
      {{message}}
    </div>
  </div>
</template>

<script>
export default {
  name: 'Notification',
  props: {
    message: {default: ''},
    color: {default: 'lightcoral'}
  },
  data () {
    return {}
  }
}
</script>

<style scoped>
  #notification-container {
    position: fixed;
    width: 98%;
    bottom: 0;
    padding: 10px;
    text-align: left;
  }

  #notification {
    background-color: lightcoral;
    border-radius: 8px;
    padding: 10px;
  }
</style>
