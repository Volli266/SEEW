<template>
  <div class="DevelopView">
    <v-toolbar height="80" card color="green" dark>
    <v-toolbar-title style="position: center">DEINE ENTWICKLER:</v-toolbar-title>
  </v-toolbar>
    <div class="flex-container">
      <section class="flex-item">
        <div class="developone">
          <br>
          <h2>{{ forenameOne }} {{ surnameOne }}</h2>
          <br><br>
          <h3>Trainiert den Dino in:</h3>
          <br>
          <table>
            <tr>
              <td>Soziale Fähigkeiten:</td>
              <td>{{ socialOne }}</td>
              <td><canvas class="rect" id="rect11" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Logik:</td>
              <td>{{ logicOne }}</td>
              <td><canvas class="rect" id="rect21" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Mut:</td>
              <td>{{ courageOne }}</td>
              <td><canvas class="rect" id="rect31" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Reaktionszeit:</td>
              <td>{{ reactionOne }}</td>
              <td><canvas class="rect" id="rect41" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Beweglichkeit:</td>
              <td>{{ agilityOne }}</td>
              <td><canvas class="rect" id="rect51" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Geschicklichkeit:</td>
              <td>{{ dexterityOne }}</td>
              <td><canvas class="rect" id="rect61" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Stärke:</td>
              <td>{{ strengthOne }}</td>
              <td><canvas class="rect" id="rect71" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Gewicht:</td>
              <td>{{ weightOne }}</td>
              <td><canvas class="rect" id="rect81" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Größe:</td>
              <td>{{ sizeOne }}</td>
              <td><canvas class="rect" id="rect91" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Extremitäten:</td>
              <td>{{ bodyOne }}</td>
              <td><canvas class="rect" id="rect101" width="70" height="17"></canvas></td>
            </tr>
          </table>
        </div>
      </section>
      <section class="flex-item">
        <div class="developtwo">
          <br>
          <h2>{{ forenameTwo }} {{ surnameTwo }}</h2>
          <br><br>
          <h3>Trainiert den Dino in:</h3>
          <br>
          <table>
            <tr>
              <td>Soziale Fähigkeiten:</td>
              <td>{{ socialTwo }}</td>
              <td><canvas class="rect" id="rect12" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Logik:</td>
              <td>{{ logicTwo }}</td>
              <td><canvas class="rect" id="rect22" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Mut:</td>
              <td>{{ courageTwo }}</td>
              <td><canvas class="rect" id="rect32" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Reaktionszeit:</td>
              <td>{{ reactionTwo }}</td>
              <td><canvas class="rect" id="rect42" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Beweglichkeit:</td>
              <td>{{ agilityTwo }}</td>
              <td><canvas class="rect" id="rect52" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Geschicklichkeit:</td>
              <td>{{ dexterityTwo }}</td>
              <td><canvas class="rect" id="rect62" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Stärke:</td>
              <td>{{ strengthTwo }}</td>
              <td><canvas class="rect" id="rect72" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Gewicht:</td>
              <td>{{ weightTwo }}</td>
              <td><canvas class="rect" id="rect82" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Größe:</td>
              <td>{{ sizeTwo }}</td>
              <td><canvas class="rect" id="rect92" width="70" height="17"></canvas></td>
            </tr>
            <tr>
              <td>Extremitäten:</td>
              <td>{{ bodyTwo }}</td>
              <td><canvas class="rect" id="rect102" width="70" height="17"></canvas></td>
            </tr>
          </table>
          <br>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import {global} from '../main.js'

export default {
  name: 'SEEW-Client',
  created: function () {
    global.rpc.register('updateDevelopers', this.updateDevelopers)
    global.rpc.call('requestUpdateDevelopers', null)
  },
  data () {
    return {
      forenameOne: '',
      surnameOne: '',
      socialOne: 0,
      logicOne: 0,
      courageOne: 0,
      reactionOne: 0,
      agilityOne: 0,
      dexterityOne: 0,
      strengthOne: 0,
      weightOne: 0,
      sizeOne: 0,
      bodyOne: 0,
      forenameTwo: '',
      surnameTwo: '',
      socialTwo: 0,
      logicTwo: 0,
      courageTwo: 0,
      reactionTwo: 0,
      agilityTwo: 0,
      dexterityTwo: 0,
      strengthTwo: 0,
      weightTwo: 0,
      sizeTwo: 0,
      bodyTwo: 0
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
    // Überträgt Daten der JSON auf Variablen und füllt Canvas
    updateDevelopers: function (data) {
      this.forenameOne = data.developerOne.developerForename
      this.surnameOne = data.developerOne.developerSurname
      this.socialOne = data.developerOne.developerSocial
      this.logicOne = data.developerOne.developerLogic
      this.courageOne = data.developerOne.developerCourage
      this.reactionOne = data.developerOne.developerReaction
      this.agilityOne = data.developerOne.developerAgility
      this.dexterityOne = data.developerOne.developerDexterity
      this.strengthOne = data.developerOne.developerStrength
      this.weightOne = data.developerOne.developerWeight
      this.sizeOne = data.developerOne.developerSize
      this.bodyOne = data.developerOne.developerBody
      this.fill('rect11', this.socialOne)
      this.fill('rect21', this.logicOne)
      this.fill('rect31', this.courageOne)
      this.fill('rect41', this.reactionOne)
      this.fill('rect51', this.agilityOne)
      this.fill('rect61', this.dexterityOne)
      this.fill('rect71', this.strengthOne)
      this.fill('rect81', this.weightOne)
      this.fill('rect91', this.sizeOne)
      this.fill('rect101', this.bodyOne)
      this.forenameTwo = data.developerTwo.developerForename
      this.surnameTwo = data.developerTwo.developerSurname
      this.socialTwo = data.developerTwo.developerSocial
      this.logicTwo = data.developerTwo.developerLogic
      this.courageTwo = data.developerTwo.developerCourage
      this.reactionTwo = data.developerTwo.developerReaction
      this.agilityTwo = data.developerTwo.developerAgility
      this.dexterityTwo = data.developerTwo.developerDexterity
      this.strengthTwo = data.developerTwo.developerStrength
      this.weightTwo = data.developerTwo.developerWeight
      this.sizeTwo = data.developerTwo.developerSize
      this.bodyTwo = data.developerTwo.developerBody
      this.fill('rect12', this.socialTwo)
      this.fill('rect22', this.logicTwo)
      this.fill('rect32', this.courageTwo)
      this.fill('rect42', this.reactionTwo)
      this.fill('rect52', this.agilityTwo)
      this.fill('rect62', this.dexterityTwo)
      this.fill('rect72', this.strengthTwo)
      this.fill('rect82', this.weightTwo)
      this.fill('rect92', this.sizeTwo)
      this.fill('rect102', this.bodyTwo)
    }
  }
}
</script>

<style scoped>

  td {
    padding: 5px;
  }

  .rect {
    border-radius: 8px;
    transition: .5s ease;
    width: 70px;
    height: 17px;
    border: 1px solid black;
    text-align: right;
  }

  .headline {
    padding: 30px;
  }

  .flex-container {
    display: flex;
    justify-content: space-evenly;
    font-size: 20px;
  }

  .flex-item {
    flex: 1;
    margin: 5px;
    padding: 5px;
    position: center;
  }

  h1 {
    font-weight: bold;
  }
  table{
    margin-left:100px;
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
</style>
