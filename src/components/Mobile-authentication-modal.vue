<template>
  <div class="wrapper" v-if="request">
    <div>
      <h3>Mobile Authentication Request</h3>
      <h3>For {{request.profileId}}</h3>
    </div>
    <div class="body">
      <p v-if="fingerprintStatus">{{fingerprintStatus}}</p>
      <p v-else>{{request.message}}</p>
      <div v-if="request.type === 'push_with_pin'">
        <input type="password" pattern="[0-9]*" inputmode="numeric" v-model="pin" placeholder="Enter PIN" />
        <p>{{request.remainingFailureCount}} out of {{request.maxFailureCount}} attempts remaning</p>
      </div>
    </div>
    <div v-if="!fingerprintStatus">
      <button-lg text="✓ Accept" @click="this.accept" />
      <button-lg text="✗ Deny" @click="this.deny" />
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
  .wrapper {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-direction: column;
    box-sizing: border-box;

    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    padding: 1em;
    
    background-color: #fff;
    box-shadow: 0 0 1em rgba(0, 0, 0, .7);

    animation: slide .3s ease;
    animation-iteration-count: 1;
  }

  .wrapper div {
    width: 100%;
  }

  .body {
    box-sizing: border-box;
    margin: 3em 0;
    padding: 0 1em;
  }

  h3 {
    margin-bottom: 0;
  }

  h3:last-of-type {
    margin-top: .1em;
    font-weight: normal;
  }

  input {
    width: 80%;
  }

  @keyframes slide {
    from {
      top: 100%;
    }

    to {
      top: 0;
    }
  }
</style>