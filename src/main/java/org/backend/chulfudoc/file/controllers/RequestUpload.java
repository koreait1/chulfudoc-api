package org.backend.chulfudoc.file.controllers;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestUpload {

    private String gid; // 반드시 사용
    private String location; // 프로필 이미지만 올리는경우 사용X
    private MultipartFile[] files; // 여러 형식을 동시에 받기 위해 정의
    private boolean single; // 1개의 파일만 기존꺼는 삭제하고 대체하는 형태로 동작
    private boolean imageOnly;

}
