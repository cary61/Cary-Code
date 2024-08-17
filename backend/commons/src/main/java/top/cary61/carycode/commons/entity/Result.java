package top.cary61.carycode.commons.entity;

import lombok.Data;
import lombok.Getter;

/**
 * @author CaryZheng
 * @since 2024-02-16
 *
 * @param <T>
 */

@Getter
public class Result<T> {

    private final boolean ok;

    private final String msg;

    private final T data;

    // 根据条件得出结果
    public static <T> Result<T> of(boolean b, String msg) {
        return b
                ? Result.ok()
                : Result.fail(msg);
    }

    public static <T> Result<T> of(boolean b) {
        return Result.of(b, null);
    }

    public static <T> Result<T> by(T data, String msg) {
        if (data == null) {
            return Result.fail(msg);
        }
        return Result.ok(data);
    }

    public static <T> Result<T> by(T data) {
        return Result.by(data, null);
    }

    // Success
    public static <T> Result<T> ok(T data) {
        return new Result<>(true, null, data);
    }

    public static <T> Result<T> ok() {
        return Result.ok(null);
    }

    // Fail
    public static <T> Result<T> fail(String msg) {
        return new Result<>(false, msg, (T)null);
    }

    public static <T> Result<T> fail() {
        return Result.fail(null);
    }

    private Result(boolean ok, String msg, T data) {
        this.ok = ok;
        this.msg = msg;
        this.data = data;
    }
}
