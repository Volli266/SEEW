<template>
  <!-- Hier kommt die gesamte Komponente rein -->
  <div id="wholeBox2">
    <!-- Überschrift -->
    <button id="title" class="btn" v-on:click="moveEl()">
      <h2>Gruppe 2</h2>
    </button>
    <!-- Boxen mit einzelnen Studenten -->
    <div class="listBox">
      <div tabindex="0" :id="index" class="allStudents" v-for="(item,index) in students" v-on:click="() => saveEl(item.username)">
        {{item.username}}
      </div>
    </div>
  </div>
</template>

<script>
  import {bus} from "../main";

  export default {
    name: 'group2',
    data () {
      return {
        groupNr: 2,
        tempUserGroup: '',
        activeStudent: '',
        students: [
        ],
      }
    },
    methods: {
      saveEl: function(user) {
        this.tempUserGroup = user;
        this.activeStudent = '';
        bus.$emit('newActiveGroup', this.tempUserGroup);
      },
      moveEl: function() {
        if (this.students.length < 4) {
          if (this.activeStudent !== '') {
            this.students.push(
              {
                username: this.activeStudent
              });
            bus.$emit('deleteStudent', this.activeStudent);
          }
          if (this.tempUserGroup !== '') {
            bus.$emit('deleteGroupUser', this.tempUserGroup);
            this.students.push(
              {
                username: this.tempUserGroup
              });
          }
          bus.$emit('forgetAll', this.activeStudent);
        }
      }
    },
    created() {
      bus.$on('newActiveStudent', (data) => {
        this.tempUserGroup = '';
        this.activeStudent = data;
      });
      bus.$on('newActiveGroup', (data) => {
        this.activeStudent = '';
        this.tempUserGroup = data;
      });
      bus.$on('deleteGroupUser', (data) => {
        this.students = this.students.filter((item) => {
          return item.username !== data;
        })
      });
      bus.$on('deleteStudent', (data) => {
        this.activeStudent = '';
      });
      bus.$on('forgetAll', (data) => {
        this.activeStudent = '';
        this.tempUserGroup = '';
      });
      bus.$on('showGroup2', (data) => {
        if (data === 'hidden') {
          document.getElementById('wholeBox2').style.visibility = "visible";
          document.getElementById("wholeBox2").style.zIndex = "1";
        }
      });
      bus.$on('pushRandom', (data) => {
        if(this.groupNr === data) {
          this.moveEl();
        }
      });
      bus.$on('isGroupEmpty', () => {
        bus.$emit('groupLength', this.students.length);
      });
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  #wholeBox2 {
    visibility: hidden;
    display: flex;
    flex-direction: column;
    z-index: -1;
    width: 89%;
    min-width: 130px;
    min-height: 211px;
    max-height: 211px;
    border-style: solid;
    border-radius: 5px;
    text-align: center;
    margin-bottom: 6%;
  }
  .listBox {
    display: flex;
    flex-direction: column;
    max-height: 219px;
    margin-top: 0px;
    margin-bottom: 0px;
    text-align: center;
    overflow-y: scroll;
  }

  #title {
    background: linear-gradient(white, crimson);
    padding-top: 1px;
    border-bottom-style: solid;
    border-radius: 10px;
  }
  #title:active{
    background: green;
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
  .allStudents:focus
  {
    outline:none;
    box-shadow:0 0 0 2px #68b, inset 0 0 0 1px #ddd;
  }

</style>
