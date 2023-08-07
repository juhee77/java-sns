package com.lahee.mutsasns.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Getter
@Setter
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption; //대체 문자
    private String storePath;    //사진을 전달받아서 서버의 특정 폴더에 저장할 것이므로 사진이 저장된 경로를 저장
    private String originName;

    public File(String caption, String storeName, String originName) {
        this.caption = caption;
        this.storePath = storeName;
        this.originName = originName;
    }
}