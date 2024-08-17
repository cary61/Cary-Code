import type { JudgeStatus } from "@/types/enum/JudgeStatus";
import type {Authority} from "@/types/enum/Authority";

export function formalizeTimeMillis(timeMillis: number): string {
    return `${ (timeMillis / 1000).toFixed(2) }s`;
}

export function formalizeMemory(memory: number): string {
    let scales = [
        1,
        1024,
        1024 * 1024,
        1024 * 1024 * 1024,
        1024 * 1024 * 1024 * 1024,
    ];
    let levels = [
        '',
        'B',
        'KB',
        "MB",
        "GB"
    ];
    for (let i = 1; i < scales.length; i++) {
        if (memory <= scales[i]) {
            return `${ (memory / scales[i - 1]).toFixed(2) }${levels[i]}`;
        }
    }
    return 'UNLIMITED';
}

export function parseChinese(judgeStatus: JudgeStatus) {
    switch (judgeStatus) {
        case 'AC': return '通过';
        case 'WA': return '答案错误';
        case 'CE': return '编译错误'
        case 'RE': return '运行时异常';
        case 'TLE': return '超时';
        case 'MLE': return '内存超限';
        case 'SE': return '评测异常';
    }
}

export function formalizeTime(ISODatetime: string) {
    return ISODatetime.substring(0, 10) + ' ' + ISODatetime.substring(11);
}

export function toChinese(authority: Authority) {
    if (authority == 'USER') {
        return '用户';
    }
    if (authority == 'ADMINISTRATOR') {
        return '管理员';
    }
    return '';
}