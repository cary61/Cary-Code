import axios from 'axios';

const ossSocket = import.meta.env.ENV_OSS_SOCKET;

const oss = axios.create({
    baseURL: ossSocket
});

export default {
    get: oss.get,
    post: oss.post,
    put: oss.put,
    delete: oss.delete,
    patch: oss.patch,
    image: (path: string) => `${ossSocket}/image/${path}`
};