<template>
  <div>
    <h1>Settings</h1>
    <button-lg text="Enable Mobile Authentication" @click="enrollForMobileAuthentication" />
    <button-lg text="Change PIN" @click="changePin" />
    <h3>Authenticators</h3>
    <ul>
      <li v-for="authenticator in authenticators">
        <div class="properties">
          <h5>{{authenticator.authenticatorType}}</h5>
          <p>ID: {{authenticator.authenticatorId}}</p>
        </div>
        <div class="indicators">
          <div class="badge" v-bind:class="{active: authenticator.isPreferred}" @click="prefer(authenticator)">Preferred</div>
          <div class="badge" v-bind:class="{active: authenticator.isRegistered}" @click="authenticator.isRegistered ? deregister(authenticator) : register(authenticator)">Registered</div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import ButtonLarge from '../components/Button-large.vue';

export default {
  components: {
    'button-lg': ButtonLarge
  },

  data () {
    return {
      authenticators: []
    }
  },

  created: function() {
    this.getAuthenticators();
  },

  methods: {
    enrollForMobileAuthentication: function() {
      onegini.mobileAuthentication.enroll()
          .then(() => {
            navigator.notification.alert('You are now ready to receive Mobile Authentication requests!');
          })
          .catch((err) => {
            navigator.notification.alert('Could not enroll for Mobile Authentication ' + err.description);
          });
    },

    changePin: function() {
      onegini.user.changePin()
          .onPinRequest((actions, options) => {
            let callback = (results) => {
              if (results.buttonIndex == 1) {
                actions.providePin(results.input1);
              }
            }

            window.plugins.pinDialog.prompt(
              `Please enter your current pin.\n${options.remainingFailureCount } out of ${options.maxFailureCount } attempts remaining`,
              callback, 'Authenticate', ['Continue','Cancel']);
          })
          .onCreatePinRequest((actions, options) => {
            let callback = (results) => {
              if (results.buttonIndex == 1) {
                actions.createPin(results.input1);
              }
            }

            window.plugins.pinDialog.prompt(
              `Please enter your new ${options.pinLength} digit PIN.`,
              callback, 'Authenticate', ['Continue','Cancel']);
          })
          .onSuccess(() => {
            navigator.notification.alert('PIN changed!')
          })
          .onError((err) => {
            navigator.notification.alert('Change PIN error: ' + err.description);
            if (err.code === 9003) {
              this.$router.push('login'); 
            }
          });
    },

    getAuthenticators: function() {
      onegini.user.getAuthenticatedUserProfile()
          .then((userProfile) => {
            return onegini.user.authenticators.getAll(userProfile);
          })
          .then((authenticators) => {
            this.authenticators = authenticators;
          })
          .catch((err) => {
            console.log(err);
            navigator.notification.alert('Could not get authenticators: ' + err.description);
          });
    },

    register: function(authenticator) {
      let handler = onegini.user.authenticators.registerNew(authenticator)
          .onPinRequest((actions, options) => {
            this.showFingerprintModal = false;
            let callback = (results) => {
              if (results.buttonIndex == 1) {
                actions.providePin(results.input1);
              }
            }

            window.plugins.pinDialog.prompt(
                `Please enter your pin.\n${options.remainingFailureCount } out of ${options.maxFailureCount } attempts remaining`,
                callback, 'Register authenticator', ['Register','Cancel']);
          })
          .onSuccess(() => {
            this.getAuthenticators();
          })
          .onError((err) => {
            navigator.notification.alert('Could not register authenticator: ' + err.description);
            this.getAuthenticators();
          });
      
      return handler;
    },

    deregister: function(authenticator) {
      onegini.user.authenticators.deregister(authenticator)
          .then(() => {
            this.getAuthenticators();
          })
          .catch((err) => {
            navigator.notification.alert('Could not deregister authenticator: ' + err.description);
          });
    },

    prefer: function(authenticator) {
      if (!authenticator.isRegistered) {
        navigator.notification.alert('Authenticator needs to be registered before it can be set as preferred');
        return;
      }

      onegini.user.authenticators.setPreferred(authenticator)
          .then(() => {
            this.getAuthenticators();
          })
          .catch((err) => {
            navigator.notification.alert(err.description);
          });
    }
  }
}
</script>

<style scoped>
ul {
  padding: 0;
  list-style-type: none;
}

li {
  display: flex;
  align-items: center;

  padding: .5em;
  margin: 1.2em;

  background-color: rgba(0, 0, 0, .05);
  box-shadow: 0 0 5px rgba(0, 0, 0, .3);
}

h5 {
  text-align: left;
  margin: 0 0 .3em 0;
}

p {
  margin: 0;
  font-size: .8em;
}

.indicators {
  display: flex;
  flex-direction: row;
  margin-left: auto;
}

.badge {
  margin: .4em;
  padding: .5em;
  font-size: .8em;
  border: 1px solid #999;
  border-radius: .3em;
}

.badge.active {
  color: #5DC115;
  border-color: #5DC115;
}

</style>
