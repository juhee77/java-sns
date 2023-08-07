package com.lahee.mutsasns.service;

import com.lahee.mutsasns.domain.File;
import com.lahee.mutsasns.domain.FolderType;
import com.lahee.mutsasns.exception.CustomException;
import com.lahee.mutsasns.exception.ErrorCode;
import com.lahee.mutsasns.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {
    private final FileRepository fileRepository;

    @Transactional
    public File saveOneFile(FolderType folderType, Long id, MultipartFile image) {
        String folderName = folderType.getName();
        // 폴더를 만든다.
        String profileDir = String.format("media/%s/%s/", folderName, id);
        try {
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String originalFilename = image.getOriginalFilename();
        assert originalFilename != null;
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String uuid = UUID.randomUUID().toString();
        String storeFileName = uuid + "." + extension;
        String profilePath = profileDir + storeFileName;

        try {
            image.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("파일 업로드 성공");
        return fileRepository.save(new File("caption", String.format("/static/%s/%d/%s", folderName, id, storeFileName), originalFilename));
    }

    public List<File> saveMultiFile(FolderType folderType, Long id, List<MultipartFile> images) {
        String folderName = folderType.getName();

        // 폴더를 만든다.
        String profileDir = String.format("media/%s/%s/", folderName, id);
        try {
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<File> files = new ArrayList<>();
        int index = 1;
        for (MultipartFile image : images) {
            String originalFilename = image.getOriginalFilename();
            assert originalFilename != null;
            String[] fileNameSplit = originalFilename.split("\\.");
            String extension = fileNameSplit[fileNameSplit.length - 1];
            String uuid = UUID.randomUUID().toString();
            String storeFileName = uuid + "." + extension;
            String profilePath = profileDir + storeFileName;

            try {
                image.transferTo(Path.of(profilePath));
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.info("파일 업로드 성공");
            files.add(fileRepository.save(new File("caption", String.format("/static/%s/%d/%s", folderName, id, storeFileName), originalFilename)));
        }

        return files;
    }

    @Transactional
    public void dropFile(File file) {
        String storePath = file.getStorePath();
        log.info(storePath);

        cleanUp(storePath);
        fileRepository.delete(file);
    }

    public File getFile(Long id) {
        Optional<File> file = fileRepository.findById(id);
        if (file.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND_EXCEPTION);
        }
        return file.get();
    }

    public void cleanUp(String folder) {
        log.info("{} 폴더 삭제에 들어옴", folder);
        //static 삭제 , media 넣기
        String currentWorkingDirectory = System.getProperty("user.dir");
        String name = "/media";
        String substring = currentWorkingDirectory + name + folder.substring(7);
        java.io.File file = new java.io.File(substring);
        if (file.isFile()) {
            deleteFolder(substring);
        }
    }

    public static void deleteFolder(String path) {
        log.info("{} 폴더 삭제", path.toString());

        java.io.File folder = new java.io.File(path);
        try {
            if (folder.exists()) {
                log.info("해당 파일은 존재합니다.");
                java.io.File[] folder_list = folder.listFiles(); //파일리스트

                if (folder_list != null) { //폴더인 경우
                    for (int i = 0; i < folder_list.length; i++) {
                        log.info("{} 파일 삭제", folder_list[i].getName());
                        if (folder_list[i].isFile()) {
                            folder_list[i].delete();
                        }
                    }
                }
                folder.delete();
                log.info("폴더까지 삭제 완료 했슴다");
            } else {
                log.info("그런 파일경로는 없음다..");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
