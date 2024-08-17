<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
import api from '@/network/http/api';
import { Message } from '@arco-design/web-vue';
import { register, type AuthDTO, login } from '@/network/api/auth-api';

const input = ref('');

const info = ref('');

const good = () => {
    api.get('/hello')
        .then(resp => resp.data as string)
        .then(data => info.value = data);
}

const bad = () => {
    api.get('/bad')
        .then(resp => resp.data as string)
        .then(data => info.value = data);
}

const admin = () => {
    api.get('/admin/hello')
        .then(() => Message.success("请求成功"))
}

const changeAdminState = () => {
    if (auth.userInfo.authority == 'ADMINISTRATOR') {
        auth.setUserInfo({
            id: 0,
            name: '',
            nickname: '',
            avatar: '',
            signature: '',
            authority: 'USER'
        })
    } else {
        auth.setUserInfo({
            id: 0,
            name: '',
            nickname: '',
            avatar: '',
            signature: '',
            authority: 'ADMINISTRATOR'
        })
    }
}

const authDTO: AuthDTO = reactive({
    username: '',
    password: ''
})

const authDTO1: AuthDTO = reactive({
    username: '',
    password: ''
});

</script>

<template>
    <div class="view">
        <h1>Test1</h1>
        <p>token: {{ auth.token }}</p>
        <input v-model="input" />
        <button @click="auth.setToken(input)">set token</button>
        <br />
        <button @click="api.post('/hello').then(resp => Message.success(resp.data))">post</button>
        <br />
        <button @click="good">good</button>
        <button @click="bad">bad</button>
        <br />
        <p>{{ info }}</p>
        <br />
        <button @click="admin">admin</button>
        <br />
        <button @click="changeAdminState">change admin state</button>
        <br>
        <br>

        <a-form :model="authDTO" :style="{ width: '600px' }" @submit="register(authDTO)">
            <a-form-item field="name" tooltip="Please enter username" label="用户名">
                <a-input v-model="authDTO.username" placeholder="please enter your username..." />
            </a-form-item>
            <a-form-item field="post" label="密码">
                <a-input v-model="authDTO.password" placeholder="please enter your post..." />
            </a-form-item>
            <a-form-item>
                <a-button html-type="submit">注册</a-button>
            </a-form-item>
        </a-form>

        <br>

        <a-form :model="authDTO1" :style="{ width: '600px' }" @submit="login(authDTO1)">
            <a-form-item field="name" tooltip="Please enter username" label="用户名">
                <a-input v-model="authDTO1.username" placeholder="please enter your username..." />
            </a-form-item>
            <a-form-item field="post" label="密码">
                <a-input v-model="authDTO1.password" placeholder="please enter your post..." />
            </a-form-item>
            <a-form-item>
                <a-button html-type="submit">登录</a-button>
            </a-form-item>
        </a-form>
    </div>
</template>

<style scoped>
.view {
    background-color: aqua;
    height: 800px;
}
</style>