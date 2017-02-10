<template>
  <div>
    <ul>
        <li v-for="authenticator in authenticators">
          <div class="properties">
            <h5>{{authenticator.name}}</h5>
            <p>Type: {{authenticator.authenticatorType}}</p>
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
export default {
  data () {
    return {
      authenticators: []
    }
  },

  created: function() {
    this.getAuthenticators()
  },

  methods: {
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
            
            if (err.code === 9003) {
              this.$router.push('login');
            } else {
              this.getAuthenticators();
            }
          });
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
  margin: 0 auto;
  width: 80%;
  list-style-type: none;
}

li {
  display: flex;
  align-items: center;

  padding: .5em;
  margin: 1.2em 0;

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
