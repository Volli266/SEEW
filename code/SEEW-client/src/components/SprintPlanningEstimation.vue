<template>
  <div id="container">
    <div class = "child">
      <div class="ticket">{{storyOne}} : {{storyOneEstimation}}</div>
      <div class="wholeBox">
        <div class="box-estimation">
          <div class="row-estimation">
            <div tabindex="1" class="number" v-on:click="selectedNumberStoryOne = 0">0</div>
            <div tabindex="2" class="number" v-on:click="selectedNumberStoryOne = 1">1</div>
            <div tabindex="3" class="number" v-on:click="selectedNumberStoryOne = 2">2</div>
          </div>
          <div class="row-estimation">
            <div tabindex="4" class="number" v-on:click="selectedNumberStoryOne = 3">3</div>
            <div tabindex="5" class="number" v-on:click="selectedNumberStoryOne = 4">5</div>
            <div tabindex="6" class="number" v-on:click="selectedNumberStoryOne = 5">8</div>
          </div>
          <div class="row-estimation">
            <div tabindex="7" class="number" v-on:click="selectedNumberStoryOne = 6">13</div>
            <div tabindex="8" class="number" v-on:click="selectedNumberStoryOne = 7">21</div>
            <div class="container-button">
              <button class="button" v-on:click="estimateStoryOne">OK</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="child">
      <div class="ticket">{{storyTwo}} : {{storyTwoEstimation}}</div>
      <div class="wholeBox">
        <div class="box-estimation">
          <div class="row-estimation">
            <div tabindex="1" class="number" v-on:click="selectedNumberStoryTwo = 0">0</div>
            <div tabindex="2" class="number" v-on:click="selectedNumberStoryTwo = 1">1</div>
            <div tabindex="3" class="number" v-on:click="selectedNumberStoryTwo = 2">2</div>
          </div>
          <div class="row-estimation">
            <div tabindex="4" class="number" v-on:click="selectedNumberStoryTwo = 3">3</div>
            <div tabindex="5" class="number" v-on:click="selectedNumberStoryTwo = 4">5</div>
            <div tabindex="6" class="number" v-on:click="selectedNumberStoryTwo = 5">8</div>
          </div>
          <div class="row-estimation">
            <div tabindex="7" class="number" v-on:click="selectedNumberStoryTwo = 6">13</div>
            <div tabindex="8" class="number" v-on:click="selectedNumberStoryTwo = 7">21</div>
            <div class="container-button">
              <button class="button" v-on:click="estimateStoryTwo">OK</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <Notification v-bind:message="notification"/>
  </div>
</template>

<script>
import {global} from '../main.js'
import Notification from './Notification'

export default {
  name: 'SprintPlanningEstimation',
  components: {Notification},
  created: function () {
    global.rpc.register('updateEstimation', this.updateEstimation)
    global.rpc.register('notifyEstimationRejection', this.notifyEstimation)
    global.rpc.call('requestUpdateEstimation', null)
    this.$emit('loadText', this.infoText)
  },
  data () {
    return {
      infoText: 'In dieser Phase sollt ihr einshätzen, wie schwer bzw. einfach eine Story zu entwickeln ist. Wenn alle eine Story geschätzt haben - wird ein Mittelwert gebildet und entsprächend angezeigt.',
      storyOne: null,
      storyTwo: null,
      storyOneEstimation: 0,
      storyTwoEstimation: 0,
      selectedNumberStoryOne: null,
      selectedNumberStoryTwo: null,
      notification: ''
    }
  },
  methods: {
    updateEstimation: function (data) {
      if (data.chosenStoryOne && data.chosenStoryTwo) {
        this.storyOne = data.chosenStoryOne.storyName
        this.storyTwo = data.chosenStoryTwo.storyName
        this.storyOneEstimation = data.chosenStoryOne.storyEstimation
        this.storyTwoEstimation = data.chosenStoryTwo.storyEstimation
      }
    },
    estimateStoryOne: function () {
      this.notification = 'Story 1 geschätzt'
      global.rpc.call('estimateStoryOne', {estimationValue: this.selectedNumberStoryOne})
    },
    estimateStoryTwo: function () {
      this.notification = 'Story 2 geschätzt'
      global.rpc.call('estimateStoryTwo', {estimationValue: this.selectedNumberStoryTwo})
    },
    notifyEstimation: function (data) {
      this.notification = data.error
    }
  }
}
</script>

<style scoped>
  #container {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    display: flex;
    flex-direction: row;
  }
  .child{
    flex: 1;
  }
  h1 {
    text-align: center;
    font-weight: bold;
  }
  .ticket {
    width: 30vw;
    height: 17vh;
    margin-left: auto;
    margin-right: auto;
    background-color: #b7b9bc;
    margin-top: 20px;
    font-size: 18px;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;
    border-style: solid;
    border-width: 1px;
    border-radius: 10px;
  }
  .wholeBox {
    margin-top: 25px;
    display: flex;
    flex-direction: row;
    justify-content: center;
  }
  .box-estimation {
    display: flex;
    flex-direction: column;
    width: 30vw;
    height: 60vh;
    background-color: white;
    margin-right: 4%;
    border-style: solid;
    border-width: 1px;
    border-radius: 10px;
  }

  .row-estimation{
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
    width: 100%;
    height: 30vh;
  }
  .number {
    box-sizing: border-box;
    background-color: white;
    margin-top: 10px;
    width: 29%;
    height: 80%;
    font-size: 32px;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;
    border-style: solid;
    border-width: 2px;
    border-radius: 10px;
    border-color: green;
    outline: none;
  }
  .number:focus {
    background-color: #4CAF50;
  }
  .number:hover {
    transform: scale(1.05);
    cursor: pointer;
    transition: 0.5s;
    background-color: #4CAF50;
  }
  .container-button {
    width: 29%;
    height: 80%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .button {
    width: 5vw;
    height: 8vh;
    margin: 10%;
    font-size: 20px;
    border-style: solid;
    border-width: 1px;
    border-radius: 10px;
    border-color: black;
    outline: none;
  }

  .button:hover {
    transform: scale(1.05);
    cursor: pointer;
    transition: 0.5s;
    background-color: #4CAF50;
  }

  .box-estimated-tickets {
    box-sizing: border-box;
    margin-left: 4%;
    width: 30vw;
    height: 60vh;
    overflow-y: scroll;
    border-color: #efeded;
    border-left-style: solid;
    border-top-style: solid;
    border-bottom-style: solid;
    border-width: 5px;
    border-radius: 10px;
    padding-top: 3px;
  }

  .estimated-ticket {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    box-sizing: border-box;
    width: 98%;
    height: 18%;
    border-style: solid;
    margin-left: 1.25%;
    margin-top: 5px;
    border-radius: 5px;
    padding-left: 10px;
    padding-right: 10px;
    font-size: 20px;
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
    background-color: #4CAF50;
  }

</style>
