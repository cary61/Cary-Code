<script setup lang="ts">
import {ref, reactive, watchEffect} from 'vue';
import type {SubmissionLog} from "@/types/po/SubmissionLog";
import {JudgeStatus} from "@/types/enum/JudgeStatus";
import {getSubmissionCaseLogs, getSubmissionCode, getSubmissionListByProblemId} from "@/network/api/submission-api";
import {useRoute} from "vue-router";
import {getResult} from "@/network/api/judge-api";
import type {SubmissionCaseLog} from "@/types/po/SubmissonCaseLog";
import type {PollingMessage} from "@/types/PollingMessage";
import {IconCaretLeft} from "@arco-design/web-vue/es/icon";
import {TableColumnData, TableData} from "@arco-design/web-vue/es/table/interface";
import {formalizeMemory, parseChinese} from "../../utils/utils";
import type {SubmissionCode} from "@/types/po/SubmissionCode";
const route = useRoute();

const ready = ref(false);
const uuid = ref('');
const submissionLog: SubmissionLog = reactive({
    id: 0,
    uuid: '',
    userId: 0,
    problemId: 0,
    judgeStatus: 'WA',
    timeMillis: 0,
    memory: 0,
    createTime: new Date()
});
const submissionCaseLogs: SubmissionCaseLog[] = reactive([]);
const submissionCode: SubmissionCode = reactive({
    id: 0,
    submissionUuid: '0',
    language: '',
    code: ''
});

watchEffect(async () => {
    uuid.value = route.params.uuid as string;
    let timeIntervalMillis = parseInt(route.query.timeIntervalMillis as string);
    let beginning = new Date().getTime();
    let pollingMessage: PollingMessage<SubmissionLog>;
    let init = true;
    while (!pollingMessage?.ready) {
        if (init || new Date().getTime() - beginning > timeIntervalMillis) {
            beginning = new Date().getTime();
            pollingMessage = await getResult(uuid.value);
            timeIntervalMillis = pollingMessage.timeIntervalMillis;
            init = false;
        }
    }
    Object.assign(submissionLog, pollingMessage.data);
    let caseLogs = await getSubmissionCaseLogs(uuid.value);
    submissionCaseLogs.length = 0;
    for (let caseLog of caseLogs) {
        submissionCaseLogs.push(caseLog);
    }
    submissionCaseLogs.sort((a, b) => a.caseSerial - b.caseSerial)
    let codeData = await getSubmissionCode(uuid.value);
    Object.assign(submissionCode, codeData);
    ready.value = true;
});

const columns: TableColumnData[] = [
    {
        title: "样例序号",
        dataIndex: "caseSerial",
        align: 'center',
    },
    {
        title: "结果",
        dataIndex: "judgeStatus",
        align: 'center',
        slotName: 'judgeStatus'
    },
    {
        title: "时间",
        dataIndex: "timeMillis",
        align: 'center',
        slotName: 'timeMillis'
    },
    {
        title: "内存",
        dataIndex: "memory",
        align: 'center',
        slotName: 'memory'
    },
];

</script>

<template>
    <div id="submissionLog">
        <div style="margin: 0px 20px; padding: 2px 2px">
            <a-space>
                <a-button style="margin-top: 16px;" type="secondary" @click="$router.go(-1)">
                    <template #icon>
                        <icon-caret-left />
                    </template>
                </a-button>
                <a-typography-title :heading="3" style="display: inline-block;">评测详情</a-typography-title>
            </a-space>
            <a-divider style="margin-bottom: 0"/>

            <div v-if="ready">
            <a-row style="margin: 10px 20px; ">
                <a-col :span="14">
                    <a-space>
                        <div
                            :style="{ borderRadius: '50%', width: '20px', height: '20px', backgroundColor: submissionLog.judgeStatus == 'AC' ? 'green' : 'red', marginLeft: '20px', marginTop: '12px', display: 'inline-block' }">
                        </div>
                        <a-typography-title :heading="5" :style="{display: 'inline-block', color: submissionLog.judgeStatus == 'AC' ? 'green' : 'red'}">
                            {{ parseChinese(submissionLog.judgeStatus) }} ({{ submissionLog.judgeStatus }})
                        </a-typography-title>
                    </a-space>
                </a-col>
                <a-col :span="5">
                    <a-typography-title :heading="5" :style="{display: 'inline-block', color: submissionLog.judgeStatus == 'AC' ? 'black' : 'black'}">
                        总用时：{{ submissionLog.timeMillis != null ? submissionLog.timeMillis + 'ms' : 'N/A' }}
                    </a-typography-title>
                </a-col>
                <a-col :span="5">
                    <a-typography-title :heading="5" :style="{display: 'inline-block', color: submissionLog.judgeStatus == 'AC' ? 'black' : 'black'}">
                        总计内存：{{ submissionLog.memory != null ? formalizeMemory(submissionLog.memory) : 'N/A' }}
                    </a-typography-title>
                </a-col>
            </a-row>

            <a-table style="margin: 10px 20px"  :columns="columns"
                                     :data="submissionCaseLogs"
                                     :bordered="{cell: true}"
                                     :pagination="false">
                    <template #judgeStatus="{ record }">
                        <div v-if="record.judgeStatus == 'AC'" :style="{color: 'green'}">
                            {{ parseChinese(record.judgeStatus) }} ({{ record.judgeStatus }})
                        </div>
                        <div v-else :style="{color: 'red'}">
                            {{ parseChinese(record.judgeStatus) }} ({{ record.judgeStatus }})
                        </div>
                    </template>
                    <template #timeMillis="{ record }">
                        <div v-if="record.judgeStatus == 'AC'">
                            {{ record.timeMillis }}ms
                        </div>
                        <div v-else :style="{color: 'red'}">
                            N/A
                        </div>
                    </template>
                    <template #memory="{ record }">
                        <div v-if="record.judgeStatus == 'AC'">
                            {{ formalizeMemory(record.memory) }}ms
                        </div>
                        <div v-else :style="{color: 'red'}">
                            N/A
                        </div>
                    </template>
            </a-table>

            <div style="margin: 10px 20px">
                <a-typography-title :heading="5" style="float: left">
                    代码
                </a-typography-title>
                <a-typography-title :heading="6" style="float: right">
                    语言：{{ submissionCode.language }}
                </a-typography-title>
                <div style="clear: both"></div>
                <a-textarea v-model="submissionCode.code" auto-size></a-textarea>
            </div>
            </div>
            <div v-else>
                <a-empty style="margin-top: 180px; margin-bottom: 20px" />
                <a-typography-title :heading="5" style="margin: auto; width: 8%">评测中......</a-typography-title>

            </div>
        </div>
    </div>
</template>

<style scoped>
#submissionLog {
    margin: 20px 10%;
    background-color: white;
    min-height: 700px;
}
</style>