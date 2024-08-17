<script setup lang="ts">
import { ref, reactive, watchEffect } from 'vue';
import { SubmissionLog } from "@/types/po/SubmissionLog";
import { getSubmissionListByProblemId } from "@/network/api/submission-api";
import { parseChinese, formalizeTime, formalizeMemory } from '@/utils/utils';
import { IconCaretLeft, IconSearch } from '@arco-design/web-vue/es/icon';
import { useRoute, useRouter } from 'vue-router';
import type ProblemDO from "@/types/do/ProblemDO";
import {addProblem, deleteProblem, getProblem, updateProblem} from "@/network/api/problem-api";
import {Message} from "@arco-design/web-vue";
const route = useRoute();
const router = useRouter();

const problem: ProblemDO = reactive({
    id: -1,
    number: '',
    title: '',
    uploaderUserId: 0,
    background: '',
    description: '',
    inputFormat: '',
    outputFormat: '',
    dataRange: '',
    difficulty: 'EASY',
    state: 'PUBLIC',
    tags: []
});

watchEffect(async () => {
    let pathId = route.params.problemId;
    if (pathId == '') {
        problem.id = -1;
        return;
    }

    problem.id = parseInt(pathId as string);
    const data = await getProblem(pathId as string) as ProblemDO;
    Object.assign(problem, data);
});

const handleSubmit = () => {
    let result: Promise<null>;
    if (problem.id == -1) {
        result = addProblem(problem);
    } else {
        result = updateProblem(problem);
    }
    result.then(() => {
        Message.success("提交成功");
        router.go(-1);
    });
}

const handleDelete = () => {
    deleteProblem(problem.id)
        .then(() => Message.success('删除成功'))
        .then(() => router.push('/problem-list'));
}

</script>

<template>
    <div id="submissionLogList">
        <div style="margin: 0 20px; padding: 2px 2px;">
            <a-space>
                <a-button style="margin-top: 16px;" type="secondary" @click="$router.go(-1)">
                    <template #icon>
                        <icon-caret-left />
                    </template>
                </a-button>
                <a-typography-title :heading="3" style="display: inline-block;">题目编辑</a-typography-title>
            </a-space>

            <a-divider/>

            <a-form size="large" :model="problem" @submit="handleSubmit" style="max-width: 1200px">
                <a-form-item label="难度">
                    <a-radio-group v-model="problem.difficulty" type="button" size="large" :style="{width:'170px', display: 'inlineBlock'}">
                        <a-radio value="EASY">简单</a-radio>
                        <a-radio value="MIDDLE">中等</a-radio>
                        <a-radio value="HARD">困难</a-radio>
                    </a-radio-group>
                </a-form-item>
                <a-form-item label="标题">
                    <a-input v-model="problem.title" placeholder="请输入标题" />
                </a-form-item>
                <a-form-item label="题目背景">
                    <a-textarea v-model="problem.background" placeholder="请输入题目背景" :auto-size="{minRows: 3}"/>
                </a-form-item>
                <a-form-item label="题目描述">
                    <a-textarea v-model="problem.description" placeholder="请输入题目描述" :auto-size="{minRows: 3}"/>
                </a-form-item>
                <a-form-item label="输入格式">
                    <a-textarea v-model="problem.inputFormat" placeholder="请输入输入格式" :auto-size="{minRows: 3}"/>
                </a-form-item>
                <a-form-item label="输出格式">
                    <a-textarea v-model="problem.outputFormat" placeholder="请输入输出格式" :auto-size="{minRows: 3}"/>
                </a-form-item>
                <a-form-item label="数据范围">
                    <a-textarea v-model="problem.dataRange" placeholder="请输入数据范围" :auto-size="{minRows: 3}"/>
                </a-form-item>
                <a-form-item label="标签">
                    <a-input-tag v-model="problem.tags" :style="{width:'320px'}" placeholder="按回车确认标签" allow-clear/>
                </a-form-item>
                <a-form-item>
                    <a-space>
                        <a-button html-type="submit" type="primary">提交</a-button>
                        <a-button status="danger" @click="handleDelete" v-if="problem.id != -1">删除</a-button>
                    </a-space>
                </a-form-item>
            </a-form>
        </div>
    </div>
</template>

<style scoped>
#submissionLogList {
    margin: 20px 10%;
    background-color: white;
    min-height: 600px;
}
</style>