<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <router-link class="navbar-brand" :to="{name: 'home'}">King of Bots</router-link>
            <div class="collapse navbar-collapse" id="navbarText">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                <router-link :class="router_name == 'pk_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'pk_index'}">战斗</router-link>
                </li>
                <li class="nav-item">
                <router-link :class="router_name == 'record_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'record_index'}">对局列表</router-link>
                </li>
                <li class="nav-item">
                <router-link :class="router_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'ranklist_index'}">天梯排行</router-link>
                </li>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        个人空间
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <li><router-link class="dropdown-item" :to="{name: 'user_bot_index'}">My Bots</router-link></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="#">退出</a></li>
                    </ul>
                </li>
            </ul>
            </div>
        </div>
    </nav>
</template>


<script>
    import { useRoute } from 'vue-router';
    import { computed } from '@vue/reactivity';

    export default{
        setup() {
            const route = useRoute();
            let router_name = computed(() => { route.name });
            return{
                router_name,
            }
        }
    }
</script>


<style scoped></style>