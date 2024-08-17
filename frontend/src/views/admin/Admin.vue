<script setup lang="ts">
import { ref, reactive, watchEffect } from 'vue';
import { SubmissionLog } from "@/types/po/SubmissionLog";
import { getSubmissionListByProblemId } from "@/network/api/submission-api";
import {parseChinese, formalizeTime, formalizeMemory, toChinese} from '@/utils/utils';
import { IconCaretLeft, IconSearch } from '@arco-design/web-vue/es/icon';
import { useRoute } from 'vue-router';
import type ProblemDO from "@/types/do/ProblemDO";
import type UserInfo from "@/types/po/UserInfo";
import type UserDO from "@/types/do/UserDO";
import {searchProblem} from "@/network/api/problem-api";
import {searchUser} from "@/network/api/admin-api";
import {TableData} from "@arco-design/web-vue/es/table/interface";
const route = useRoute();

const topic = ref('USER');
const queryString = ref('');

const list: (ProblemDO | UserDO) [] = reactive([]);

const search = async () => {
    if (topic.value == 'USER') {
        let data = await searchUser(queryString.value);
        list.length = 0;
        list.push(...data);
    } else if (topic.value == 'PROBLEM') {
        let data = await searchProblem(queryString.value);
        list.length = 0;
        list.push(...data);
    } else {
        console.log("topic error");
    }
}

const userColumns = [
    {
        title: 'ID',
        dataIndex: 'id',
        align: 'center'
    },
    {
        title: '用户名',
        dataIndex: 'name',
        align: 'center'
    },
    {
        title: '昵称',
        dataIndex: 'nickname',
        align: 'center'
    },
    {
        title: '签名',
        dataIndex: 'signature',
        align: 'center'
    },
    {
        title: '权限',
        dataIndex: 'authority',
        align: 'center',
        slotName: 'authority'
    },
    {
        title: '注册时间',
        dataIndex: 'registerTime',
        align: 'center',
        slotName: 'registerTime'
    },
    {
        title: '封禁至',
        dataIndex: 'bannedUntil',
        align: 'center',
        slotName: 'bannedUntil'
    },
    {
        title: '封禁次数',
        dataIndex: 'banCount',
        align: 'center'
    },
];

const problemColumns = [
    {
        title: 'ID',
        dataIndex: 'id',
        align: 'center'
    },
    {
        title: '编号',
        dataIndex: 'number',
        align: 'center'
    },
    {
        title: '标题',
        dataIndex: 'title',
        align: 'center'
    },
]


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
                <a-typography-title :heading="3" style="display: inline-block;">管理员系统</a-typography-title>

            </a-space>
            <a-divider />

            <a-form size="large" @submit="search" style="max-width: 1200px">
                <a-row>
                    <a-col :span="3">
                        <a-form-item >
                            <a-radio-group v-model="topic" type="button" size="large" :style="{width:'110px', display: 'inlineBlock'}"
                                    @change="list.length=0">
                                <a-radio value="USER">用户</a-radio>
                                <a-radio value="PROBLEM">题目</a-radio>
                            </a-radio-group>
                        </a-form-item>
                    </a-col>
                    <a-col :span="4">
                        <a-form-item>
                            <a-space>
                                <a-input v-model="queryString" placeholder="请输入" :style="{width: '320px'}" allow-clear/>
                                <a-button html-type="submit" type="primary">搜索</a-button>
                            </a-space>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>

            <div v-if="list.length == 0">
                <a-empty style="margin-top: 180px; margin-bottom: 20px" />
            </div>
            <div v-else-if="topic == 'USER'">
                <a-table :columns="userColumns" :data="list">
                    <template #authority="{ record }">
                        {{ toChinese(record.authority) }}
                    </template>
                    <template #registerTime="{ record }">
                        {{ formalizeTime(record.registerTime.toString()) }}
                    </template>
                    <template #bannedUntil="{ record }">
                        <div v-if="record.bannedUntil != null">
                            {{ formalizeTime(record.bannedUntil.toString()) }}
                        </div>
                    </template>
                </a-table>
            </div>
            <div v-else-if="topic == 'PROBLEM'">
                <a-table :columns="problemColumns" :data="list" @row-click="(record: TableData) => $router.push(`/problem/${record.id}`)">
                </a-table>
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