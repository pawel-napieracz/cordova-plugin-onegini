<template>
  <div class="mask">
    <div class="wrapper">
      <div class="container">
        <div class="header">
          <h4>Login with your fingerprint</h4>
          <div>
            <div class="body">
              <p>{{status}}</p>
            </div>
            <div class="footer">
              <button-lg text="Use PIN instead" @click="fallbackToPin" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import ButtonLarge from '../components/Button-large.vue';

  export default {
    props: [
      'status',
      'actions'
    ],

    methods: {
      fallbackToPin: function () {
        if (this.actions) {
          this.actions.fallbackToPin();
        }
      }
    },

    components: {
      'button-lg': ButtonLarge
    }
  }
</script>

<style scoped>
  .mask {
    display: flex;
    align-items: center;
    justify-content: center;

    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;

    background-color: rgba(0, 0, 0, .1);
  }

  .wrapper {
    display: flex;
    width: 90%;
    background-color: #fff;
    box-shadow: 0 0 10px #999;
  }

  .body {
    padding: 1.5em 0;
  }

  .container {
    width: 100%;
    display: border-box;
    padding: 1em;
  }

  .footer {
    font-size: .5em;
  }

</style>
