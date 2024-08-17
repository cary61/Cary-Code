<script setup lang="ts">
import Header from './components/header/Header.vue';
import Footer from './components/Footer.vue';
</script>

<template>
    <div >
        <a-layout style="height: 400px;">
            <a-layout-header>
                <Header />
            </a-layout-header>
            <a-layout-content id="content">
                <slot />
            </a-layout-content>
            <a-layout-footer>
                <Footer />
            </a-layout-footer>
        </a-layout>
    </div>
</template>

<style scoped>
#content {
    background-color: var(--color-fill-2);
}
</style>