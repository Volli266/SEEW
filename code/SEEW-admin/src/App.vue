<template>
  <div id="app">
    <h1>Admin-Gruppenverwaltung</h1>
    <div id="components">
      <StudentList/>
      <div id="groups">
        <div id="groupsLeft">
          <group1/>
          <group3/>
        </div>
        <div id="groupsRight">
          <group2/>
          <group4/>
        </div>
      </div>
        <div id="ButtonLeft">
          <button3/>
        </div>
        <div id="ButtonTopRight">
          <button2/>
        </div>
        <div id="ButtonBottomRight">
          <button4/>
        </div>
    </div>
    <random/>
  </div>
</template>

<script>
  import StudentList from './components/StudentList'
  import group1 from './components/group1'
  import group2 from './components/group2'
  import group3 from './components/group3'
  import group4 from './components/group4'
  import button2 from './components/button2'
  import button3 from './components/button3'
  import button4 from './components/button4'
  import random from './components/random'

  export default {
    name: 'App',
    components: {
      StudentList,
      group1,
      group2,
      group3,
      group4,
      button2,
      button3,
      button4,
      random
    }
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
  }
  #components {
    display: flex;
    flex-direction: row;
  }
  #groups {
    display: flex;
    flex-direction: row;
    margin-left: 15%;
    width: 45%;
  }
  #groupsLeft {
    display: flex;
    flex-direction: column;
    width: 50%;
    margin-top: 3%;
  }
  #groupsRight {
    display: flex;
    flex-direction: column;
    width: 50%;
    margin-top: 3%;
  }
  h1 {
    text-align: center;
    font-weight: bold;
  }
  #ButtonLeft {
    display: flex;
    flex-direction: column;
    width: 5vw;
    height: 5vw;
    margin-left: -37.3%;
    margin-top: 26%;
  }
  #ButtonTopRight {
    display: flex;
    flex-direction: column;
    width: 5vw;
    height: 5vw;
    margin-top: 7.7%;
    margin-left: 17.5%;
  }
  #ButtonBottomRight {
    display: flex;
    flex-direction: column;
    width: 5vw;
    height: 5vw;
    margin-top: 26%;
    margin-left: -5%;
  }
</style>
