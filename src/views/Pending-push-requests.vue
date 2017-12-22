<template>
  <div>
    <h1>Pending push requests</h1>
    <button-lg text="Refresh" @click="fetchPendingPushRequests()"/>
    <pending-push-requests-list ref="pendingPushRequestsList"/>
  </div>
</template>

<script>
  import ButtonLarge from '../components/Button-large.vue';
  import PendingPushRequestsList from '../components/Pending-push-requests-list.vue';

  export default {

    components: {
      'button-lg': ButtonLarge,
      'pending-push-requests-list': PendingPushRequestsList
    },
    methods: {
      fetchPendingPushRequests: function () {
        this.$refs.pendingPushRequestsList.fetchPendingPushRequests();
      }
    }
  }
</script>

<style scoped>

  h1 {
    font-weight: normal;
  }

</style>
