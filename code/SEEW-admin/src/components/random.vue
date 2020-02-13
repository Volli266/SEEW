<template>
  <div id="divRandom">
    <button id="btnRandom" v-on:click="genRandom()">
      Random
    </button>
  </div>
</template>

<script>
  import {bus} from "../main";

  export default {
    name: "random",
    data () {
      return {
        randomAllowed: true,
        studentsLength: 0,
        randomIndex: 0,
        decrStudentsLength: 1
      }
    },
    methods: {
      genRandom: function() {
        bus.$emit('isGroupEmpty');
        if (this.randomAllowed === true) {
          bus.$emit('getStudentsLength');
          //wenn zu wenige User uns random somit sinnlos
          if (this.studentsLength <= 5) {
            alert('Nicht genügend User für Random!')
          }
          if (this.studentsLength <= 16 && this.studentsLength > 5) {
            for (let i = 0; i < this.studentsLength; i++) {
              for (let x = 4; x >= 1; x--) {
                //x = Anzahl der zu öffnenden Gruppen
                if (this.studentsLength / x >= 3 && this.studentsLength / x <= 4) {
                  //passende Gruppen öffnen
                  bus.$emit('enableGroup', x);
                  this.randomIndex = Math.floor((Math.random() * ((this.studentsLength + 1) - this.decrStudentsLength)));
                  //Unterscheidung nach Durchschnitt an Usern pro geöffneter Gruppe
                  if (this.studentsLength / x === 4) {
                    //4 User pro Gruppe
                    //Danach wird bus emitted mit ermitteltem Index
                    bus.$emit('randomStudentsAvg4', this.randomIndex);
                    //Username in students mit dem Index wird in erste Gruppe geschrieben
                    this.decrStudentsLength++;
                    break;
                  } else if (this.studentsLength / x <= 3.5) {
                    //3.0 bis 3.5 User pro Gruppe
                    if (this.studentsLength === 7) {
                      //7 User
                      //Danach wird bus emitted mit ermitteltem Index
                      bus.$emit('randomStudents7', this.randomIndex);
                      //Username in students mit dem Index wird in erste Gruppe geschrieben
                      this.decrStudentsLength++;
                      break;
                    } else if (this.studentsLength === 10) {
                      //10 User
                      //Danach wird bus emitted mit ermitteltem Index
                      bus.$emit('randomStudents10', this.randomIndex);
                      //Username in students mit dem Index wird in erste Gruppe geschrieben
                      this.decrStudentsLength++;
                      break;
                    } else if (this.studentsLength === 14) {
                      //14 User
                      //Danach wird bus emitted mit ermitteltem Index
                      bus.$emit('randomStudents14', this.randomIndex);
                      //Username in students mit dem Index wird in erste Gruppe geschrieben
                      this.decrStudentsLength++;
                      break;
                    } else {
                      //Danach wird bus emitted mit ermitteltem Index
                      bus.$emit('randomStudentsAvg3', this.randomIndex);
                      //Username in students mit dem Index wird in erste Gruppe geschrieben
                      this.decrStudentsLength++;
                      break;
                    }
                  } else if (this.studentsLength === 11) {
                    //11 User
                    //Danach wird bus emitted mit ermitteltem Index
                    bus.$emit('randomStudents11', this.randomIndex);
                    //Username in students mit dem Index wird in erste Gruppe geschrieben
                    this.decrStudentsLength++;
                    break;
                  } else if (this.studentsLength === 15) {
                    //15 User
                    //Danach wird bus emitted mit ermitteltem Index
                    bus.$emit('randomStudents15', this.randomIndex);
                    //Username in students mit dem Index wird in erste Gruppe geschrieben
                    this.decrStudentsLength++;
                    break;
                  }
                }
              }
            }
            this.decrStudentsLength = 0;
          }
          //Wenn mehr als 16 User im Netzwerk sind
          if (this.studentsLength > 16) {
            for (let i = 0; i <= 16; i++) {
              //4 Gruppen öffnen
              bus.$emit('enableGroup', 4);
              //Jetzt kommt der Random-Generator
              this.randomIndex = Math.floor((Math.random() * (17 - this.decrStudentsLength)));
              //Danach wird bus emitted mit ermitteltem Index
              bus.$emit('randomStudentsAvg4', this.randomIndex);
              //Username in students mit dem Index wird in erste Gruppe geschrieben
              this.decrStudentsLength++;
            }
          }
        }
        else {
          alert('Alle Gruppen müssen leer sein!')
        }
      }
    },
    created() {
      bus.$on('StudentsLength', (data) => {
        this.studentsLength = data;
      });
      bus.$on('groupLength', (data) => {
        if(data > 0) {
          this.randomAllowed = false;
        }
      });
    }
  }
</script>

<style scoped>
  #divRandom {
    display: flex;
    flex-direction: row;
    margin-top: -4.3%;
    margin-left: 88%;
    width: 9vw;
    height: 3vw;
  }

  #btnRandom {
    width: 9vw;
    height: 3vw;
    font-weight: bold;
    font-size: 15px;
    border-radius: 5px;
    border-style: solid;
    border-color: black;
    background-color: white;
  }

  #btnRandom:hover {
    outline: none;
    box-shadow:0 0 0 1px darkblue;
  }

</style>
