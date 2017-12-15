<template>
  <div>
    <p v-if="!pushRequests.length">{{ status }}</p>
    <ul v-if="pushRequests.length" class="block-list">
      <li v-for="pushRequest in pushRequests" class="block" @click="handleRequest(pushRequest)">
        <p class="request-attributes">{{pushRequest.profileId}}<time style="float:right;">{{parseTimestamp(pushRequest.timestamp)}}</time>
        </p>
        <p class="request-message">{{pushRequest.message}}</p>
        <p class="request-attributes">Will expire at:<time>{{parseTimestamp(pushRequest.timeToLiveSeconds * 1000 + pushRequest.timestamp)}}</time>
        </p>
      </li>
    </ul>
  </div>
</template>

<script>

  export default {
    data() {
      return {
        pushRequests: [],
        status: 'No pending requests'
      }
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

      handleRequest: function (pushRequest) {
        //TODO: refresh the list after the request in handled
        onegini.mobileAuth.push.handlePendingRequest(pushRequest);
      },

      parseTimestamp: function (millis) {
        return new Date(millis).toLocaleTimeString();
      }
    }
  }
</script>

<style scoped>

  ul {
    list-style-type: none;
  }

  p {
    font-weight: bold;
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

  .request-attributes {
    margin: 0 0 .3em 0;
    font-size: smaller;
  }

  .request-message {
    font-size: larger;
  }

</style>
