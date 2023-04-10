package com.mik1ng.chat.event;

public class SendChatRecordEvent {
    private String record;

    public SendChatRecordEvent(String record) {
        this.record = record;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
