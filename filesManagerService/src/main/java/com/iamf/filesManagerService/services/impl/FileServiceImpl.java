package com.iamf.filesManagerService.services.impl;

import com.iamf.commons.dtos.ConsultaDTO;
import com.iamf.commons.exceptions.MyFunctionalExceptionHandler;
import com.iamf.filesCommons.enums.TipoDeArchivo;
import com.iamf.commons.exceptions.MyException;
import com.iamf.filesCommons.mappers.ResoonseFileMapper;
import com.iamf.filesCommons.models.File;
import com.iamf.filesCommons.responses.ResponseFile;
import com.iamf.filesManagerService.clientes.ServicioConsultas;
import com.iamf.filesManagerService.clientes.ServicioUsuarios;
import com.iamf.filesManagerService.repositories.FileRepo;
import com.iamf.filesManagerService.services.interfaces.FileService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
    @Autowired
    private ServicioConsultas servicioConsultas;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public File store(MultipartFile file, String idUsuario, String tipoDeArchivo, Long idConsulta) throws IOException, MyException {
        if (idUsuario == null || tipoDeArchivo == null)
            throw new IOException("Es necesario adjuntar el ID del usuario y especificar el tipo de archivo.");

        if (!TipoDeArchivo.validarTipoDeArchivo(tipoDeArchivo))
            throw new MyException("El tipo de archivo " + tipoDeArchivo + " no es válido.");

        if (tipoDeArchivo.equals(TipoDeArchivo.RECIBO.name()) && idConsulta == null)
            throw new MyException("Para guardar el recibo es necesario espeficiar el ID de la consulta.");
        if (tipoDeArchivo.equals(TipoDeArchivo.FACTURA.name()) && idConsulta == null)
            throw new MyException("Para guardar la factura es necesario espeficiar el ID de la consulta.");

        if (idConsulta == null && !tipoDeArchivo.equals(TipoDeArchivo.PROFILE_PICTURE.name())){
            String[] partes = tipoDeArchivo.split("-");
            Long idCon = Long.valueOf(partes[1]);
            log.info("Validando si la consulta con id " + idCon + " existe.");
            ConsultaDTO consulta = null;
            try {
                consulta = servicioConsultas.getConsulta(idCon);
            }catch (RuntimeException e){
                log.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            if (!idUsuario.equals(consulta.getPaciente().getCredenciales().getEmail()))
                throw new MyException("El paciente con el email " + idUsuario + " no tiene relacion con la consulta con el id " + idCon + ".");
        }else if (!tipoDeArchivo.equals(TipoDeArchivo.PROFILE_PICTURE.name())){
            log.info("Validando si la consulta con id " + idConsulta + " existe.");
            ConsultaDTO consulta = null;
            try {
                consulta = servicioConsultas.getConsulta(idConsulta);
            }catch (RuntimeException e){
                log.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            if (!idUsuario.equals(consulta.getPaciente().getCredenciales().getEmail()))
                throw new MyException("El paciente con el email " + idUsuario + " no tiene relacion con la consulta con el id " + idConsulta + ".");
            if (!consulta.getPagado())
                throw new MyException("No es posible generar un recibo o factura ya que la consulta" +
                        " con el id " + idConsulta + " no figura como pagada en el sistema.");
        }

        File response = null;
        if (idConsulta == null){
            response = getFile2(idUsuario,tipoDeArchivo);
        }else{
            String tipoReal = tipoDeArchivo + "-" + idConsulta;
            response = getFile2(idUsuario,tipoReal);
        }

        if (response == null || response.getId().equals("")){
            log.info("Se subira un nuevo archivo.");
            return newFile(file,idUsuario,tipoDeArchivo,idConsulta);
        }else{
            log.info("El archivo tipo " + tipoDeArchivo + " se reescribira en el id " + response.getId() + ".");
            return updateFile(file, response.getId());
        }
    }

    private File updateFile(MultipartFile file, String id) throws IOException {
        File archivoActual = getFile1(id);
        archivoActual.setData(file.getBytes());
        archivoActual.setType(file.getContentType());
        return fileRepo.save(archivoActual);
    }

    public File newFile(MultipartFile file, String idUsuario, String tipoDeArchivo, Long idConsulta) throws IOException {
        File fileEntity = File.builder()
                .type(file.getContentType())
                .data(file.getBytes())
                .build();

        if (tipoDeArchivo.equals(TipoDeArchivo.PROFILE_PICTURE.name())){
            fileEntity.setName(tipoDeArchivo);
        }else{
            fileEntity.setName(tipoDeArchivo + "-" + idConsulta);
        }

        File savedFile = fileRepo.save(fileEntity);

        //Metodo para evitar que se suban archivos sin asociar a un usuario
        MyFunctionalExceptionHandler.handleException(() -> servicioUsuarios.agregarArchivo(idUsuario,savedFile.getId()),
                ex -> {
                    if (savedFile.getId() != null){
                        log.info("Archivo guardado sin asociar.");
                        fileRepo.deleteById(savedFile.getId());
                        log.info("Se elimino el archivo con el id: " + savedFile.getId());
                        throw new RuntimeException("Error al asociar el archivo con el usuario.");
                    }else{
                        log.info("No se guardo el archivo.");
                    }
                });
        return savedFile;
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
    public List<ResponseFile> getFiles(String emailUsuario, String tipo) throws MyException {
        log.info("Extrayerndo lista de archivos del usuario con el email " + emailUsuario);
        List<String> filesIds = new ArrayList<>();
        if (tipo == null){
            filesIds = fileRepo.getAllFilesIdsDePersona(emailUsuario).stream().map(i -> {
                return String.valueOf(i[0]);
            }).toList();
        }else{
            if (!TipoDeArchivo.validarTipoDeArchivo(tipo))
                throw new MyException("El tipo " + tipo + " no es valido.");
            log.info("Buscando archivos de tipo " + tipo);
            filesIds = fileRepo.getFilesIdsPorTipo(emailUsuario,tipo).stream().map(i -> {
                return String.valueOf(i[0]);
            }).toList();
        }

        List<File> files = filesIds.stream().flatMap(fileId -> {
            log.info("Archivo id: " + fileId);
            Optional<File> file = fileRepo.findById(fileId);
            return file.stream().flatMap(Stream::of);
        }).collect(Collectors.toList());
        return resoonseFileMapper.getResponseFileList(files);
    }

}