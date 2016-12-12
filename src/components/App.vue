<template>
  <div id="app">
    <img src="img/logo.png" />
    <div v-if="ready">
      <router-link to="/">App</router-link>
      <router-link to="/login">Login</router-link>
    </div>

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
      ready: false
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
          this.ready = true;
        })
        .catch((err) => {
          this.state = err.description;
        })
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

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}

</style>
