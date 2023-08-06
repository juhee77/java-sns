package com.lahee.mutsasns.domain;

public enum FolderType {
    USER("user"),POST("post"), COMMENT("comment");

    private String name;

    FolderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
