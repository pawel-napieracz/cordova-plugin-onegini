<template>
  <div>
    <pending-push-requests/>
  </div>
</template>

<script>
  import PendingPushRequests from '../components/Pending-push-requests-list.vue';

  export default {

    components: {
      'pending-push-requests': PendingPushRequests
    }
  }
</script>
