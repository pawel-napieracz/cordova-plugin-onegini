<template>
  <div class="mask" v-if="request">
    <div class="wrapper">
      <div class="container">
        <div class="header">
          <h4>Mobile Authentication Request</h4>
          <h4>For {{request.profileId}}</h4>
        <div>
        <div class="body">
          <p v-if="fingerprintStatus">{{fingerprintStatus}}</p>
          <p v-else>{{request.message}}</p>
          <div v-if="request.type === 'push_with_pin'">
            <input type="password" pattern="\d" v-model="pin" placeholder="Enter PIN" />
            <p>{{request.remainingFailureCount}} out of {{request.maxFailureCount}} attempts remaning</p>
          </div>
        </div>
        <div v-if="!fingerprintStatus" class="footer">
          <button-lg text="✓ Accept" @click="this.accept" />
          <button-lg text="✗ Deny" @click="this.deny" />
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
      pin: null,
      request: null,
      actions: null,
      fingerprintStatus: null
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
            navigator.notification.alert("You win!");
            this.complete();
          })
          .onError((err) => {
            navigator.notification.alert("Game over");
            this.complete();
          });

      onegini.mobileAuthentication.on('pin')
          .onPinRequest((actions, request) => {
            console.log('request', request);
            this.actions = actions;
            this.request = request;
          })
          .onSuccess(() => {
            navigator.notification.alert('PIN Entry success! You win');
            this.complete();
          })
          .onError((err) => {
            navigator.notification.alert('Game over');
            this.complete();
          });

      onegini.mobileAuthentication.on('fingerprint')
          .onFingerprintRequest((actions, request) => {
            this.actions = actions;
            this.request = request;
          })
          .onFingerprintCaptured(() => {
            this.fingerprintStatus = 'Verifying...';
          })
          .onFingerprintFailed(() => {
            this.fingerprintStatus = 'No match!';
          })
          .onSuccess(() => {
            navigator.notification.alert('Fingerprint authentication success! You win');
            this.complete();
          })
          .onError(() => {
            navigator.notification.alert('Game over');
            this.complete();
          });
    },

    accept: function() {
      if (this.request.type === 'push') {
        this.actions.accept();
      } else if (this.request.type === 'push_with_pin') {
        this.actions.accept({pin: this.pin});
        this.pin = null;
      } else if (this.request.type === 'push_with_fingerprint') {
        this.actions.accept();
        this.fingerprintStatus = 'Touch sensor to start';
      }
    },

    deny: function() {
      this.actions.deny();
    },

    complete: function() {
      this.pin = null;
      this.request = null;
      this.actions = null;
      this.fingerprintStatus = null;
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

  input {
    width: 80%;
  }

</style>