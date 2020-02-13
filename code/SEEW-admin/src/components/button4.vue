<template>
  <button disabled id="btn4" v-on:click="show()">
    +
  </button>
</template>

<script>
  import {bus} from "../main";

  export default {
    name: 'button4',
    data () {
      return {
        visibilityStatus: 'shown',
        btnNr: 4
      }
    },
    methods: {
      show: function () {
        document.getElementById("btn4").style.visibility = "hidden";
        this.visibilityStatus = 'hidden';
        bus.$emit('showGroup4', this.visibilityStatus);
      }
    },
    created() {
      bus.$on('showGroup3', (data) => {
        document.getElementById("btn4").disabled = false;
      });
      bus.$on('enableGroup', (data) => {
        if(this.btnNr <= data) {
          this.show();
        }
      });
    }
  }
</script>

<style scoped>
  button {
    height: 5vw;
    border-radius: 50%;
    border-style: solid;
    border-color: black;
    background-color: white;
    font-size: 30px;
  }
  button:disabled,
  button[disabled]{
    opacity: 50%;
    border-color: #ddd;
  }
  button:hover:enabled {
    outline: none;
    box-shadow:0 0 0 2px #68b, inset 0 0 0 1px #ddd;
  }
</style>
