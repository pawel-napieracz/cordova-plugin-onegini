<template>
  <select v-model="selectedIdentityProviderId">
    <option :value="null">Default identity provider</option>
    <option :value="identityProvider.identityProviderId" v-for="identityProvider in identityProviders">{{identityProvider.identityProviderName}}
    </option>
  </select>
</template>

<script>
  export default {
    data() {
      return {
        selectedIdentityProviderId: null
      }
    },

    props: [
      'identity-providers'
    ],

    watch: {
      selectedIdentityProviderId: function () {
        this.$emit('select', this.selectedIdentityProviderId);
      }
    }
  }
</script>

<style scoped>
  select {
    -webkit-appearance: none;
    display: block;
    position: relative;
    padding: .1em .5em;
    min-width: 80%;
    margin: .5em auto;

    color: #00aeef;
    font-size: 1.1em;

    background-color: transparent;
    border: 1px solid #00aeef;
    border-radius: .1em;
  }
</style>
