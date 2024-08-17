export default interface Result<T> {
    ok: boolean;
    msg: string | null,
    data: T
}