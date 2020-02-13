<template>
  <div class="DinoView">
    <v-toolbar  height="80" card color="green" dark>
      <v-toolbar-title style="position: center">EUER DINO:</v-toolbar-title>
    </v-toolbar>
    <div class="dino">
        <br>
        <br>
        <h2>Name: {{name}}</h2>
        <br><br><br>
        <div id="container">
          <table>
            <tr>
              <td>Soziale Fähigkeiten:</td>
              <td>{{ social }}</td>
              <td><canvas id="rect1" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Logik:</td>
              <td>{{ logic }}</td>
              <td><canvas id="rect2" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Mut:</td>
              <td>{{ courage }}</td>
              <td><canvas id="rect3" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Reaktionszeit</td>
              <td>{{ reaction }}</td>
              <td><canvas id="rect4" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Beweglichkeit:</td>
              <td>{{ agility }}</td>
              <td><canvas id="rect5" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Geschicklichkeit:</td>
              <td>{{ dexterity }}</td>
              <td><canvas id="rect6" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Stärke:</td>
              <td>{{ strength }}</td>
              <td><canvas id="rect7" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Gewicht:</td>
              <td>{{ weight }}</td>
              <td><canvas id="rect8" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Größe</td>
              <td>{{ size }}</td>
              <td><canvas id="rect9" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Vorderbeine:</td>
              <td>{{ front }}</td>
              <td></td>
            </tr>
            <tr>
              <td>Hinterbeine:</td>
              <td>{{ back }}</td>
              <td></td>
            </tr>
            <tr>
              <td>Schwanz:</td>
              <td>{{ tail }}</td>
              <td></td>
            </tr>
          </table>
        </div>
      </div>
      <div id="container3">
        <h2>Abschlüsse</h2>
        <br><br>
        <div id="container2">
          <div id="left">
            <h3>MÖGLICH</h3>
            <br>
            <div v-bind:key="i" v-for="i in abilitiesPossible">{{ abilities[i-1] }}</div>
          </div>
          <div id="right">
            <h3>ABGESCHLOSSEN</h3>
            <br>
            <div v-bind:key="i" v-for="i in abilitiesComplete">{{ abilities[i-1] }}</div>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
import {global} from '../main.js'

export default {
  name: 'SEEW-Client',
  created: function () {
    global.rpc.register('updateDino', this.updateDino)
    global.rpc.call('requestUpdateDino', null)
  },
  data () {
    return {
      name: 'Torben',
      social: 0,
      logic: 0,
      courage: 0,
      reaction: 0,
      agility: 0,
      dexterity: 0,
      strength: 0,
      weight: 0,
      size: 0,
      front: 'NORMAL',
      back: 'NORMAL',
      tail: 'NORMAL',
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
      ],
      abilitiesPossible: [],
      abilitiesComplete: []
    }
  },
  methods: {
    // Füllt Canvas
    fill: function (id, val) {
      let el = document.getElementById(id)
      if (el !== null) {
        el.getContext('2d').fillRect(0, 0, val * 7, el.offsetHeight)
      }
    },
    // schließt den offenen Tab
    exit: function () {
      window.close()
    },
    goOn: function () {
      this.$router.push('ProductBacklog')
    },
    // Überträgt Daten der JSON auf Variablen und füllt Canvas
    updateDino: function (data) {
      this.name = data.dinoName
      this.social = data.dinoSocial
      this.logic = data.dinoLogic
      this.courage = data.dinoCourage
      this.reaction = data.dinoReaction
      this.agility = data.dinoAgility
      this.dexterity = data.dinoDexterity
      this.strength = data.dinoStrength
      this.weight = data.dinoWeight
      this.size = data.dinoSize
      this.front = data.dinoFront
      this.back = data.dinoBack
      this.tail = data.dinoTail

      this.abilitiesComplete = data.dinoAbilities
      let all = []
      for (let i = 0; i < this.abilities.length; ++i) {
        all.push(i)
      }
      this.abilitiesPossible = all.reduce(function (acc, e) {
        if (data.dinoAbilities.includes(e)) {
          return acc
        } else {
          acc.push(e)
          return acc
        }
      }, [])

      this.fill('rect1', this.name)
      this.fill('rect1', this.social)
      this.fill('rect2', this.logic)
      this.fill('rect3', this.courage)
      this.fill('rect4', this.reaction)
      this.fill('rect5', this.agility)
      this.fill('rect6', this.dexterity)
      this.fill('rect7', this.strength)
      this.fill('rect8', this.weight)
      this.fill('rect9', this.size)
    }
  }
}
</script>

<style scoped>

  #container {
    font-size: 20px;
    display: flex;
    justify-content: center;
  }

  td {
    padding: 5px;
  }

  #rect1, #rect2, #rect3, #rect4, #rect5, #rect6, #rect7, #rect8, #rect9 {
    border-radius: 8px;
    transition: .5s ease;
    width: 70px;
    height: 17px;
    border: 1px solid black;
  }

  h1 {
    font-weight: bold;
  }

  .button {
    border: 2px solid #4CAF50;
    border-radius: 8px;
    color: #000000;
    padding: 15px 32px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    font-family: Comic Sans MS, Comic Sans, cursive;
    margin: 4px;
    cursor: pointer;
  }

  .button:hover {
    background-color: #4CAF50;
    color: #ffffff;
    box-shadow: 0 6px 6px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19);
  }

  .dino {
    text-align: center;
  }

  #container2 {
    display: flex;
    justify-content: space-evenly;
    text-align: left;
  }

  #container3 {
    margin: 60px;
  }

</style>
