package tech.yiyehu.locust.rpc.exception;

public class RpcException extends Exception {
    String message;
    public RpcException(String message, Throwable e) {
        super(e);
        this.message = message;
    }
}
