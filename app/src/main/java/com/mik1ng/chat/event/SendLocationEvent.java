package com.mik1ng.chat.event;

public class SendLocationEvent {
    private Object[] result;

    public SendLocationEvent() {
    }

    public SendLocationEvent(Object[] result) {
        this.result = result;
    }

    public Object[] getResult() {
        return result;
    }

    public void setResult(Object[] result) {
        this.result = result;
    }
}
