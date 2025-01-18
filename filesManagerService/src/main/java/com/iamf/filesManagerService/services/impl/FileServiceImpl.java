package com.iamf.filesManagerService.services.impl;

import com.iamf.commons.exceptions.MyFunctionalExceptionHandler;
import com.iamf.filesCommons.enums.TipoDeArchivo;
import com.iamf.commons.exceptions.MyException;
import com.iamf.filesCommons.mappers.ResoonseFileMapper;
import com.iamf.filesCommons.models.File;
import com.iamf.filesCommons.responses.ResponseFile;
import com.iamf.filesManagerService.clientes.ServicioUsuarios;
import com.iamf.filesManagerService.repositories.FileRepo;
import com.iamf.filesManagerService.services.interfaces.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepo fileRepo;
    @Autowired
    private ServicioUsuarios servicioUsuarios;
    private final ResoonseFileMapper  resoonseFileMapper = new ResoonseFileMapper();

    @Override
    public File store(MultipartFile file, String idUsuario, String tipoDeArchivo) throws IOException, MyException {
        if (idUsuario == null || tipoDeArchivo == null)
            throw new IOException("Error al subir el archivo.");

        if (!TipoDeArchivo.validarTipoDeArchivo(tipoDeArchivo))
            throw new MyException("El tipo de archivo " + tipoDeArchivo + " no es válido.");

        File response = getFile2(idUsuario,tipoDeArchivo);

        if (response == null || response.getId().equals("")){
            return newFile(file,idUsuario,tipoDeArchivo);
        }else{
            log.info("El archivo tipo " + tipoDeArchivo + " se reescribira en el id " + response.getId() + ".");
            return updateFile(file, response.getId());
        }
    }

    private File updateFile(MultipartFile file, String id) throws IOException {
        File archivoActual = getFile1(id);
        archivoActual.setData(file.getBytes());
        return fileRepo.save(archivoActual);
    }

    public File newFile(MultipartFile file, String idUsuario, String tipoDeArchivo) throws IOException {
        File fileEntity = File.builder()
                .name(tipoDeArchivo)
                .type(file.getContentType())
                .data(file.getBytes())
                .build();

        File savedFule = fileRepo.save(fileEntity);

        //Metodo para evitar que se suban archivos sin asociar a un usuario
        MyFunctionalExceptionHandler.handleException(() -> servicioUsuarios.agregarArchivo(idUsuario,savedFule.getId()),
                ex -> {
                    if (savedFule.getId() != null){
                        log.info("Archivo guardado sin asociar.");
                        fileRepo.deleteById(savedFule.getId());
                        log.info("Se elimino el archivo con el id: " + savedFule.getId());
                        throw new RuntimeException("Error al asociar el archivo con el usuario.");
                    }else{
                        log.info("No se guardo el archivo.");
                    }
                });
        return savedFule;
    }

    @Override
    public File getFile1(String id) throws FileNotFoundException {
        Optional<File> file = fileRepo.findById(id);
        if (file.isPresent()){
            return file.get();
        }
        throw new FileNotFoundException();
    }

    @Override
    public File getFile2(String param, String tipo) {
        log.info("Buscando archivo tipo " + tipo + " del usuario " + param);
        List<Object[]> ressponse = fileRepo.getFile(param,tipo);
        if (ressponse.isEmpty())
            return null;
        Optional<File> file = fileRepo.findById(String.valueOf(ressponse.get(0)[0]));
        return file.get();
    }

    @Override
    public List<ResponseFile> getFiles(List<String> filesIds) {
        log.info("Extrayerndo lista de archivos a partir de lista de ids.");
        List<File> files = filesIds.stream().flatMap(fileId -> {
            log.info("Archivo id: " + fileId);
            Optional<File> file = fileRepo.findById(fileId);
            return file.stream().flatMap(Stream::of);
        }).collect(Collectors.toList());
        return resoonseFileMapper.getResponseFileList(files);
    }

}