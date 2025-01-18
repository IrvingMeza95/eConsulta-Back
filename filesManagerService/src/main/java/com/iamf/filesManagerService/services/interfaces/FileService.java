package com.iamf.filesManagerService.services.interfaces;

import com.iamf.commons.exceptions.MyException;
import com.iamf.filesCommons.models.File;
import com.iamf.filesCommons.responses.ResponseFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FileService {
    File store(MultipartFile file, String idUsuario, String tipoDeArchivo) throws IOException, MyException;
    File getFile1(String id) throws FileNotFoundException;
    File getFile2(String param,String tipo) ;
    List<ResponseFile> getFiles(List<String> filesIds);
}
