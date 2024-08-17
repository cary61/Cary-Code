import { useAuthStore } from '@/stores/auth';
import { createPinia, setActivePinia } from 'pinia';
import { beforeEach, expect, test } from 'vitest';

beforeEach(() => {
    setActivePinia(createPinia());
})

test('auth store: token', () => {
    const auth = useAuthStore();
    expect(auth.token).toBe('');

    const newToken = 'abcdefg';
    auth.setToken(newToken);
    expect(auth.token).toBe(newToken);
});

test('auth store: userInfo', () => {
    const auth = useAuthStore();
    const userInfo = auth.userInfo;
    expect(userInfo.id).toBe(0);
    auth.setUserInfo({
        id: 0,
        name: 'Cary',
        nickname: '',
        avatar: '',
        signature: '',
        authority: 'USER'
    });
    expect(userInfo.name).toBe('Cary');
});