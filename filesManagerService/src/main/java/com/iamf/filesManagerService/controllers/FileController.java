package com.iamf.filesManagerService.controllers;

import com.iamf.filesCommons.enums.TipoDeArchivo;
import com.iamf.commons.exceptions.MyException;
import com.iamf.filesCommons.mappers.ResoonseFileMapper;
import com.iamf.filesCommons.models.File;
import com.iamf.filesCommons.responses.ResponseFile;
import com.iamf.filesManagerService.services.interfaces.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;
    private ResoonseFileMapper resoonseFileMapper = new ResoonseFileMapper();

    @PostMapping
    public ResponseEntity<ResponseFile> subir(@RequestParam("file") MultipartFile file, @RequestParam("idUsuario") String idUsuario,
                                              @RequestParam("tipo") String tipo) throws IOException, MyException {
        if (file.getContentType() == null || tipo.isEmpty()){
            throw new IOException();
        }
        File newFile = fileService.store(file, idUsuario, tipo);
        return ResponseEntity.status(HttpStatus.OK).body(resoonseFileMapper.getResponseFile(newFile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> verArchivo1(@PathVariable String id) throws FileNotFoundException {
        File file = fileService.getFile1(id);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, file.getType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

    @GetMapping("/{param}/{tipo}")
    public ResponseEntity<byte[]> verArchivo2(@PathVariable String param, @PathVariable String tipo) throws MyException {
        File file = fileService.getFile2(param,tipo);
        if (file == null)
            throw new MyException("File not found.");
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, file.getType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }

    @GetMapping("/files-paths")
    public ResponseEntity<List<ResponseFile>> listarArchivos(@RequestParam List<String> filesIds){
        List<ResponseFile> files = fileService.getFiles(filesIds);
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<TipoDeArchivo>> tiposDeArchivos(){
        return ResponseEntity.ok(List.of(TipoDeArchivo.values()));
    }

}
