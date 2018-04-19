<template>
  <div>
    <h1>One-Time Password</h1>
    <p class="message">Challenge code</p>
    <p class="request-code">{{$route.params.code}}</p>
    <p class="message">Response code</p>
    <input class="response-code" type="number" inputmode="numeric" v-model="code" title=""/>
    <div class="buttons">
      <button-lg class="button" text="✓ Accept" @click="accept"/>
      <button-lg class="button" text="✗ Deny" @click="deny"/>
    </div>
  </div>
</template>

<script>
  import ButtonLarge from '../components/Button-large.vue';

  export default {
    data() {
      return {
        code: null,
        identityProviderId: null,
      }
    },
    components: {
      'button-lg': ButtonLarge,
    },
    methods: {
      accept: function () {
        this.$route.params.actions.acceptRegistrationCompleteRequest({
          data: this.code,
          identityProviderId: this.$route.params.identityProviderId
        });
        this.$router.back();
      },
      deny: function () {
        this.$route.params.actions.denyRegistrationCompleteRequest({
          identityProviderId: this.$route.params.identityProviderId
        });
        this.$router.back();
      }
    }
  }
</script>

<style scoped>
  .buttons {
    width: 100%;
    position: fixed;
    top: 440px;
    left: 0;
    display: flex;
    justify-content: space-around;
  }

  .button {
    align-content: center;
    margin: .3em;
  }

  .message {
    font-size: larger;
    font-weight: bolder;
  }

  .request-code {
    font-size: 250%;
    color: #808080;
    margin: .1em;
  }

  .response-code {
    font-size: 250%;
    margin: .1em;
    width: 60%;
    text-align: center;
    color: #00aeef;
  }
</style>
