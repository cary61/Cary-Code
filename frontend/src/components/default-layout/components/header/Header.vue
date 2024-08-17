<script setup lang="ts">
import { useAuthStore } from '@/stores/auth';
import { ref, reactive, computed } from 'vue';
import {login, logout, register} from '@/network/api/auth-api';
import { Message } from '@arco-design/web-vue';
const auth = useAuthStore();

interface Page {
    name: string;
    path: string;
    requiresAdmin?: boolean;
}

const allPages: Page[] =
    [
        {
            name: '题单',
            path: '/problem'
        },
        {
            name: '管理',
            path: '/admin',
            requiresAdmin: true
        }
    ];

const pages = computed(() => allPages.filter(e => !e.requiresAdmin || auth.userInfo.authority == 'ADMINISTRATOR'));


// login form
const loginFormVisible = ref(false);
const loginForm = reactive({
    username: '',
    password: ''
});
const onLoginFormSubmit = async () => {
    let status = false;
    await login(loginForm).then(() => status = true);
    return status;
}

// register form
const registerFormVisible = ref(false);
const registerForm = reactive({
    username: '',
    password: '',
    passwordRepeat: ''
});
const onRegisterFormSubmit = async () => {
    if (!checkPasswordConsistence()) {
        return false;
    }
    let status = false;
    await register({
        username: registerForm.username,
        password: registerForm.password
    }).then(() => status = true)
    return status;
}
const checkPasswordConsistence = () => {
    if (registerForm.password != registerForm.passwordRepeat) {
        Message.error('两次密码输入不一致!');
        return false;
    }
    return true;
}

</script>

<template>
    <div class="header">
        <a-row justify="space-between" align="center" style="margin: 0 10%;">
            <a-col :span="5">
                <h2>CaryCode</h2>
            </a-col>

            <a-col :span="8">
                <!-- <a-menu mode="horizontal" :default-selected-keys="[1]"
                        @menu-item-click="(str) => console.log(str)">
                    <a-menu-item key="1">Home</a-menu-item>
                    <a-menu-item key="2">Test</a-menu-item>
                    <a-menu-item key="3">Test1</a-menu-item>
                    <a-menu-item key="4">Cooperation</a-menu-item>
                </a-menu> -->
                <a-menu mode="horizontal" @menu-item-click="(key) => $router.push(pages[key - 1].path)">
                    <a-menu-item v-for="(page, index) in pages" :key="index + 1">
                        {{ page.name }}
                    </a-menu-item>
                </a-menu>
            </a-col>

            <a-col :span="2" v-if="auth.isLoggedIn">
                <span >
                    {{ auth.userInfo.nickname }}
                </span>
                <a-button type="text" @click="logout()">退出</a-button>
            </a-col>
            <a-col :span="4" v-else>
                <a-space>
                    <a-button type="text" style="color: black;" @click="loginFormVisible = true">
                        登录
                    </a-button>
                    <a-divider direction="vertical" :size="1.5" />
                    <a-button type="text" style="color: black;" @click="registerFormVisible = true">
                        注册
                    </a-button>
                </a-space>
            </a-col>
        </a-row>
    </div>

    <a-modal v-model:visible="loginFormVisible" title="登录" @cancel="loginFormVisible = false" :on-before-ok="onLoginFormSubmit">
        <a-form :model="loginForm">
            <a-form-item label="用户名">
                <a-input v-model="loginForm.username" />
            </a-form-item>
            <a-form-item label="密码">
                <a-input-password v-model="loginForm.password" />
            </a-form-item>
        </a-form>
    </a-modal>

    <a-modal v-model:visible="registerFormVisible" title="注册" @cancel="registerFormVisible = false"
         :on-before-ok="onRegisterFormSubmit">
        <a-form :model="registerForm">
            <a-form-item label="用户名">
                <a-input v-model="registerForm.username" />
            </a-form-item>
            <a-form-item label="密码">
                <a-input-password v-model="registerForm.password" :invisible-button="false" />
            </a-form-item>
            <a-form-item label="确认密码">
                <a-input-password v-model="registerForm.passwordRepeat" :invisible-button="false" />
            </a-form-item>
        </a-form>
    </a-modal>

</template>


<style scoped>
.header {
    box-sizing: border-box;
    width: 100%;
    position: relative;
    padding-top: 2px;
    /* background-color: var(--color-neutral-2); */
}
</style>