<script setup lang="ts">
import { ref, reactive, watchEffect } from 'vue';
import { IconCaretLeft, IconPlus } from '@arco-design/web-vue/es/icon';
import { useRoute, useRouter } from 'vue-router';
import { getProblemCases, updateProblemCases} from "@/network/api/problem-api";
import {Message} from "@arco-design/web-vue";
import type ProblemCase from "@/types/po/ProblemCase";
import problemCases from "@/router/routes/modules/problem-cases";
const route = useRoute();
const router = useRouter();

class ProblemCaseImpl implements ProblemCase {

    problemId: number;
    input: string;
    output: string;
    maxTimeMillis: number;
    maxMemory: number;

    constructor() {
        this.problemId = problemId.value;
        this.input = '';
        this.output = '';
        this.maxTimeMillis = 1000;
        this.maxMemory = 125;
    }
}

const problemId = ref(-1);
const cases: ProblemCase[] = reactive([]);

watchEffect(async () => {
    let pathId = route.params.problemId;
    if (pathId == '') {
        Message.error("题目信息缺失");
        router.go(-1);
    }
    problemId.value = parseInt(pathId as string);
    await getProblemCases(problemId.value)
        .then((list) => { list.forEach(e => e.maxMemory /= 1024 * 1024); return list } )
        .then((list) => { cases.length = 0; return list } )
        .then(list => list.forEach(e => cases.push(e)));
});

const handleSubmit = () => {
    updateProblemCases(cases).then(() => {
        Message.success("提交成功");
        router.go(-1);
    });
}

const removeCase = (idx: number) => {
    cases.splice(idx, 1);
    Message.info('已移除一个样例');
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
                <a-typography-title :heading="3" style="display: inline-block;">管理样例</a-typography-title>
            </a-space>

            <a-divider/>

            <div style="margin: 20px 20px">
                <a-card v-for="idx of cases.length" :title="'样例' + idx" style="margin-bottom: 20px">
                    <template #extra>
                        <a-button status="danger" @click="removeCase(idx - 1)">移除</a-button>
                    </template>
                    <a-form :model="cases[idx - 1]">
                        <a-row>
                            <a-col :span="12">
                                <a-form-item label="时间限制 (ms)" style="width: 50%" label-col-flex="100px">
                                    <a-input v-model="cases[idx - 1].maxTimeMillis"></a-input>
                                </a-form-item>
                            </a-col>
                            <a-col :span="12">
                                <a-form-item label="空间限制 (MB)" style="width: 50%" label-col-flex="100px">
                                    <a-input v-model="cases[idx - 1].maxMemory"></a-input>
                                </a-form-item>
                            </a-col>
                        </a-row>
                        <a-row justity="space-between">
                            <a-col :span="12">
                                <a-form-item label="输入数据" label-col-flex="100px">
                                    <a-textarea v-model="cases[idx - 1].input" :auto-size="{ maxRows: 5 }">
                                    </a-textarea>
                                </a-form-item>
                            </a-col>
                            <a-col :span="12">
                                <a-form-item label="输出数据" label-col-flex="100px">
                                    <a-textarea v-model="cases[idx - 1].output" :auto-size="{ maxRows: 5 }">
                                    </a-textarea>
                                </a-form-item>
                            </a-col>
                        </a-row>

                    </a-form>
                    <br>
                </a-card>

                <div style="margin: auto; width: 18%">
                    <a-button type="secondary" shape="round" size="large"
                            @click="cases.push(new ProblemCaseImpl())"
                            style="display: inline-block; margin-right: 10px">
                        <template #icon>
                            <IconPlus/>
                        </template>
                        添加样例
                    </a-button>
                    <a-button type="primary" shape="round" size="large"
                              @click="handleSubmit"
                              style="display: inline-block">
                        提交
                    </a-button>
                </div>
            </div>

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