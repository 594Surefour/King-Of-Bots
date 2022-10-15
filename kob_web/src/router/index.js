import { createRouter, createWebHistory } from 'vue-router';
import NotFound from "@/views/error/NotFound.vue";
import PkIndexView from "@/views/pk/PkIndexView.vue";
import RankListIndexView from "@/views/ranklist/RankListIndexView.vue";
import RecordIndexView from "@/views/record/RecordIndexView.vue";
import RecordContentView from "@/views/record/RecordContentView.vue";
import UserBotIndex from "@/views/user/bot/UserBotIndex.vue";
import UserLoginView from "@/views/user/account/UserLoginView.vue";
import UserRegisterView from "@/views/user/account/UserRegisterView.vue";
import store from '../store/index';

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/:recordId",
    name: "record_content",
    component: RecordContentView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RankListIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndex,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserLoginView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserRegisterView,
     meta: {
      requestAuth: false,
    }
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
     meta: {
      requestAuth: false,
    }
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/"
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requestAuth && !store.state.user.is_login) {
    next({ name: "user_account_login" });
  } else {
    next();
  }
})


export default router
