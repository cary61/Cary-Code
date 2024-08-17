<template>
    <div class="container">
        <!-- <Breadcrumb :items="['题单']" /> -->
        <a-card class="general-card" title="题单">
            <a-form :model="selectors" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 16 }" label-align="left">
                <a-row :gutter="16">
                    <a-col :span="4">
                        <a-form-item field="filterType" label="难度">
                            <a-select v-model="selectors.difficulty" :options="difficultyOptions"
                                placeholder="选择难度" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="4">
                        <a-form-item field="filterType" label="状态">
                            <a-select v-model="selectors.submissionState" :options="submissionStateOptions" @change="submissionStateOptionChange"
                                placeholder="选择提交状态" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="4">
                        <a-form-item field="status" label="标签">
                            <a-input v-model="selectors.tag"
                                placeholder="输入题目标签" />
                        </a-form-item>
                    </a-col>
                    <a-col :span="4">
                        <a-button @click="resetSelectors">
                            <template #icon>
                                <IconRefresh />
                            </template>
                            重置
                        </a-button>
                    </a-col>
                    <a-col :span="6">
                        <a-input placeholder="搜索题目标题、编号、背景或描述" allow-clear v-model="queryString">
                            <template #prefix>
                                <IconSearch />
                            </template>
                        </a-input>
                    </a-col>
                    <a-col :span="2" @click="handleSearch">
                        <a-button type="primary">
                            <template #icon>
                                <IconSearch />
                            </template>
                            搜索
                        </a-button>
                    </a-col>
                </a-row>
            </a-form>
            
            <a-divider style="margin-top: 0" />

            <a-table :columns="columns" 
                    :data="problems" 
                    :bordered="{cell: true}" 
                    :pagination="false"
                    @row-click="(record: TableData) => $router.push(`/problem/${record.id}`)">
                <template #difficulty="{ record }">
                    <div v-if="record.difficulty == 'EASY'" :class="record.difficulty">
                        简单
                    </div>
                    <div v-if="record.difficulty == 'MIDDLE'" :class="record.difficulty">
                        中等
                    </div>
                    <div v-if="record.difficulty == 'HARD'" :class="record.difficulty">
                        困难
                    </div>
                </template>
                <template #submissionState="{ record }">
                    <div v-if="record.submissionState == 'UNATTEMPTED'" >
                        未开始
                    </div>
                    <div v-if="record.submissionState == 'SOLVED'" >
                        已解决
                    </div>
                    <div v-if="record.submissionState == 'ATTEMPTED'" >
                        尝试过
                    </div>
                </template>
            </a-table>

            <div class="flex-container">
                <a-pagination :total="pagesTotal" 
                        :page-size="1" 
                        v-model:current="pageId"
                        
                />
            </div>
        </a-card>

        <div>

        </div>
    </div>
    <a-affix :offsetBottom="120" style="margin-left: auto; width: 10%">
        <a-button type="primary" size="large" shape="round" @click="$router.push('/problem-edit')">上传题目</a-button>
    </a-affix>
</template>

<script lang="ts" setup>
import {IconRefresh, IconSearch} from '@arco-design/web-vue/es/icon';
import {computed, reactive, ref, watch, watchEffect} from 'vue';
import type {TableColumnData, TableData} from '@arco-design/web-vue/es/table/interface';
import {getPagesTotal, getProblems, searchProblem} from '@/network/api/problem-api';
import type {Difficulty} from '@/types/enum/Difficulty';
import type {SubmissionState} from '@/types/enum/SubmissionState';
import type ProblemDO from '@/types/do/ProblemDO';

import {useRoute, useRouter} from 'vue-router';
import problem from "../../router/routes/modules/problem";
import {useAuthStore} from "@/stores/auth";
import {Message} from "@arco-design/web-vue";

const auth = useAuthStore();
const route = useRoute();
const router = useRouter();

const selectors: {   
    difficulty: Difficulty, 
    submissionState: SubmissionState, 
    tag: string
} = 
reactive({
    difficulty: '',
    submissionState: '',
    tag: ''
});



const resetSelectors = () => {
    selectors.difficulty = '';
    selectors.submissionState = '';
    selectors.tag = '';
}

const pageId = ref(1);

watch(selectors, () => {
    pageId.value = 1;
});

// 捕获URL中的pageId
watchEffect(() => {
    pageId.value = route.params.pageId !== ''
        ? parseInt(route.params.pageId as string)
        : 1;
});

// 反馈修改URL中的pageId
// watchEffect(() => {
//     if (pageId.value) {
//         router.replace(`/problem-list/${pageId.value}`);
//     }
// });



const problemsRaw: ProblemDO[] = reactive([]);
const pagesTotal = ref(1);

const queryString = ref('');

const handleSearch = async () => {
    let data = await searchProblem(queryString.value);
    problemsRaw.length = 0;
    problemsRaw.push(...data);
    console.log(data)
}

const submissionStateOptionChange = () => {
    if (selectors.submissionState != '' && !auth.isLoggedIn) {
        selectors.submissionState = '';
        Message.warning('请先登录再筛选提交状态');
    }
}


watchEffect(async () => {
    const updatedProblems = await getProblems({pageId: pageId.value, ...selectors});
    problemsRaw.length = 0;
    problemsRaw.push(...updatedProblems);
    pagesTotal.value = await getPagesTotal(selectors);
})

const columns: TableColumnData[] = [
    {
        title: "状态",
        dataIndex: "submissionState",
        align: 'center',
        slotName: 'submissionState'
        // width: 100
    },
    {
        title: "题号",
        dataIndex: "number",
        align: 'center'
    },
    {
        title: "题目",
        dataIndex: "title",
        align: 'center'
    },
    {
        title: "难度",
        dataIndex: "difficulty",
        align: 'center',
        slotName: 'difficulty'
    },
];

const problems = computed(() => {
    const list: any[] = [];
    for (let problemRaw of problemsRaw) {
        let problem = {
            id: problemRaw.id,
            number: problemRaw.number,
            title: problemRaw.title,
            difficulty: problemRaw.difficulty,
            submissionState: problemRaw.submissionState
        }
        list.push(problem);
    }
    return list;
});


const difficultyOptions = computed(() => [
    {
        label: "(不限)",
        value: null
    },
    {
        label: '简单',
        value: 'EASY',
    },
    {
        label: '中等',
        value: 'MIDDLE',
    },
    {
        label: '困难',
        value: 'HARD',
    }
]);
const submissionStateOptions = computed(() => [
    {
        label: "(不限)",
        value: null
    },
    {
        label: '未开始',
        value: 'UNATTEMPTED',
    },
    {
        label: '已解决',
        value: 'SOLVED',
    },
    {
        label: '尝试过',
        value: 'ATTEMPTED'
    }
]);


</script>


<style scoped lang="less">
.flex-container {
  display: flex;
  justify-content: center;
}


.container {
    padding: 0 20px 20px 20px;
    width: 80%;
    margin: auto;
}

:deep(.arco-table-th) {
    &:last-child {
        .arco-table-th-item-title {
            margin-left: 16px;
        }
    }
}

.action-icon {
    margin-left: 12px;
    cursor: pointer;
}

.active {
    color: #0960bd;
    background-color: #e3f4fc;
}

.setting {
    display: flex;
    align-items: center;
    width: 200px;

    .title {
        margin-left: 12px;
        cursor: pointer;
    }
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
