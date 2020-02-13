<template>
  <div>
    <div class="heading">
      <h1 v-if="success">Der Gorilla wurde besiegt!</h1>
      <h1 v-else>Der Gorilla wurde nicht besiegt!</h1>
    </div>
      <div class="epic" v-for="(epic, index) in epics" v-bind:key="index">
        <h2>Sprint {{ index + 1 }}</h2>
        <h3>Daily: {{dailyDone[index] ? 'Ja' : 'Nein'}}</h3>
        <h3>Zeit: {{sprintTime[index] / 30 }}</h3>
        <div class="way">
          <h3>Gewählter Weg:</h3>
          <br>
          <div v-for="(d, index) in epic.decisions" v-bind:key="d">
            <span class="unChosen" v-bind:class="{ chosen : index === epic.chosenDecision }" > {{ d.decisionName }} </span>
          </div>
        </div>
        <div class="con1">
          <div>
            <h4>Pool 1</h4>
            <br>
            <div v-for="story in epic.chosenDecisionEval.storyPoolOne" v-bind:key="story">
              <span class="unChosen" v-bind:class="{ chosen: story.storyId === epic.chosenDecisionEval.chosenStoryOne  }">{{ story.storyName }} </span>
            </div>
          </div>
          <div>
            <h4>Pool 2</h4>
            <br>
            <div v-for="story in epic.chosenDecisionEval.storyPoolTwo" v-bind:key="story">
              <span class="unChosen" v-bind:class="{ chosen: story.storyId === epic.chosenDecisionEval.chosenStoryTwo  }">{{ story.storyName }} </span>
            </div>
          </div>
        </div>
        <div class="way">
          <h3>Bearbeitete Stories</h3>
        </div>
        <div class="con1">
          <div>
            <h4>{{epic.chosenDecisionEval.chosenStoryOneEval.storyName}}</h4>
            <h5>Schätzung: {{epic.chosenDecisionEval.chosenStoryOneEval.storyEstimation}}</h5>
            <div v-for="task in epic.chosenDecisionEval.chosenStoryOneEval.storyTasks" v-bind:key="task.taskId">
              <br>
              <table>
                <tr>
                  <td class="chosen">Name:</td>
                  <td class="chosen">{{task.taskName}}</td>
                </tr>
                <tr>
                  <td>Fortschritt:</td>
                  <td>{{task.taskCompletion}}%</td>
                </tr>
                <tr>
                  <td>Fehlerwahrscheinlichkeit:</td>
                  <td>{{task.taskFailureProbability}}%</td>
                </tr>
                <tr>
                  <td>Abgeschlossen in Sprint:</td>
                  <td>{{task.taskCompletion === 100 ? task.taskSprint + 1 : '-'}}</td>
                </tr>
                <tr>
                  <td>Getestet:</td>
                  <td>{{task.tested > 0 ? "Ja" : "Nein"}}</td>
                </tr>
                <tr>
                  <td>Beteiligte Entwickler:</td>
                  <td>{{ task.involvedDevelopers.length === 0 ? "-" : task.involvedDevelopers.map(function (d) {
                    return d.developerForename + " " + d.developerSurname }).join(', ')}}</td>                </tr>
              </table>
            </div>
          </div>
          <div>
            <h4>{{epic.chosenDecisionEval.chosenStoryTwoEval.storyName}}</h4>
            <h5>Schätzung: {{epic.chosenDecisionEval.chosenStoryTwoEval.storyEstimation}}</h5>
            <div v-for="task in epic.chosenDecisionEval.chosenStoryTwoEval.storyTasks" v-bind:key="task.taskId">
              <br>
              <table>
                <tr>
                  <td class="chosen">Name:</td>
                  <td class="chosen">{{task.taskName}}</td>
                </tr>
                <tr>
                  <td>Fortschritt:</td>
                  <td>{{task.taskCompletion}}%</td>
                </tr>
                <tr>
                  <td>Fehlerwahrscheinlichkeit:</td>
                  <td>{{task.taskFailureProbability}}%</td>
                </tr>
                <tr>
                  <td>Abgeschlossen in Sprint:</td>
                  <td>{{task.taskCompletion === 100 ? task.taskSprint + 1 : '-'}}</td>
                </tr>
                <tr>
                  <td>Getestet:</td>
                  <td>{{task.tested > 0 ? "Ja" : "Nein"}}</td>
                </tr>
                <tr>
                  <td>Beteiligte Entwickler:</td>
                  <td>{{ task.involvedDevelopers.length === 0 ? "-" : task.involvedDevelopers.map(function (d) {
                          return d.developerForename + " " + d.developerSurname }).join(', ')}}</td>
                </tr>
              </table>
            </div>
          </div>
        </div>
      </div>
    <div class="epic">
      <h2>Sprint 4</h2>
      <h3>Daily: {{dailyDone[3] ? 'Ja' : 'Nein'}}</h3>
      <div class="way">
        <h3>Nicht beendete Aufgaben</h3>
        <br>
        <div v-for="task in leftoverTasks" v-bind:key="task.taskId">
          {{task.taskName}}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {global} from '../main.js'
export default {
  name: 'Evaluation',
  created: function () {
    global.rpc.register('updateEvaluation', this.updateEvaluation)
    global.rpc.call('requestUpdateEvaluation', null)
    this.$emit('loadText', this.infoText)
  },
  data () {
    return {
      infoText: 'Das ist Evaluation',
      success: false,
      epics: [],
      leftoverTasks: [],
      dailyDone: [],
      sprintTime: []
    }
  },
  methods: {
    back: function () {
      this.$emit('changeView', 'Between')
    },
    updateEvaluation: function (data) {
      this.success = data.success
      this.epics = data.epics
      this.leftoverTasks = data.leftoverTasks
      this.dailyDone = data.dailyDoneEval
      this.sprintTime = data.sprintTimeEval
    }
  }
}
</script>

<style scoped>
  @import "../stylesheet.css";
  td {
    padding-left: 10px;
  }
  .unChosen {
    color: darkgrey;
  }
  .chosen {
    color: #4CAF50;
  }
  .heading {
    padding: 30px;
  }
  .epic {
    text-align: left;
    padding: 30px;
  }
  .way {
    padding: 30px;
  }
  .con1 {
    display: flex;
    justify-content: space-evenly;
  }
</style>
