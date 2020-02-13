<template>
    <div class="container">
      <img class="policeofficer" src="../assets/police2.png" alt="Polizist">
      <button v-on:click="wechseln" v-if="notCompleted">Rede mit mir</button>
      <SpeechBubble :show="showdialog1"
                    text="Super, ihr habt den Sprint hinter euch. Nun ist es an der Zeit eure Arbeit zu reflektieren. Um dies effektiv tun zu können müsst ihr euch nun zusammen unterhalten. Nachfolgend ein paar Techniken."
                    bubble-pointer="bottomLeft" v-bind:bubble-top="50"
                    v-bind:bubble-width="350" v-bind:bubble-left="680"></SpeechBubble>

      <SpeechBubble :show="showdialog2"
                    text="Bewertet den letzten Sprint wie eine Amazon-Kundenrezension. Jedes Teammitglied verfasst eine kurze Rezension mit Titel, Inhalt und Sterne Bewertung (1-5). Dann liest jeder seine Rezension vor. Sammelt die Sternebewertungen. Beantwortet ihr die Frage welche Empfehlungen es für den nächsten Sprint gibt."
                    bubble-pointer="bottomLeft" v-bind:bubble-top="50"
                    v-bind:bubble-width="350" v-bind:bubble-left="680"></SpeechBubble>

      <SpeechBubble :show="showdialog3"
                    text="Die Teilnehmer notieren ihre Temperatur (Stimmung) auf einem Flipchart. Male ein Thermometer.
                          Jeder einzelne im Team markiert jetzt ein Stelle auf der Temperaturskala, die seiner
                          aktuellen Stimmung entspricht und erklärt warum."
                    bubble-pointer="bottomLeft" v-bind:bubble-top="50"
                    v-bind:bubble-width="350" v-bind:bubble-left="680"></SpeechBubble>
      <SpeechBubble :show="showdialog4"
                    text="Klebe aus Malerkrepp eine Skala auf den Boden. Markiere das eine Ende mit 'Großartig' und das andere mit 'Mies'. Bitte die Teilnehmer sich an die
                          Stelle der Skala zu stellen, die ihrer Zufriedenheit mit dem letzten Sprint entspricht."
                    bubble-pointer="bottomLeft" v-bind:bubble-top="50"
                    v-bind:bubble-width="350" v-bind:bubble-left="680"></SpeechBubble>
  </div>
</template>

<script>
import SpeechBubble from './SpeechBubble'
export default {
  name: 'Retro',
  components: {SpeechBubble},
  data () {
    return {
      i: 0,
      showdialog1: false,
      showdialog2: false,
      showdialog3: false,
      showdialog4: false,
      notCompleted: true
    }
  },
  methods: {
    back: function () {
      this.$emit('changeView', 'Between')
    },
    wechseln: function () {
      this.i = this.i + 1
      /* OK 1 Mal angeklickt -> wechsel der Sprechblase */
      if (this.i === 1) {
        this.showdialog1 = true
        this.showdialog2 = false
        this.showdialog3 = false
      }
      /* OK 2 Mal angeklickt -> wechsel der Sprechblase */
      if (this.i === 2) {
        this.showdialog1 = false
        this.showdialog2 = true
        this.showdialog3 = false
      }
      /* OK 1 Mal angeklickt -> alles auf der Seite wird verborgen */
      if (this.i === 3) {
        this.showdialog1 = false
        this.showdialog2 = false
        this.showdialog3 = true
      }
      if (this.i === 4) {
        this.showdialog1 = false
        this.showdialog2 = false
        this.showdialog3 = false
        this.showdialog4 = true
      }

      if (this.i === 5) {
        this.showdialog1 = false
        this.showdialog2 = false
        this.showdialog3 = false
        this.showdialog4 = false
        this.notCompleted = false
      }
    }
  }
}
</script>

<style scoped>
@import "../stylesheet.css";

.container {
  background-image: url("../assets/office.jpg");
  background-size: 100% auto;
  height: 600px;
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}
.policeofficer{
    width: 20%;
    height: auto;
}
</style>
