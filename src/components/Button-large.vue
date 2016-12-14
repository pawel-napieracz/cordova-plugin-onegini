<template>
    <button @click="click">{{text}}</button>
</template>

<script>
export default {
  props: [
    'text'
  ],

  methods: {
    click: function() {
      this.$emit('click');
    }
  }
}
</script>

<style scoped>
  button {
    -webkit-appearance: none;
    display: block;
    padding: .5em;
    width: 80%;
    margin: .5em auto;

    color: #fff;
    font-size: 1.5em;
    text-transform: uppercase;
    
    background-color: #00aeef;
    box-shadow: 0 0 .1em #999;
    border: none;
    border-radius: .1em;
  }
</style>