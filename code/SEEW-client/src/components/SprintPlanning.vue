<template>
    <div>
      <h1>Sprint-Planning</h1>
        <div class="rectangle1"></div>
        <div class="rectangle2"></div>
        <div class="rectangle3"></div>
      <div class="btn-group">
        <div class ="upgrades">
          <button>Schwanz Upgrade</button>
          <button>Schwanz Upgrade</button>
          <button>Schwanz Upgrade</button>
          <button>Schwanz Upgrade</button>
          <button>Schwanz Upgrade</button>
          <button>Schwanz Upgrade</button>
          <button>Schwanz Upgrade</button>
        </div>
        <div class="highlevel">
          <button>High Level Ziel 1</button>
          <button>High Level Ziel 2</button>
        </div>
        <div class="tickets">
          <button>Ticket 1</button>
          <button>Ticket 2</button>
          <button>Ticket 3</button>
          <button>Ticket 4</button>
        </div>
      </div>
      <img src="../assets/dino.png" alt="https://images.vexels.com/media/users/3/153116/isolated/preview/8863d3b6a7dd48a27428224cb7780747-cute-dino-character-cartoon-by-vexels.png">
      <br><button v-on:click="goOn" class="button">Weiter</button>
    </div>
</template>

<script>
export default {
  name: 'SprintPlanning',
  data () {
    return {
    }
  },
  methods: {
    // Informationen für die Prozess-Zwischensicht sichtbar im store
    goOn: function () {
      /*   this.$store.state.currentPhase = 'SprintPlanning'      Auskommentiert aufgrund des unfertigen Zustands der Zwischensicht
      this.$store.state.nextPhase = 'Sprint'
      this.$router.push('Prozess-Zwischensicht')  */
      this.$router.push('SprintVerlauf')
    }
  }
}
</script>
<style scoped>
  .btn-group button {
    background-color: #4CAF50; /* Green background */
    display: block; /* Make the buttons appear below each other */
    border: 1px solid green; /* Green border */
    border-radius: 12px;
    color: white; /* White text */
    padding: 20px 20px; /* Some padding */
     width: 150px;
    font-size: 16px;
    cursor: pointer; /* Pointer/hand icon */
  }

  .btn-group button:not(:last-child) {
    border-bottom: none; /* Prevent double borders */
  }

  /* Add a background color on hover */
  .btn-group button:hover {
    background-color: #3e8e41;
    box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
  }
  .btn-group .upgrades {
    position:absolute;
    transition: .5s ease;
    top: 5%;
    left: 5%;
  }
  .btn-group .highlevel {
    position:absolute;
    transition: .5s ease;
    top: 5%;
    right: 5%;
  }
  .btn-group .tickets {
    position:absolute;
    transition: .5s ease;
    top: 50%;
    right: 5%;
  }
  .rectangle1 {
    position:absolute;
    transition: .5s ease;
    top: 50%;
    right: 50%;
    width: 20px;
    height: 20px;
    padding: 10px;
    border: 1px solid #aaaaaa;
  }

  .rectangle2 {
    position:absolute;
    transition: .5s ease;
    top: 22%;
    right: 55%;
    width: 20px;
    height: 20px;
    padding: 10px;
    border: 1px solid #aaaaaa;
  }

  .rectangle3 {
    position:absolute;
    transition: .5s ease;
    top: 50%;
    right: 40%;
    width: 20px;
    height: 20px;
    padding: 10px;
    border: 1px solid #aaaaaa;
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
