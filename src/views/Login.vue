<template>
  <div>
    <h1>Login</h1>
    <select-profile v-if="userProfiles.length > 0" @select="selectProfileId" :user-profiles="userProfiles" />
    <button-lg v-if="userProfiles.length > 0" @click="login" text="Login" />
    <button-lg @click="register" text="Register" />
  </div>
</template>

<script>
import ButtonLarge from '../components/Button-large.vue';
import SelectProfile from '../components/Select-profile.vue';

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

    selectProfileId: function(profileId) {
      this.selectedProfileId = profileId;
    },

    login: function() {
      if(!this.selectedProfileId) {
        navigator.notification.alert('Please select a User Profile first');
        return;
      }

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
          console.error(err);
          navigator.notification.alert('Authentication failed. ' + err.description);
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
            navigator.notification.alert('Registration failed. ' + err.description);
          });
    }
  },

  components: {
    'button-lg': ButtonLarge,
    'select-profile': SelectProfile
  }
}
</script>

<style scoped>
  h1 {
    font-weight: normal;
  }
</style>