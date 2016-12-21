<template>
  <div class="mask" v-if="request">
    <div class="wrapper">
      <div class="container">
        <div class="header">
          <h4>Mobile Authentication Request</h4>
          <h4>For {{request.profileId}}</h4>
        <div>
        <div class="body">
          <p>{{request.message}}</p>
        </div>
        <div class="footer">
          <button-lg text="✓ Accept" @click="actions.accept"/>
          <button-lg text="✗ Deny" @click="actions.deny" />
        </div>    
      </div>
    </div>
  </div>
</template>

<script>
import ButtonLarge from '../components/Button-large.vue';

export default {
  data () {
    return {
      request: null,
      actions: null
    }
  },

  created: function() {
    document.addEventListener('deviceready', this.listenForEvents);
  },

  methods: {
    listenForEvents: function() {
      onegini.mobileAuthentication.on('confirmation')
          .onConfirmationRequest((actions, request) => {
            this.request = request;
            this.actions = actions;
          })
          .onSuccess(() => {
            navigator.notification.alert("Yo win!");
            this.actions = null;
            this.request = null;
          })
          .onError((err) => {
            navigator.notification.alert("Game over");
            this.actions = null;
            this.request = null;
          });
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

    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    
    background-color: rgba(0, 0, 0, .1);
  }

  h4 {
    margin-bottom: 0;
  }

  h4:last-of-type {
    margin-top: .1em;
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