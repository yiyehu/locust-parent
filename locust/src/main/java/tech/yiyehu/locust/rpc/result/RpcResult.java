package tech.yiyehu.locust.rpc.result;

import tech.yiyehu.locust.rpc.Result;

public class RpcResult implements Result {
    private Object value;
    private Throwable throwable;

    public RpcResult(Object value) {
        this.value = value;
    }

    public RpcResult(Throwable throwable) {
        this.throwable = throwable;
    }

    public RpcResult(Object value, Throwable throwable) {
        this.value = value;
        this.throwable = throwable;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Throwable getException() {
        return throwable;
    }

    @Override
    public void setException(Throwable t) {
        this.throwable = throwable;
    }

    @Override
    public boolean hasException() {
        return throwable == null;
    }

    @Override
    public Object get() {
        if(throwable !=null){
            return throwable;
        }else {
            return value;
        }
    }
}
