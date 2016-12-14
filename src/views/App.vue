<template>
  <div id="app">
    <img src="img/logo.png" />
    <template v-if="$route.matched.length">
      <router-view></router-view>
    </template>
    <template v-else>
      <p>{{state}}</p>
    </template>
  </div>
</template>

<script>
export default {
  data () {
    return {
      state: 'Waiting for device...',
    }
  },

  created: function() {
    document.addEventListener('deviceready', this.startOnegini, false);
  },

  methods: {
    startOnegini: function() {
      this.state = 'Waiting for Onegini Plugin...';

      onegini.start()
          .then(() => {
            this.state = 'Ready!';
            return onegini.user.getAuthenticatedUserProfile();
          })
          .catch((err) => {
            if (err.code == 8005) {
              this.$router.push('login');
            } else {
              this.state = err.description;
            }
          });
    }
  }
}
</script>

<style scoped>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

img {
  display: block;
  height: 50px;
  margin: 0 auto;
}

h1, h2 {
  font-weight: normal;
}

</style>
