<template>
  <!-- Hier kommt die gesamte Komponente rein -->
  <div class="wholeBox">
    <!-- Überschrift -->
    <button id="title" class="btn" v-on:click="moveEl()">
      <h2>Studenten</h2>
    </button>
    <!-- Boxen mit einzelnen Studenten -->
    <div class="listBox">
      <div tabindex="0" :id="index" class="allStudents" v-for="(item, index) in students" v-on:click="() => saveEl(item.username)">
        {{item.username}}
      </div>
    </div>
  </div>
</template>

<script>
  import {bus} from "../main";

  export default {
    name: 'HelloWorld',
    data () {
      return {
        groupDefiner: 0,
        tempUserStudent: '',
        activeGroup: '',
        students: [
          {
            username: 'User1'
          },
          {
            username: 'User2'
          },
          {
            username: 'User3'
          },
          {
            username: 'User4'
          },
          {
            username: 'User5'
          },
          {
            username: 'User6'
          },
          {
            username: 'User7'
          },
          {
            username: 'User8'
          },
          {
            username: 'User9'
          },
          {
            username: 'User10'
          },
          {
            username: 'User11'
          },
          {
            username: 'User12'
          },
          {
            username: 'User13'
          },
          {
            username: 'User14'
          },
          {
            username: 'User15'
          },
        ],
      }
    },
    methods: {
      saveEl: function(user){
        this.tempUserStudent = user;
        this.activeGroup = '';
        bus.$emit('newActiveStudent', this.tempUserStudent);
      },
      moveEl: function() {
        if (this.activeGroup !== '') {
          this.students.push(
            {
              username: this.activeGroup
            });
          bus.$emit('deleteGroupUser', this.activeGroup);
        }
        this.activeGroup = '';
      }
    },
    created() {
      bus.$on('newActiveGroup', (data) => {
        this.tempUserStudent = '';
        this.activeGroup = data;
      });
      bus.$on('deleteStudent', (data) => {
        this.students = this.students.filter((item) => {
          return item.username !== data;
        })
      });
  //Ab hier nur noch Sachen für die Random-Funktion
      bus.$on('getStudentsLength', () => {
        bus.$emit('StudentsLength', this.students.length);
      });
      //Wenn 4 pro Gruppe sind
      bus.$on('randomStudentsAvg4', (data) => {
        this.tempUserStudent = this.students[data].username;
        bus.$emit('newActiveStudent', this.tempUserStudent);
        if(this.groupDefiner <= 3) {
          bus.$emit('pushRandom', 1);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 3 && this.groupDefiner <= 8) {
          bus.$emit('pushRandom', 2);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 8 && this.groupDefiner <= 13) {
          bus.$emit('pushRandom', 3);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 13) {
          bus.$emit('pushRandom', 4);
          this.groupDefiner++;
        }
      });
      //Wenn 3 pro Gruppe sind
      bus.$on('randomStudentsAvg3', (data) => {
        this.tempUserStudent = this.students[data].username;
        bus.$emit('newActiveStudent', this.tempUserStudent);
        if(this.groupDefiner <= 2) {
          bus.$emit('pushRandom', 1);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 2 && this.groupDefiner <= 6) {
          bus.$emit('pushRandom', 2);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 6 && this.groupDefiner <= 10) {
          bus.$emit('pushRandom', 3);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 10) {
          bus.$emit('pushRandom', 4);
          this.groupDefiner++;
        }
      });
      //Wenn es 7 User gibt
      bus.$on('randomStudents7', (data) => {
        this.tempUserStudent = this.students[data].username;
        bus.$emit('newActiveStudent', this.tempUserStudent);
        if(this.groupDefiner <= 2) {
          bus.$emit('pushRandom', 1);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 2) {
          bus.$emit('pushRandom', 2);
          this.groupDefiner++;
        }
      });
      //Wenn es 10 User gibt
      bus.$on('randomStudents10', (data) => {
        this.tempUserStudent = this.students[data].username;
        bus.$emit('newActiveStudent', this.tempUserStudent);
        if(this.groupDefiner <= 2) {
          bus.$emit('pushRandom', 1);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 2 && this.groupDefiner <= 6) {
          bus.$emit('pushRandom', 2);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 6 ) {
          bus.$emit('pushRandom', 3);
          this.groupDefiner++;
        }
      });
      //Wenn es 11 User gibt
      bus.$on('randomStudents11', (data) => {
        this.tempUserStudent = this.students[data].username;
        bus.$emit('newActiveStudent', this.tempUserStudent);
        if(this.groupDefiner <= 2) {
          bus.$emit('pushRandom', 1);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 2 && this.groupDefiner <= 7) {
          bus.$emit('pushRandom', 2);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 7 ) {
          bus.$emit('pushRandom', 3);
          this.groupDefiner++;
        }
      });
      //Wenn es 14 User gibt
      bus.$on('randomStudents14', (data) => {
        this.tempUserStudent = this.students[data].username;
        bus.$emit('newActiveStudent', this.tempUserStudent);
        if(this.groupDefiner <= 2) {
          bus.$emit('pushRandom', 1);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 2 && this.groupDefiner <= 6) {
          bus.$emit('pushRandom', 2);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 6 && this.groupDefiner <= 11) {
          bus.$emit('pushRandom', 3);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 11) {
          bus.$emit('pushRandom', 4);
          this.groupDefiner++;
        }
      });
      //Wenn es 15 User gibt
      bus.$on('randomStudents15', (data) => {
        this.tempUserStudent = this.students[data].username;
        bus.$emit('newActiveStudent', this.tempUserStudent);
        if(this.groupDefiner <= 2) {
          bus.$emit('pushRandom', 1);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 2 && this.groupDefiner <= 7) {
          bus.$emit('pushRandom', 2);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 7 && this.groupDefiner <= 12) {
          bus.$emit('pushRandom', 3);
          this.groupDefiner++;
        }
        if(this.groupDefiner > 12) {
          bus.$emit('pushRandom', 4);
          this.groupDefiner++;
        }
      });
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .wholeBox {
    display: flex;
    flex-direction: column;
    width: 20%;
    min-width: 130px;
    min-height: 423px;
    max-height: 423px;
    margin-left: 5%;
    margin-top: 3%;
    border-style: solid;
    border-radius: 5px;
    text-align: center;
  }
  .listBox {
    display: flex;
    flex-direction: column;
    max-height: 364px;
    margin-top: 0px;
    margin-bottom: 0px;
    text-align: center;
    overflow-y: scroll;
  }
  #title {
    background-image: linear-gradient(white, grey);
    padding-top: 1px;
    border-bottom-style: solid;
    border-radius: 10px;
  }
  h2 {
    color: darkblue;
  }
  .allStudents {
    display: block;
    list-style-type: none;
    border-bottom-style: solid;
    border-radius: 5px;
    padding: 10px;
    line-height: 0.7;
    font-size: 22px;
    font-family: Arial, Helvetica, sans-serif;
  }
  .allStudents:focus {
    background-color: green;
  }
  .allStudents:hover {
    outline: none;
    box-shadow:0 0 0 1px darkblue;
  }
  ::-webkit-scrollbar {
    display: none;
  }

</style>
