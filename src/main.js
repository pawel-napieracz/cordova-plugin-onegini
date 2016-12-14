import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter);

Vue.config.devtools = true;
Vue.config.debug = true;
Vue.config.silent = false;

import App from './views/App.vue';
import Login from './views/Login.vue';
import Dashboard from './views/Dashboard.vue';

const router = new VueRouter({
  mode: 'abstract',
  routes: [
    {path: '/login', component: Login},
    {path: '/dashboard', component: Dashboard}
  ]
});

new Vue({
  el: '#app',
  router,
  render: h => h(App)
});
