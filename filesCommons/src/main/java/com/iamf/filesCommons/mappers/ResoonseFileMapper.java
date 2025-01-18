package com.iamf.filesCommons.mappers;

import com.iamf.filesCommons.models.File;
import com.iamf.filesCommons.responses.ResponseFile;

import java.util.List;
import java.util.stream.Collectors;

public class ResoonseFileMapper {

    public File getFile(ResponseFile responseFile){
        File file = File.builder()
        		.id(responseFile.getId())
                .build();
        return file;
    }

    public List<File> getFileList(List<ResponseFile> responseFiles){
        return responseFiles.stream().map(this::getFile).collect(Collectors.toList());
    }

    public ResponseFile getResponseFile(File file){
        return ResponseFile.builder()
                .id(file.getId())
                .name(file.getName())
                .url("/api/files/files/" + file.getId())
                .type(file.getType())
                .size(file.getData().length)
                .build();
    }

    public ResponseFile getResponseFile(String file){
        return ResponseFile.builder()
                .id(file)
                .url("/api/files/files/" + file)
                .build();
    }

    public List<ResponseFile> getResponseFileList(List<File> files){
        return files.stream().map(this::getResponseFile).collect(Collectors.toList());
    }

    public List<ResponseFile> getResponseFileList2(List<String> files){
        return files.stream().map(this::getResponseFile).collect(Collectors.toList());
    }

}
