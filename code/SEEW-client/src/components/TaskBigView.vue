<template>
  <v-layout row>
    <div color="primary" dark @click.stop="dialog = true">
      {{TaskObject.taskName}}
    </div>

    <v-dialog v-model="dialog" max-width="600">
      <v-card>
        <!--<v-card-title class="headline"></v-card-title>-->
        <v-toolbar card color="green" dark>
          <v-toolbar-title>{{TaskObject.taskName}}</v-toolbar-title>
        </v-toolbar>

        <v-container class="container">
          <!--
          <div class="row">
            <div class="dummy"></div>
            <v-card class="flex left">
              Beschreibung:
            </v-card>
            <v-card class="flex right">
              {{TaskObject.taskDescription}}
            </v-card>
          </div>-->
          <div class="row">
            <div class="dummy"></div>
            <v-card class="flex left">
              Voraussetzungen:
            </v-card>
            <v-card class="flex right">
              <span v-for="(requierment, index) in TaskObject.taskRequirements" v-bind:key="index">
                {{abilities[requierment + 1] + (index === TaskObject.taskRequirements.length-1 ? '' : ',')}}
              </span>
            </v-card>
          </div>
          <div class="row">
            <div class="dummy"></div>
            <v-card class="flex left">
              Bearbeiter 1:
            </v-card>
            <v-card class="flex right">
              {{ TaskObject.taskDeveloperOne ? TaskObject.taskDeveloperOne.developerForename + " " + TaskObject.taskDeveloperOne.developerSurname : "nicht zugewiesen"}}
            </v-card>
          </div>
          <div class="row">
            <v-btn v-if="devOne !== null" color="green darken-1" flat="flat" @click="devOneAsOne">
              {{devOne.developerForename + " " + devOne.developerSurname}} zuweisen
            </v-btn>
            <v-btn v-if="devTwo !== null" color="green darken-1" flat="flat" @click="devTwoAsOne">
              {{devTwo.developerForename + " " + devTwo.developerSurname}} zuweisen
            </v-btn>
          </div>
          <div class="row">
            <div class="dummy"></div>
            <v-card class="flex left">
              Bearbeiter 2:
            </v-card>
            <v-card class="flex right">
              {{ TaskObject.taskDeveloperTwo ? TaskObject.taskDeveloperTwo.developerForename + " " + TaskObject.taskDeveloperTwo.developerSurname : "nicht zugewiesen"}}
            </v-card>
          </div>
          <div class="row">
            <v-btn v-if="devOne !== null" color="green darken-1" flat="flat" @click="devOneAsTwo">
              {{devOne.developerForename + " " + devOne.developerSurname}} zuweisen
            </v-btn>
            <v-btn v-if="devTwo !== null" color="green darken-1" flat="flat" @click="devTwoAsTwo">
              {{devTwo.developerForename + " " + devTwo.developerSurname}} zuweisen
            </v-btn>
          </div>
          <div class="row"> <p style="color: white">leer</p> </div> <!-- for space-->
          <div class="row">
            <div class="dummy"></div>
            <v-card class="flex left">
              Sprint:
            </v-card>
            <v-card class="flex right">
              {{TaskObject.taskSprint}}
            </v-card>
          </div>
          <div class="row">
            <div class="dummy"></div>
            <v-card class="flex left">
              Status:
            </v-card>
            <v-card class="flex right">
              {{ taskStatusNames[TaskObject.taskStatus]}}
            </v-card>
          </div>
          <div id="spacer"></div>
          <!--<v-layout row>
            <v-flex>
              <v-card-text>
                {{TaskObject.taskDescription}}
              </v-card-text>
              <v-spacer></v-spacer>
            </v-flex>
          </v-layout>
          <v-layout row>
            <v-flex xs6>
              <v-card-text>
                Entwickler:
              </v-card-text>
            </v-flex>
              <v-select class="right-side" item-text= "name" item-value="last"
                        v-model="defaultSelected"
                        :items="people"
                        v-on:input="limiter" chips solo></v-select>
            <v-flex xs6>
            </v-flex>
          </v-layout> -->
        </v-container>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" flat="flat" @click="dialog = false">Speichern</v-btn>
          <v-btn color="green darken-1" flat="flat" @click="dialog = false">Abbrechen</v-btn>
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-layout>
</template>

<script>
import {VCard, VDialog, VLayout, VToolbar} from 'vuetify/lib'
import {global} from '../main.js'
export default {
  name: 'Confirm',
  components: {
    VCard,
    VDialog,
    VLayout,
    VToolbar
  },
  props: {
    TaskObject: {
      type: Object,
      required: false
    },
    devOne: {
      type: Object,
      required: false,
      default: null
    },
    devTwo: {
      type: Object,
      required: false,
      default: null
    }
  },
  data: function () {
    return {
      taskStatusNames: {
        taskReady: 'Bereit',
        taskInProgress: 'in Arbeit',
        taskTesting: 'wird getestet',
        taskInReview: 'wird reviewed'
      },
      dialog: false,
      defaultSelected: {
        name: 'nicht zugewiesen',
        last: ''
      },
      people: [
        {
          name: 'nicht zugewiesen',
          last: ''
        },
        {
          name: 'John',
          last: 'Doe'
        },
        {
          name: 'Harry',
          last: 'Potter'
        },
        {
          name: 'George',
          last: 'Bush'
        }
      ],
      abilities: [
        'Interaktion mit Menschen',
        'Kommunikation mit Menschen',
        'Lesen',
        'Topographie',
        'Verkehrstraining für Fussgänger',
        'Grundsätzliche Bedienung von Kraftfahrzeugen',
        'Truck-Surfing',
        'Universalführerschein',
        'GK Fassaden Raufklettern',
        'Fahrstuhlbenutzung',
        'GK Fliegen',
        'Herabklettern',
        'Weitsprung',
        'Hangeln',
        'Streckenflug',
        'Hochhausklettern',
        'Tarnung',
        'Hilfe von Fensterputzern',
        'Höhenflug',
        'Krafttraining für Höhenflugsaurier',
        'Flugtechniken Shao Lang',
        'Kampftechniken Shao Lang',
        'Flugtechniken „Mitteldeutschland“',
        'Kampftechniken „Mitteldeutschland“',
        'Kampftechniken Peitschenschwanz',
        'Flugtechniken Krallen',
        'Fitness- & Schnelligkeitstraining „Nying Do“',
        'Kampftechnik „Nying Do“',
        'Ertüchtigungstraining „Sachsen“',
        'Allgemeines Kampftraining',
        'Bodenkampftechniken Krallen',
        'Bodenkampftechniken Peitschenschwanz',
        'Gorillasprache',
        'Groß und böse aussehen',
        'Gesteigertes Selbstbewusstsein',
        'Gorillas überzeugen',
        'Primaten Einschüchtern',
        'Schnelligkeits- & Reflextraining'
      ]
    }
  },
  methods: {
    devOneAsOne: function () {
      global.rpc.call('setTaskDeveloperOne', {
        taskId: this.TaskObject.taskId,
        developerId: this.devOne.developerId
      })
    },
    devTwoAsOne: function () {
      global.rpc.call('setTaskDeveloperOne', {
        taskId: this.TaskObject.taskId,
        developerId: this.devTwo.developerId
      })
    },
    devOneAsTwo: function () {
      global.rpc.call('setTaskDeveloperTwo', {
        taskId: this.TaskObject.taskId,
        developerId: this.devOne.developerId
      })
    },
    devTwoAsTwo: function () {
      global.rpc.call('setTaskDeveloperTwo', {
        taskId: this.TaskObject.taskId,
        developerId: this.devTwo.developerId
      })
    }
  }
}
</script>
<style scoped>
  .container{
    display: flex;
    flex-direction: column;
  }
  .row{
    display: flex;
    flex-direction: row;
    min-height: 10px;
  }
  .left{
    font-size: 18px;
    font-weight: lighter;
    text-align: center;
    border-radius: 20px;
    max-height: 40px;
  }
  .right{
    flex: 100;
  }
  #spacer{
    width: 10px;
  }
  #select{
    height: 20px;
  }
  .flex{
    padding: 10px 10px 10px 10px;
    margin: 5px 5px 5px 5px;
    /*border-style: solid;*/
    border-color: black;
    border-width: 2px;
  }
  .v-select, v-select__selections, .v-select__slot {
    max-height:30px;
    padding: 0 0 0 0;}

</style>
