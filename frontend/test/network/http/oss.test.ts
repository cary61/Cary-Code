import oss from "@/network/http/oss";
import { expect, test } from "vitest";

const ossSocket = import.meta.env.ENV_OSS_SOCKET;

test('oss image', () => {
    expect(oss.image('aaa')).toBe(`${ossSocket}/image/aaa`);
});