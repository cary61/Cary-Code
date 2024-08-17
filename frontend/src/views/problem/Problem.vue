<script setup lang="ts">
import { ref } from 'vue';
import { formalizeTimeMillis, formalizeMemory } from '@/utils/utils';
import {getProblem, getProblemLimit, isAvailable} from '@/network/api/problem-api';
import type ProblemDO from '@/types/do/ProblemDO';
import { reactive, watchEffect } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRoute, useRouter } from 'vue-router';
import { getUser } from '@/network/api/user-api';
import { getSubmissionState } from '@/network/api/submission-api';
import type { Language } from '@/types/enum/Language';
import { judge } from '@/network/api/judge-api';
import { Message } from '@arco-design/web-vue';

const auth = useAuthStore();
const route = useRoute();
const router = useRouter();

const problem: ProblemDO = reactive({
    id: 0,
    number: '',
    title: '',
    uploaderUserId: 0,
    background: '',
    description: '',
    inputFormat: '',
    outputFormat: '',
    dataRange: '',
    difficulty: '',
    state: '',
    tags: []
});


const difficultyText = ref('');
const uploaderUsername = ref('');
const submissionStateText = ref('');
const problemLimitText = reactive({
    maxTimeMillis: '0s',
    maxMemory: '0MB'
});
const available = ref(false);


watchEffect(async () => {
    const data = await getProblem(route.params.problemId as string) as ProblemDO;
    Object.assign(problem, data);
    if (problem.difficulty == 'EASY') {
        difficultyText.value = '简单';
    } else if (problem.difficulty == 'MIDDLE') {
        difficultyText.value = '中等';
    } else {
        difficultyText.value = '困难';
    }
    uploaderUsername.value = (await getUser(problem.uploaderUserId)).nickname;
    let submissionState = await getSubmissionState(auth.userInfo.id, problem.id);
    if (submissionState == 'ATTEMPTED') {
        submissionStateText.value = '尝试过';
    } else if (submissionState == 'UNATTEMPTED') {
        submissionStateText.value = '未开始';
    } else if (submissionState == 'SOLVED') {
        submissionStateText.value = '已解决';
    }
    let problemLimit = getProblemLimit(problem.id);
    problemLimitText.maxTimeMillis = formalizeTimeMillis((await problemLimit).maxTimeMillis);
    problemLimitText.maxMemory = formalizeMemory((await problemLimit).maxMemory);
    available.value = await isAvailable(problem.id);
});

// code submit modal
const codeSubmitModalVisible = ref(false);
const languageOptions = [
    {
        label: 'C++',
        value: 'CPP'
    },
    {
        label: 'Java',
        value: 'JAVA'
    },
    {
        label: 'Python',
        value: 'PYTHON'
    },
]
const codeLanguage = ref('');
const code = ref('');

const codeSubmit = () => {
    if (codeLanguage.value == '') {
        Message.warning("请选择语言！");
        return;
    }

    judge({
        userId: auth.userInfo.id,
        problemId: problem.id,
        language: codeLanguage.value,
        code: code.value as Language
    }).then(msg => router.push(`/submission-log/${msg.uuid}?timeIntervalMillis=${msg.timeIntervalMillis}`))
}

</script>

<template>
    <div id="problem">
        <a-split :disabled="true" size="0.75">
            <template #first>
                <div style="background-color: rgb(253, 253, 253); 
                        height: 850px;
                        margin: 10px 2%;">
                    <div style="margin: 0 3%; padding: 1px 0;">
                        <a-typography-title :heading="3">{{ problem.number }}. {{ problem.title }}</a-typography-title>
                        <a-divider></a-divider>
                        <div v-if="problem.background" style="margin-bottom: 40px">
                            <a-typography-title :heading="4">背景</a-typography-title>
                            <a-typography-title :heading="6">{{ problem.background }}</a-typography-title>
                        </div>
                        <div v-if="problem.description" style="margin-bottom: 40px">
                            <a-typography-title :heading="4">题目描述</a-typography-title>
                            <a-typography-title :heading="6">{{ problem.description }}</a-typography-title>
                        </div>
                        <div v-if="problem.inputFormat" style="margin-bottom: 40px">
                            <a-typography-title :heading="4">输入格式</a-typography-title>
                            <a-typography-title :heading="6">{{ problem.inputFormat }}</a-typography-title>
                        </div>
                        <div v-if="problem.outputFormat" style="margin-bottom: 40px">
                            <a-typography-title :heading="4">输出格式</a-typography-title>
                            <a-typography-title :heading="6">{{ problem.outputFormat }}</a-typography-title>
                        </div>
                        <div v-if="problem.dataRange" style="margin-bottom: 40px">
                            <a-typography-title :heading="4">数据范围</a-typography-title>
                            <a-typography-title :heading="6">{{ problem.dataRange }}</a-typography-title>
                        </div>
                    </div>
                </div>
            </template>
            <template #second>
                <div style="background-color: rgb(253, 253, 253); 
                        height: 125px; 
                        margin: 10px 0;
                        padding: 15px 15px;">
                    <a-typography-title :heading="6" style="margin: auto; float: left;">难度</a-typography-title>
                    <a-typography-title :heading="6" style="margin: auto; float: right;" :class="problem.difficulty">{{
            difficultyText }}</a-typography-title>
                    <div style="clear: both; padding-bottom: 20px;"></div>

                    <a-typography-title :heading="6" style="margin: auto; float: left;">上传者</a-typography-title>
                    <a-typography-title :heading="6" style="margin: auto; float: right;">{{ uploaderUsername
                        }}</a-typography-title>
                    <div style="clear: both; padding-bottom: 20px;"></div>

                    <a-typography-title :heading="6" style="margin: auto; float: left;">通过情况</a-typography-title>
                    <a-typography-title :heading="6" style="margin: auto; float: right;">{{ submissionStateText
                        }}</a-typography-title>
                    <div style="clear: both; padding-bottom: 20px;"></div>
                </div>

                <div style="background-color: rgb(253, 253, 253); 
                        height: 125px; 
                        margin: 20px 0;
                        padding: 15px 15px;">
                    <a-typography-title :heading="6" style="margin: auto; float: left;">时间限制</a-typography-title>
                    <a-typography-title :heading="6" style="margin: auto; float: right;">{{
            problemLimitText.maxTimeMillis }}</a-typography-title>
                    <div style="clear: both; padding-bottom: 20px;"></div>

                    <a-typography-title :heading="6" style="margin: auto; float: left;">内存限制</a-typography-title>
                    <a-typography-title :heading="6" style="margin: auto; float: right;">{{ problemLimitText.maxMemory
                        }}</a-typography-title>
                    <div style="clear: both; padding-bottom: 20px;"></div>

                    <a-typography-title :heading="6" style="margin: auto; float: left;">通过情况</a-typography-title>
                    <a-typography-title :heading="6" style="margin: auto; float: right;">{{ submissionStateText
                        }}</a-typography-title>
                    <div style="clear: both; padding-bottom: 20px;"></div>
                </div>

                <div style="background-color: rgb(253, 253, 253); 
                        height: 90px; 
                        margin: 20px 0;
                        padding: 15px 15px;">
                    <a-typography-title :heading="6" style="margin: 0 0 15px 0;">标签</a-typography-title>
                    <a-typography-title v-if="problem.tags.length == 0" :heading="6"
                        style="margin: auto;">暂无标签</a-typography-title>
                    <a-space v-else>
                        <a-tag v-for="tag of problem.tags" size="large">{{ tag }}</a-tag>
                    </a-space>
                    <div style="clear: both; padding-bottom: 20px;"></div>
                </div>

                <div style="background-color: rgb(253, 253, 253); 
                        height: 90px; 
                        margin: 20px 0;
                        padding: 15px 15px;">
                    <a-typography-title :heading="6" style="margin: 0 0 15px 0;">评测</a-typography-title>
                    <div style="margin: 5% 12%;">
                        <a-button type="primary" size="large" style="float: left;" :disabled="!available"
                            @click="codeSubmitModalVisible = true">上传代码</a-button>
                        <a-button type="secondary" size="large" style="float: right;"
                                @click="$router.push(`/submission-log-list/${problem.id}`)">评测记录</a-button>
                        <div style="clear: both; padding-bottom: 20px;"></div>
                    </div>

                </div>

                <div v-if="auth.userInfo.id == problem.uploaderUserId || auth.userInfo.authority == 'ADMINISTRATOR' "
                    style="background-color: rgb(253, 253, 253);
                        height: 90px;
                        margin: 20px 0;
                        padding: 15px 15px;">
                    <a-typography-title :heading="6" style="margin: 0 0 15px 0;">管理</a-typography-title>
                    <div style="margin: 5% 12%;">
                        <a-button type="secondary" size="large" style="float: left;"
                                  @click="$router.push(`/problem-edit/${problem.id}`)">修改题目</a-button>
                        <a-button type="secondary" size="large" style="float: right;"
                                  @click="$router.push(`/problem-cases/${problem.id}`)">管理样例</a-button>
                        <div style="clear: both; padding-bottom: 20px;"></div>
                    </div>
                </div>

            </template>
        </a-split>
    </div>

    <!-- code submit modal -->
    <a-modal :visible="codeSubmitModalVisible" @ok="codeSubmit" @cancel="codeSubmitModalVisible = false" okText="提交"
        unmountOnClose>
        <template #title>
            上传代码
        </template>
        <div>
            <a-select style="width: 320px; margin-bottom: 20px;" placeholder="请选择语言" v-model="codeLanguage"
                :options="languageOptions">
            </a-select>
            <a-textarea v-model="code" placeholder="在此输入代码" :max-length="5000" show-word-limit autoSize
                    style="min-height: 500px;"/>
        </div>
    </a-modal>

</template>

<style scoped>
#problem {
    margin: 0 10%;
    height: 880px;
}

.EASY {
    color: green;
}

.MIDDLE {
    color: orange;
}

.HARD {
    color: red;
}
</style>