package com.lahee.mutsasns.domain;

public enum FolderType {
    USER("user"),POST_THUMB("post-thumb"),POST("post"), COMMENT("comment");

    private String name;

    FolderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
