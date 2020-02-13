<template>
  <div>
    <img src="../assets/Dino.svg" alt="Logo"/>
    <h1>Software-Entwicklungs-Experimentier-Workbench</h1><br>
    <div class="container">
      <div class="item"></div>
      <div class="item">
        <h2>Registrieren</h2><br>
        <input v-model="registerUsername" type="text" placeholder="Benutzername">
        <br>
        <input v-model="registerPassword" type="password" placeholder="Passwort">
        <br>
        <button v-on:click="register">Registrieren</button>
      </div>
      <div class="item">
        <h2>Login</h2>
        <br>
        <input v-model="loginUsername" type="text" placeholder="Benutzername">
        <br>
        <input v-model="loginPassword" type="password" placeholder="Passwort">
        <br>
        <button v-on:click="login">Login</button>
      </div>
      <div class="item"></div>
    </div>
    <Notification v-bind:message="notification"/>
  </div>

</template>

<script>
import {global} from '../main.js'
import Notification from './Notification'

export default {
  name: 'Login',
  components: {Notification},
  created () {
    global.rpc.register('confirmLogin', this.confirmLogin)
    global.rpc.register('notifyLogin', this.notifyLogin)
    global.rpc.register('alreadyStarted', this.alreadyStarted)
  },
  data () {
    return {
      loginUsername: '',
      loginPassword: '',
      registerUsername: '',
      registerPassword: '',
      notification: ''
    }
  },
  methods: {
    login: function () {
      global.rpc.setUsername(this.loginUsername)
      global.rpc.setPassword(this.loginPassword)
      global.rpc.call('login', {})
    },
    register: function () {
      global.rpc.setUsername(this.registerUsername)
      global.rpc.setPassword(this.registerPassword)
      global.rpc.call('register', {})
    },
    confirmLogin: function () {
      this.$router.push('GroupOverview')
    },
    notifyLogin: function (data) {
      this.notification = data.error
    },
    alreadyStarted: function () {
      this.$router.push('Overview')
    }
  }
}
</script>

<style scoped>
  @import "../stylesheet.css";
  input {
    margin-bottom: 10px;
  }

  img {
    width: 10%;
    margin-top: 50px;
  }

  .container {
    display:flex;
    justify-content: space-evenly;
    align-items: stretch;
  }

  .item {
    flex: auto;
  }
</style>
