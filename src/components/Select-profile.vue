<template>
  <select v-model="selectedProfileId">
    <option :value="null">Select a User Profile</option>
    <option :value="userProfile.profileId" v-for="userProfile in userProfiles">{{userProfile.profileId}}</option>
  </select>
</template>

<script>
export default {
  data () {
    return {
      selectedProfileId: null
    }
  },

  props: [
    'user-profiles'
  ],

  watch: {
    selectedProfileId: function() {
      this.$emit('select', this.selectedProfileId);
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