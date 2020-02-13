import Vue from 'vue'
import Router from 'vue-router'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import SprintPlanning from '@/components/SprintPlanning'
import Login from '@/components/Login'
import GroupOverview from '@/components/GroupOverview'
import ProzessZwischensicht from '../components/ProzessZwischensicht'
import DevelopView from '@/components/DevelopView'
import Intro from '@/components/Intro'
import ProductBacklog from '@/components/ProductBacklog'
import DinoView from '@/components/DinoView'
import SprintProgress from '@/components/SprintProgress'
import Task from '@/components/Task'
import Overview from '@/components/Overview'
import Evaluation from '@/components/Evaluation'
import Backlog from '@/components/Backlog'
import Review from '@/components/Review'
import Retro from '@/components/Retro'

Vue.use(Vuetify)
Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', name: '', redirect: '/Login' },
    { path: '/Login', name: 'Login', component: Login },
    { path: '/Retro', name: 'Retro', component: Retro },
    { path: '/Intro', name: 'Intro', component: Intro },
    { path: '/GroupOverview', name: 'GroupOverview', component: GroupOverview },
    { path: '/Overview', name: 'Overview', component: Overview },
    { path: '/Evaluation', name: 'Evaluation', component: Evaluation },
    { path: '/SprintPlanning', name: 'SprintPlanning', component: SprintPlanning },
    { path: '/DevelopView', name: 'DevelopView', component: DevelopView },
    { path: '/Prozess-Zwischensicht', name: 'ProzessZwischensicht', component: ProzessZwischensicht },
    { path: '/ProductBacklog', name: 'ProductBacklog', component: ProductBacklog },
    { path: '/DinoView', name: 'DinoView', component: DinoView },
    { path: '/SprintProgress', name: 'SprintProgress', component: SprintProgress },
    { path: '/Task', name: 'Task', component: Task },
    { path: '/Backlog', name: 'Backlog', component: Backlog },
    { path: '/Review', name: 'Review', component: Review }

  ]
})
