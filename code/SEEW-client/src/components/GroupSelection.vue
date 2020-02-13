<template>
  <div id="overview-container">
    <div id="selection-container">
      <div v-for="group in groups" class="group" v-bind:class="{selected: selectedGroup === group.groupName}"
           v-bind:style="{borderColor: group.groupColor}" v-bind:key="group.groupName"
           v-on:click="selectedGroup = group.groupName">
        <div class="group-name" v-bind:style="{borderBottomColor: group.groupColor}">
          {{ group.groupName }}
        </div>
        <div v-for="(member, index) in group.groupMembers" class="group-member" :id="index" v-bind:key="index">
          {{ member.username }}
        </div>
      </div>
    </div>
    <div id="buttons-container">
      <div>
        <button id="join" v-on:click="joinGroup">Beitreten</button>
        <button id="leave" v-on:click="leaveGroup">Verlassen</button>
      </div>
      <div>
        <button id="start" v-on:click="readyForIntro">Bereit</button>
      </div>
    </div>
  </div>
</template>

<script>
import {global} from '../main.js'

export default {
  name: 'GroupSelection',
  created: function () {
    global.rpc.register('updateGroupList', this.updateGroupList)
    global.rpc.call('requestGroupList', null)
  },
  data () {
    return {
      groups: [],
      selectedGroup: ''
    }
  },
  methods: {
    // hier Methode um eingeloggten Nutzer der angewählten Gruppe hinzufügen
    joinGroup: function () {
      global.rpc.call('joinGroup', {groupName: this.selectedGroup})
    },

    // hier Methode um eingeloggten Nutzer aus der gewählten Gruppe zu entfernen
    leaveGroup: function () {
      global.rpc.call('leaveGroup', null)
    },

    // Methode um Simulation zu starten
    readyForIntro: function () {
      global.rpc.call('readyForIntro', null)
    },
    // update der Gruppenanzeigen und Nutzer
    updateGroupList: function (data) {
      this.groups = data.groups
    }
  }
}
</script>

<style scoped>
  .group-name {
    height: 20%;
    border-bottom: solid;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  .group-member {
    height: 20%;
    text-align: left;
    padding-left: 10px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    border-bottom: solid;
    border-width: 1px;
    border-bottom-color: #d6d4d4;
  }

  .group {
    width: 31%;
    height: 47%;
    border-style: solid;
    margin-left: 1.25%;
    margin-top: 5px;
    border-radius: 5px;
    display: flex;
    flex-direction: column;
    tab-index: -1;
    outline: none;
  }

  .selected {
    border-style: dashed;
  }

  #overview-container {
    display: flex;
    flex-direction: column;
    width: 69%;
    margin-left: 6.2%;
  }

  #selection-container {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    width: 99.5%;
    height: 100%;
    overflow-y: scroll;
    border-color: #efeded;
    border-left-style: solid;
    border-top-style: solid;
    border-bottom-style: solid;
    border-width: 5px;
    border-radius: 10px;
    padding-top: 3px;
  }

  #buttons-container {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-top: 15px;
    padding-left: 1.25%;
  }

  button {
    background-color: #d8d8d8;
    padding: 10px;
    border-radius: 10px;
    font-size: 20px;
    transition: 0.5s;
    outline: none;
    border-style: solid;
    border-width: 1px;
  }

  button:hover {
    transform: scale(1.05);
    cursor: pointer;
    transition: 0.5s;
  }

  #join {
    border-color: green;
  }

  #leave {
    border-color: red;
    margin-left: 3px;
  }

  #start {
    border-color: blue;
  }

  ::-webkit-scrollbar
  {
    width: 12px;
    background-color: #F5F5F5;
  }

  ::-webkit-scrollbar-track
  {
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
    border-radius: 10px;
    background-color: #F5F5F5;
  }

  ::-webkit-scrollbar-thumb
  {
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
    background-color: #555;
  }

  ::-webkit-scrollbar-thumb:hover {
    background-color: #999e9c;
  }
</style>
