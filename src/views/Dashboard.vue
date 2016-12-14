<template>
  <div>
    <h3>Hello!</h3>
    <button-lg text="Settings" @click="$router.push('settings')" />
    <button-lg text="Logout" @click="logout" />
    <button-lg text="Deregister" @click="deregister" />
    <h3>Your devices</h3>
    <device-list />
  </div>
</template>

<script>
import ButtonLarge from '../components/Button-large.vue';
import DeviceList from '../components/Device-list.vue';

export default {
  components: {
    'button-lg': ButtonLarge,
    'device-list': DeviceList
  },

  methods: {
    logout: function() {
      onegini.user.logout()
          .then(() => {
            this.$router.push('login')
          });
    },

    deregister: function() {
      onegini.user.getAuthenticatedUserProfile()
          .then((userProfile) => {
            return onegini.user.deregister(userProfile);
          })
          .then(() => {
            this.$router.push('login');
          })
          .catch((err) => {
            navigator.notification.alert('Something went wrong! ' + err.description, () => {
              this.$router.push('login');
            });
          });
    }
  }
}
</script>