<script setup lang="ts">
import { RouterView } from 'vue-router'
import DefaultLayout from '@/components/default-layout/DefaultLayout.vue';
// import DefaultLayout from '@/components/default-layout1/default-layout.vue';
</script>

<template>
    <DefaultLayout>
        <RouterView />
    </DefaultLayout>
</template>
