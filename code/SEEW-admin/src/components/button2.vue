<template>
    <button id="btn2" v-on:click="show()">
      +
    </button>
</template>

<script>
  import {bus} from "../main";

  export default {
    name: 'button2',
    data () {
      return {
        visibilityStatus: 'shown',
        btnNr: 2,
      }
    },
    methods: {
      show: function () {
        document.getElementById("btn2").style.visibility = "hidden";
        document.getElementById("btn2").style.zIndex = "-1";
        this.visibilityStatus = 'hidden';
        bus.$emit('showGroup2', this.visibilityStatus);
      }
    },
    created() {
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
    width: 5vw;
    height: 5vw;
    border-radius: 50%;
    border-style: solid;
    border-color: black;
    background-color: white;
    font-size: 30px;
  }
  button:hover {
    outline: none;
    box-shadow:0 0 0 2px #68b, inset 0 0 0 1px #ddd;
  }
  #btn2 {
    z-index: 1;
  }
</style>
