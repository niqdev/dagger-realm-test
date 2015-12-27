package com.github.niqdev.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import static java.util.Objects.requireNonNull;

public class MessageModel extends RealmObject {

    @PrimaryKey
    private String uuid;

    @Required
    private String content;

    private String info;

    public MessageModel() {}

    public MessageModel(Builder builder) {
        this.uuid = builder.uuid;
        this.content = builder.content;
        this.info = builder.info;
    }

    public static class Builder {

        private final String uuid;
        private String content;
        private String info;

        public Builder() {
            this.uuid = UUID.randomUUID().toString();
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder info(String info) {
            this.info = info;
            return this;
        }

        public MessageModel build() {
            requireNonNull(content, "content is null");

            return new MessageModel(this);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
