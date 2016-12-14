<template>
  <ul class="block-list">
    <li v-for="device in devices" class="block">
      <h5>{{device.name}}</h5>
      <ul class="prop-list">
        <li>Application: {{ device.application }}</li>
        <li>Platform: {{ device.platform }}</li>
        <li>Mobile authentication: {{ device.mobile_authentication_enabled ? "YES" : "NO" }}</li>
      </ul
    </li>
  </ul>
</template>

<script>
export default {
  data () {
    return {
      devices: []
    }
  },

  created: function() {
    this.fetchDeviceList();
  },

  methods: {
    fetchDeviceList: function() {
      onegini.resource.fetch('https://onegini-msp-snapshot.test.onegini.io/resources/devices')
          .then((result) => {
            this.devices = JSON.parse(result.body).devices;
          })
          .catch((err) => {
            navigator.notification.alert('Error while fetching device list! ' + err.description)
          });
    }
  },
}
</script>

<style scoped>
ul {
  list-style-type: none;
  margin: 0;
}

.block-list {
  padding: 0 1em;
}

.block {
  padding: .5em;
  margin: 1.2em;
  background-color: rgba(0, 0, 0, .05);
  box-shadow: 0 0 5px rgba(0, 0, 0, .3);
  text-align: left;
}

h5 {
  margin: 0 0 .3em 0;
}

.prop-list {
  padding: 0;
  font-size: .8em;
}
</style>