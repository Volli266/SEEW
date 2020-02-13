<template>
  <transition v-bind:name= "keyframeName" >
    <div v-show="show" v-bind:style="speechStyle" v-bind:id = "picClass" class="speech">
      <div v-bind:style="speechTextStyle"  class="speechText">
        {{text}}
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'SpeechBubble',
  /* Params, die dem Komponenten übergeben werden sollen */
  props: {
    /* erst wenn die Variable true wird, wird der Inhalt angezeigt */
    show: {
      type: Boolean,
      required: true,
      default: true
    },
    /* Inhalt der Sprechblase */
    text: {
      type: String,
      required: true,
      default: 'kein Text'
    },
    /* Bubble-'id', position des Blasenzeigers */
    /* Mögliche Werte: (siehe auch styleSheets als ID ) */
    /* bottomLeft, bottomRight, topLeft, topRight, rightTop, rightBottom, leftTop, leftBottom */
    bubblePointer: {
      type: String,
      required: true,
      default: 'bottomLeft'
    },
    /* ACHTUNG: Angabe in px als eine Ganzzahl (ohne px am Ende) */
    bubbleTop: {
      type: Number,
      required: true,
      default: 0
    },
    bubbleLeft: {
      type: Number,
      required: true,
      default: 0
    },
    /* Höhe des Elements (Breite wird automatisch angepasst (siehe CSS)) */
    bubbleWidth: {
      type: Number,
      required: true,
      default: 100
    },
    bubbleFontSize: {
      type: String,
      required: false,
      default: '20px'
    },
    bubbleFontFamily: {
      type: String,
      required: false,
      default: 'garmond'
    },
    bubbleTextColor: {
      type: String,
      required: false,
      default: 'black'
    }
  },
  data: function () {
    return {
      keyframeName: 'bottomRight',
      picClass: this.bubblePointer,
      picFactorW: 1.81,
      picFactorH: 1.25,
      factorPaddingW: 0.14,
      factorPaddingH: 0.2,
      speechStyle: {
        top: this.bubbleTop + 'px',
        left: this.bubbleLeft + 'px',
        width: this.bubbleWidth + 'px',
        height: '' /* in created:function berechnet */
      },
      speechTextStyle: {
        fontSize: this.bubbleFontSize,
        fontFamily: this.bubbleFontFamily,
        color: this.bubbleTextColor,
        paddingBottom: '4%',
        paddingLeft: '4%',
        paddingRight: '4%',
        paddingTop: '4%'
      }
    }
  },
  created: function () {
    /* Height anpassen */
    let bubbleHeight = 0
    if (this.bubblePointer === 'bottomLeft' || this.bubblePointer === 'topLeft' ||
        this.bubblePointer === 'bottomRight' || this.bubblePointer === 'topRight') {
      this.keyframeName = this.bubblePointer
      bubbleHeight = this.bubbleWidth / this.picFactorH
    } else {
      bubbleHeight = this.bubbleWidth / this.picFactorW
      if (this.bubblePointer === 'leftTop') { this.keyframeName = 'topLeft' }
      if (this.bubblePointer === 'leftBottom') { this.keyframeName = 'bottomLeft' }
      if (this.bubblePointer === 'rightTop') { this.keyframeName = 'topRight' }
      if (this.bubblePointer === 'rightBottom') { this.keyframeName = 'bottomRight' }
    }
    this.speechStyle.height = bubbleHeight + 'px'
    this.speechTextStyle.height = bubbleHeight + 'px'
    /* Padding & transformation anpassen */
    if (this.bubblePointer === 'bottomLeft' || this.bubblePointer === 'bottomRight') {
      this.speechTextStyle.paddingBottom = 100 * this.factorPaddingH + 4 + '%'
    }
    if (this.bubblePointer === 'topLeft' || this.bubblePointer === 'topRight') {
      this.speechTextStyle.paddingTop = 100 * this.factorPaddingH + 4 + '%'
    }
    if (this.bubblePointer === 'leftTop' || this.bubblePointer === 'leftBottom') {
      this.speechTextStyle.paddingLeft = 100 * this.factorPaddingW + 4 + '%'
    }
    if (this.bubblePointer === 'rightTop' || this.bubblePointer === 'rightBottom') {
      this.speechTextStyle.paddingRight = 100 * this.factorPaddingW + 4 + '%'
    }
  }
}

</script>
<style scoped>
  .speech {
    display: -moz-box;
    display: -ms-flexbox;
    display: -webkit-flex;
    display: flex;
    position: absolute;
    z-index: 10;
    top: 400px;
    left: 100px;
    margin: 0;

    background-repeat: no-repeat;
    -webkit-background-size: contain;
    -moz-background-size: contain;
    -o-background-size: contain;
    background-size: contain;
  }
  .speechText {
    min-height: 0;
    min-width: 0;
    flex: 0 1 auto;
    flex-grow: 0;
    flex-wrap: wrap;
    color: black;
    font-size: 20px;
    font-weight: lighter;
    font-style: normal;
    line-height: normal;
    padding-bottom: 3px;
    padding-left: 3px;
    padding-left: 3px;
    padding-right: 3px;
  }
  #bottomLeft{
    background-image: url("../assets/bubbles/bottom_left_bubble.svg");
  }
  #bottomRight{
    background-image: url("../assets/bubbles/bottom_right_bubble.svg");
  }
  #topLeft{
    background-image: url("../assets/bubbles/top_left_bubble.svg");
  }
  #topRight{
    background-image: url("../assets/bubbles/top_right_bubble.svg");
  }
  #leftBottom{
    background-image: url("../assets/bubbles/left_bottom_bubble.svg");
  }
  #rightBottom{
    background-image: url("../assets/bubbles/right_bottom_bubble.svg");
  }
  #leftTop{
    background-image: url("../assets/bubbles/left_top_bubble.svg");
  }
  #rightTop{
    background-image: url("../assets/bubbles/right_top_bubble.svg");
  }

  /* animation */
  @keyframes bounce{
    0%{transform-origin: center; transform: scale(0);}
    80%{transform: scale(1.1);}
    100%{transform: scale(1.0);}
  }
  .bounce-enter-active {
    animation: bounce 1.2s;
  }
  .bounce-leave-active {
    animation: bounce 0.7s reverse;
  }
  @keyframes bottomLeft {
    0%{transform-origin: left bottom; transform: scale(0);}
    80%{transform: scale(1.1);}
    100%{transform: scale(1.0);}
  }
  @keyframes bottomRight {
    0%{transform-origin: right bottom; transform: scale(0);}
    80%{transform: scale(1.1);}
    100%{transform: scale(1.0);}
  }
  @keyframes topRight{
    0%{transform-origin: right top; transform: scale(0);}
    80%{transform: scale(1.1);}
    100%{transform: scale(1.0);}
  }

  @keyframes topLeft {
    0%{transform-origin: left top; transform: scale(0);}
    80%{transform: scale(1.1);}
    100%{transform: scale(1.0);}
  }
  .topLeft-enter-active{
    animation: topLeft 1.2s;
  }
  .topLeft-leave-active {
    animation: topLeft 0.7s reverse;
  }
  .topRight-enter-active{
    animation: topRight 1.2s;
  }
  .topRight-leave-active {
    animation: topRight 0.7s reverse;
  }
  .bottomLeft-enter-active{
    animation: bottomLeft 1.2s;
  }
  .bottomLeft-leave-active {
    animation: bottomLeft  0.7s reverse;
  }
  .bottomRight-enter-active{
    animation: bottomRight 1.2s;
  }
  .bottomRight-leave-active {
    animation: bottomRight 0.7s reverse;
  }

</style>
