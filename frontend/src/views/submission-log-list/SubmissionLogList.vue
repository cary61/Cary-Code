<script setup lang="ts">
import {ref, reactive, watchEffect, computed, ComputedRef} from 'vue';
import { SubmissionLog } from "@/types/po/SubmissionLog";
import { getSubmissionListByProblemId } from "@/network/api/submission-api";
import { parseChinese, formalizeTime, formalizeMemory } from '@/utils/utils';
import { IconCaretLeft, IconSearch } from '@arco-design/web-vue/es/icon';
import { useRoute } from 'vue-router';
const route = useRoute();

const problemId = ref(0);
const submissionLogs: SubmissionLog[] = reactive([]);

watchEffect(async () => {
    problemId.value = parseInt(route.params.problemId as string);
    let data = await getSubmissionListByProblemId(problemId.value);
    submissionLogs.length = 0;
    submissionLogs.push(...data);
    submissionLogs.sort((a, b) => a.createTime > b.createTime ? -1 : 1)
});

const makeTitle = (submissionLog: SubmissionLog): string => {
    return `${parseChinese(submissionLog.judgeStatus)} (${submissionLog.judgeStatus}) - ${formalizeTime(submissionLog.createTime.toString())}`;
}
const makeDescription = (submissionLog: SubmissionLog): string => {
    if (submissionLog.judgeStatus != 'AC') {
        return 'N/A'
    }
    return `${submissionLog.timeMillis}ms , ${formalizeMemory(submissionLog.memory)}`;
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
                <a-typography-title :heading="3" style="display: inline-block;">提交记录</a-typography-title>
            </a-space>
            <div v-if="submissionLogs.length == 0">
                <a-divider />
                <a-empty style="margin-top: 180px;" />
            </div>
            <div v-else>
                <a-list>
                    <a-list-item v-for="submissionLog of submissionLogs" :key="submissionLog.createTime">
                        <a-list-item-meta :title="makeTitle(submissionLog)"
                            :description="makeDescription(submissionLog)">
                            <template #avatar>
                                <div
                                    :style="{ borderRadius: '50%', width: '12px', height: '12px', backgroundColor: submissionLog.judgeStatus == 'AC' ? 'green' : 'red' }">
                                </div>
                            </template>
                        </a-list-item-meta>
                        <template #actions>
                            <a-button style="margin-top: 16px;" type="secondary" @click="$router.push(`/submission-log/${submissionLog.uuid}`)">
                                <template #icon>
                                    <icon-search />
                                </template>
                                查看详细
                            </a-button>
                        </template>
                    </a-list-item>
                </a-list>
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