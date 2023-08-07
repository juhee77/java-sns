package com.lahee.mutsasns.domain;

public enum FriendshipStatus {
    PENDING("보류"), ACCEPTED("수락"), REJECTED("거절");

    private String name;

    FriendshipStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
