<template>
  <div>
    <h1>Login</h1>
    <select-profile v-if="userProfiles.length > 0" @select="selectProfileId" :user-profiles="userProfiles" />
    <button-lg v-if="userProfiles.length > 0" @click="login" text="Login" />
    <button-lg @click="register" text="Register" />
    <fingerprint-modal v-if="showFingerprintModal" :status="fingerprintStatus" :actions="fingerprintActions" />
  </div>
</template>

<script>
import ButtonLarge from '../components/Button-large.vue';
import SelectProfile from '../components/Select-profile.vue';
import FingerprintModal from '../components/Fingerprint-modal.vue';

export default {
  data () {
    return {
      userProfiles: [],
      selectedProfileId: null,
      showFingerprintModal: false,
      fingerprintStatus: null,
      fingerprintActions: null
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
          this.showFingerprintModal = false;
          let callback = (results) => {
            if (results.buttonIndex == 1) {
              actions.providePin(results.input1);
            }
          }

          window.plugins.pinDialog.prompt(
              `Please enter your pin.\n${options.remainingFailureCount } out of ${options.maxFailureCount } attempts remaining`,
              callback, 'Authenticate', ['Login','Cancel']);
        })
        .onFingerprintRequest((actions) => {
          let callback = (result) => {
            if (result == 1) {
              actions.acceptFingerprint({ iosPrompt: 'Login to the Example App'});

              if (cordova.platformId === 'android'){
                this.showFingerprintModal = true;
                this.fingerprintActions = actions;
                this.fingerprintStatus = 'Touch sensor to start';
              }
            } else {
              actions.fallbackToPin();
            }
          }

          navigator.notification.confirm('Login using your fingerprint?', callback, 'Authenticate', ['Continue','Use PIN']);
        })
        .onFingerprintCaptured(() => {
          this.fingerprintStatus = 'Verifying...';
        })
        .onFingerprintFailed(() => {
          this.fingerprintStatus = 'No match!';
        })
        .onFidoRequest((actions) => {
          let callback = (result) => {
            if (result == 1) {
              actions.acceptFido();
            } else {
              actions.fallbackToPin();
           }
         }

          navigator.notification.confirm('Login using FIDO?', callback, 'Authenticate', ['Continue','Use PIN']);
        })
        .onSuccess(() => {
          this.$router.push('dashboard');
        })
        .onError((err) => {
          console.error(err);
          this.getUserProfiles();
          navigator.notification.alert('Authentication failed. ' + err.description);
        });
    },

    register: function() {
      onegini.user.register()
          .onCreatePinRequest((actions, options) => {
            let callback = (results) => {
              if (results.buttonIndex == 1) {
                actions.createPin(results.input1);
              }
            }

            window.plugins.pinDialog.prompt('Create your ' + options.pinLength + ' digit pin', callback, 'Register', ['Create','Cancel']);
          })
          .onSuccess((result) => {
            this.$router.push('dashboard');
          })
          .onError((err) => {
            navigator.notification.alert('Registration failed. ' + err.description);
          });
    }
  },

  components: {
    'button-lg': ButtonLarge,
    'select-profile': SelectProfile,
    'fingerprint-modal': FingerprintModal
  }
}
</script>

<style scoped>
  h1 {
    font-weight: normal;
  }
</style>