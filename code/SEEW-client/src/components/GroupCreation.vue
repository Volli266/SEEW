<template>
  <div id="creation-container">
    <h1>Gruppe erstellen</h1>
    <div id="group-name">
      <label for="group-name-input" class="tooltip">
        Name: &#9432;
        <span class="tooltip-text">mind. 1 Buchstabe/Zahl</span>
      </label>
      <input id="group-name-input" v-bind:class="{noError: validGroupName, error: !validGroupName}" v-model="groupName"
             placeholder="Gruppenname" maxlength="30"></div>
    <div id="group-color">
      <label for="group-color-picker">Farbe:</label>
      <input type="color" id="group-color-picker" v-model="groupColor" >
    </div>
    <button v-bind:style="{borderColor: groupColor}" id="create-button" v-on:click="createGroup">Erstellen</button>
  </div>
</template>

<script>
import {global} from '../main.js'

export default {
  name: 'GroupCreation',
  data () {
    return {
      groupName: '',
      groupColor: 'green',
      validGroupName: true
    }
  },
  methods: {
    // Notification "verschwindet"
    // Gruppenname wird auf Regel getestet
    // call creatGroup an Server
    createGroup: function () {
      if (this.$parent) {
        this.$parent.notification = ''
      }
      this.validGroupName = /[A-Za-z\d]/.test(this.groupName)
      if (this.validGroupName) {
        global.rpc.call('createGroup', {groupName: this.groupName, groupColor: this.groupColor})
      }
    }
  }
}
</script>

<style scoped>

  #creation-container {
    width: 26%;
    min-width: 229px;
    margin-right: 7%;
    align-self: center;
    border-style: solid;
    border-width: 2px;
    border-radius: 5px;
    transition: 0.8s;
  }

  h1 {
    font-size: 20px;
    margin-top: 10px;
    padding-bottom: 10px;
    border-bottom-style: solid;
    font-family: Verdana, Geneva, sans-serif;
    transition: 0.8s;
  }

  #group-name {
    text-align: left;
    padding-left: 10px;
    font-size: 20px;
    margin-bottom: 15px;
    transition: 0.8s;
  }

  .error {
    border-color: orangered;
  }

  .no-error {
    border-color: #d8d8d8;
  }

  #group-name-input {
    width: 87%;
    margin-top: 10px;
    padding: 10px;
    background-color: #d8d8d8;
    border-style: solid;
    border-width: 1px;
    border-radius: 10px;
    font-size: 15px;
    outline: none;
    transition: 0.5s;
  }

  #group-color {
    text-align: left;
    padding-left: 10px;
    font-size: 20px;
    transition: 0.8s;
  }

  #group-color-picker {
    background-color: #d8d8d8;
    border-style: solid;
    border-color: #d8d8d8;
    border-width: 1px;
    width: 87%;
    margin-top: 10px;
    padding: 10px;
    border-radius: 10px;
    outline: none;
  }

  #group-color-picker:hover {
    cursor: pointer;
  }

  #create-button {
    background-color: #d8d8d8;
    margin-top: 12px;
    margin-bottom: 12px;
    padding: 10px;
    border-style: solid;
    border-width: 1px;
    border-color: #d8d8d8;
    border-radius: 10px;
    font-size: 20px;
    transition: 0.5s;
    outline: none;
  }

  #create-button:hover {
    transform: scale(1.05);
    cursor: pointer;
    transition: 0.5s;
  }

  .tooltip {
    position: relative;
    display: inline-block;
  }

  .tooltip .tooltip-text {
    visibility: hidden;
    background-color: #3f3f3f;
    width: 155px;
    color: #e5e5e5;
    text-align: center;
    font-size: 14px;
    border-radius: 6px;
    padding: 10px;
    position: absolute;
    z-index: 1;
    top: -11px;
    left: 114%;
    opacity: 0;
    transition: opacity 0.4s;
  }

  .tooltip .tooltip-text::after {
    content: "";
    position: absolute;
    top: 66%;
    right: 100%;
    margin-top: -5px;
    border-width: 5px;
    border-style: solid;
    border-color: transparent #3f3f3f transparent transparent;
  }
  .tooltip:hover .tooltip-text {
    visibility: visible;
    opacity: 1;
  }
</style>
