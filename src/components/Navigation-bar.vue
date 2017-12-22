<template>
  <div class="wrapper">
    <div class="box" @click="redirect()">
      <img src="img/onegini-key.png"/>
      <p class="navigation-element-description">Home</p>
    </div>
    <div class="box" @click="$router.push('pendingRequests')">
      <img src="img/notifications.png"/>
      <p class="navigation-element-description">Notifications</p>
    </div>
    <div class="box">
      <img src="img/http.png"/>
      <p class="navigation-element-description">Resource call</p>
    </div>
  </div>
</template>

<script>
  export default {
    methods: {
      redirect: function () {
        if ('PendingPushRequests' === this.$route.name) {
          this.$router.back();
        }
      },
    }
  }

</script>

<style scoped>
  .wrapper {
    width: 100%;
    position: fixed;
    bottom: 0;
    left: 0;
    background-color: #00aeef;
    display: flex;
    justify-content: space-around;
  }

  .box {
    align-content: center;
    width: 5em;
  }

  .navigation-element-description {
    color: white;
    font-size: small;
    margin-top: 0;
    margin-bottom: 0.3em;
  }

  img {
    margin-top: 0.5em;
    margin-bottom: 0;
    width: 25px;
  }

</style>
