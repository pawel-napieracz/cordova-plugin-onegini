<template>
  <div>
    <h3>Pending push requests</h3>
    <button-lg text="Refresh" @click="fetchPendingPushRequests()"/>
    <h4 v-if="!pushRequests.length">{{ status }}</h4>
    <ul v-if="pushRequests.length" class="block-list">
      <li v-for="pushRequest in pushRequests" class="block">
        <h5>{{pushRequest.profileId}}<span style="float:right;">{{parseTimestamp(pushRequest.timestamp)}}</span>
        </h5>
        <h3>{{pushRequest.message}}</h3>
        <h5>Will expire at: {{parseTimestamp(pushRequest.timestamp + pushRequest.timeToLiveSeconds)}}</h5>
      </li>
    </ul>
  </div>
</template>

<script>
  import ButtonLarge from '../components/Button-large.vue';

  export default {
    data() {
      return {
        pushRequests: [],
        status: 'No pending requests'
      }
    },

    components: {
      'button-lg': ButtonLarge
    },

    created: function () {
      this.fetchPendingPushRequests();
    },

    methods: {
      fetchPendingPushRequests: function () {
        onegini.mobileAuth.push.getPendingRequests()
          .then((result) => {
            this.pushRequests = result;
          })
          .catch((err) => {
            console.error('Error while fetching pending push requests:', err);
            this.status = 'Could not fetch pending push requests';
          });
      },

      parseTimestamp: function (seconds) {
        return new Date(seconds * 1000).toLocaleTimeString();
      }
    },
  }
</script>

<style scoped>
  h5 {
    margin-top: 0;
  }

  ul {
    list-style-type: none;
  }

  .block-list {
    padding: 0 1em;
    margin: 0 auto;
    width: 80%;
  }

  .block {
    padding: .5em;
    margin: 1.2em 0;
    background-color: rgba(0, 0, 0, .05);
    box-shadow: 0 0 5px rgba(0, 0, 0, .3);
    text-align: left;
  }

  h5 {
    margin: 0 0 .3em 0;
  }
</style>
