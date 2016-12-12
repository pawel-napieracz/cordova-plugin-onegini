<template>
  <div>
    <h1>Login</h1>
    <select v-if="userProfiles.length > 0" v-model="selectedProfileId">
      <option v-for="userProfile in userProfiles">{{userProfile.profileId}}</option>
    </select>
    <button @click="login">Login</button>
    <button @click="register">Register</button>
  </div>
</template>

<script>
export default {
  data () {
    return {
      userProfiles: [],
      selectedProfileId: null
    }
  },

  created: function() {
    this.getUserProfiles();
  },

  methods: {
    getUserProfiles: function() {
       onegini.user.getUserProfiles()
            .then(userProfiles => {
              this.userProfiles = userProfiles;
            })
            .catch(function(err) {
              console.log(err);
            });
    },

    login: function() {
      onegini.user.authenticate(this.selectedProfileId)
        .onPinRequest((actions, options) => {
          window.plugins.pinDialog.prompt(
                `Please enter your pin.\n${options.remainingFailureCount } out of ${options.maxFailureCount } attempts remaining`,
                callback,
                'Authenticate',
                ['Login','Cancel']);

                function callback(results) {
                  if (results.buttonIndex == 1) {
                    actions.providePin(results.input1);
                  }
                }
        })
        .onSuccess(() => {
          navigator.notification.alert('Authentication success. You are now logged in!');
        })
        .onError((err) => {
          navigator.notification.alert('Authentication failed. ' + err.reason);
        });
    },

    register: function() {
      onegini.user.register()
          .onCreatePinRequest((actions, options) => {
            window.plugins.pinDialog.prompt(
              'Create your ' + options.pinLength + ' digit pin',
              callback,
              'Register',
              ['Create','Cancel']);

              function callback(results) {
                if (results.buttonIndex == 1) {
                  actions.createPin(results.input1);
                }
              }
          })
          .onSuccess((result) => {
            this.getUserProfiles();
            navigator.notification.alert('Registration success! Profile ID: ' + result.profileId);
          })
          .onError((err) => {
            navigator.notification.alert('Registration failed. ' + err.reason);
          });
    }
  }
}
</script>

<style scoped>
  h1 {
    font-weight: normal;
  }
</style>