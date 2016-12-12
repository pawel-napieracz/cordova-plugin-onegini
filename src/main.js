import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter);

Vue.config.devtools = true;
Vue.config.debug = true;
Vue.config.silent = false;

import App from './components/App.vue'
import Login from './components/Login.vue'

const router = new VueRouter({
  mode: 'hash',
  routes: [
    {path: '/login', component: Login}
  ]
});

new Vue({
  el: '#app',
  router,
  render: h => h(App)
});
