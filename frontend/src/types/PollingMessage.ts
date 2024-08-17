export interface PollingMessage <T> {
    uuid: string;
    ready: boolean;
    timeIntervalMillis: number;
    data: T
}

