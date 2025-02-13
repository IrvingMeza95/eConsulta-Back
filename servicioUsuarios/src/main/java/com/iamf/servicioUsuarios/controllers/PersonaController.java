package com.iamf.servicioUsuarios.controllers;

import com.iamf.commons.dtos.RequestDTO;
import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.enums.TiposDePlantillas;
import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.mappers.PersonaMapper;
import com.iamf.commons.models.Medico;
import com.iamf.commons.models.Paciente;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioUsuarios.clientes.ServicioVerificacion;
import com.iamf.servicioUsuarios.dtos.RegistroDTO;
import com.iamf.servicioUsuarios.services.interfaces.MedicoService;
import com.iamf.servicioUsuarios.services.interfaces.PacienteService;
import com.iamf.servicioUsuarios.services.interfaces.PersonaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/persona")
@Slf4j
public class PersonaController {

    @Autowired
    private PersonaService personaService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private MedicoService medicoService;
    @Autowired
    private ServicioVerificacion servicioVerificacion;
    private final PersonaMapper personaMapper = new PersonaMapper();

    @GetMapping("/{param}")
    public ResponseEntity<PersonaDTO> getPersona(@PathVariable String param) throws MyException {
        log.info("Param: " + param);
        TipoPersona tipoPersona = personaService.getTipoPersona(param);
        PersonaDTO personaDTO = null;
        if (tipoPersona.equals(TipoPersona.MEDICO)){
            log.info("Buscando persona tipo MEDICO.");
            Medico medico = medicoService.getPersona(param);
            personaDTO = personaMapper.getMedicoDTO(medico);
//            if (medico.getConsultas() != null)
//                personaDTO.setConsultas(consultaMapper.getConsultas(medico.getConsultas()));
//            if (medico.getArchivos() != null){
//                log.info("Cargando lista de archivos para medico.");
//                personaDTO.setArchivos(
//                        (List<ResponseFile>) Optional.ofNullable(medico.getArchivos())
//                                .map(archivos -> {
//                                    try {
//                                        return filesManagerService.listarArchivos(archivos);
//                                    } catch (Exception e) {
//                                        log.error("Gestor de archivos no disponible.", e);
//                                        return Collections.emptyList();
//                                    }
//                                })
//                                .orElse(Collections.emptyList())
//                );
//            }
            return ResponseEntity.ok(personaDTO);
        }else if (tipoPersona.equals(TipoPersona.PACIENTE)){
            log.info("Buscando persona tipo PACIENTE    .");
            Paciente paciente = pacienteService.getPersona(param);
            personaDTO = personaMapper.getPacienteDTO(paciente);
//            if (paciente.getConsultas() != null)
//                personaDTO.setConsultas(consultaMapper.getConsultas(paciente.getConsultas()));
//            if (paciente.getArchivos() != null){
//                log.info("Cargando lista de archivos para paciente.");
//                personaDTO.setArchivos(
//                        (List<ResponseFile>) Optional.ofNullable(paciente.getArchivos())
//                                .map(archivos -> {
//                                    try {
//                                        return filesManagerService.listarArchivos(archivos);
//                                    } catch (Exception e) {
//                                        log.error("Gestor de archivos no disponible.", e);
//                                        return Collections.emptyList();
//                                    }
//                                })
//                                .orElse(Collections.emptyList())
//                );
//
//
//            }
            return ResponseEntity.ok(personaDTO);
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<PersonaDTO> crear(@RequestBody RegistroDTO registro) throws MyException {
        RequestDTO request = new RequestDTO();
        request.setFecha(registro.getCredenciales().getFechaDeSolicitudDeCodigoDeVerificacion());
        request.setTemplate(TiposDePlantillas.CODIGO_VERIFICACION_DE_CORREO.name());
        if (registro.getTipoPersona().equals(TipoPersona.PACIENTE)){
            log.info("Creando nueva persona PACIENTE.");
            Paciente paciente = pacienteService.crear(registro);
            request.setTo(paciente.getCredenciales().getEmail());
            try{
                log.info("Enviando correo para creacion de contraseña.");
                servicioVerificacion.codigoDeVerificacion(request);
            }catch (RuntimeException e){
                log.error(e.getMessage());
                throw new RuntimeException("Error al enviar el correo de creacion de contraseña.");
            }
            return ResponseEntity.ok(personaMapper.getPersonaDTO(paciente));
        }else if (registro.getTipoPersona().equals(TipoPersona.MEDICO)){
            log.info("Creando nueva persona MEDICO.");
            Medico medico = medicoService.crear(registro);
            request.setTo(medico.getCredenciales().getEmail());
            try{
                log.info("Enviando correo para creacion de contraseña.");
                servicioVerificacion.codigoDeVerificacion(request);
            }catch (RuntimeException e){
                log.error(e.getMessage());
                throw new RuntimeException("Error al enviar el correo de creacion de contraseña.");
            }
            return ResponseEntity.ok(personaMapper.getMedicoDTO(medico));
        }
        return null;
    }

    @PutMapping("/{param}")
    public ResponseEntity<PersonaDTO> modificar(@PathVariable String param, @RequestBody PersonaDTO personaDTO) throws MyException {
        TipoPersona tipoPersona = personaService.getTipoPersona(param);
        if (tipoPersona.equals(TipoPersona.PACIENTE)){
            log.info("Modificando persona PACIENTE con param: " + param);
            return ResponseEntity.ok(
                    personaMapper.getPacienteDTO(pacienteService.modificar(param, personaDTO))
            );
        }else if (tipoPersona.equals(TipoPersona.MEDICO)){
            log.info("Modificando persona MEDICO con param: " + param);
            return ResponseEntity.ok(
                    personaMapper.getMedicoDTO(medicoService.modificar(param, personaDTO))
            );
        }
        return null;
    }

    @DeleteMapping("/{param}")
    public ResponseEntity<ResponseMessage> eliminar(@PathVariable String param) throws MyException {
        TipoPersona tipoPersona = personaService.getTipoPersona(param);
        if (tipoPersona.equals(TipoPersona.PACIENTE)){
            log.info("Eliminando persona PACIENTE con param: " + param);
            return ResponseEntity.ok(pacienteService.eliminar(param));
        }else if (tipoPersona.equals(TipoPersona.MEDICO)){
            log.info("Eliminando persona MEDICO con param: " + param);
            return ResponseEntity.ok(medicoService.eliminar(param));
        }
        return null;
    }

    @PutMapping("/agregar-archivo/{param}/{idArchivo}")
    public void agregarArchivo(@PathVariable String param, @PathVariable String idArchivo) throws MyException{
        TipoPersona tipoPersona = personaService.getTipoPersona(param);
        if (tipoPersona.equals(TipoPersona.PACIENTE)){
            log.info("Asociando nuevo archivo a persona PACIENTE  con param" + param);
            pacienteService.agregarArchivo(param,idArchivo);
        }else if (tipoPersona.equals(TipoPersona.MEDICO)){
            log.info("Asociando nuevo archivo a persona MEDICO  con param" + param);
            medicoService.agregarArchivo(param,idArchivo);
        }
    }

    @GetMapping("/get-all/{tipo}")
    public ResponseEntity<List<PersonaDTO>> getAll(@PathVariable TipoPersona tipo, @RequestParam(required = false) String especialidadMedico) throws MyException {
        List<PersonaDTO> personas = new ArrayList<>();
        if (tipo == null)
            throw new MyException("Es necesario especificar el tipo de persona.");
        if (tipo.equals(TipoPersona.MEDICO)){
            log.info("Buscando personas tipo MEDICO.");
            if (especialidadMedico == null) {
                personas = personaMapper.listaMedicos(medicoService.getAll());
            }else{
                personas = personaMapper.listaMedicos(medicoService.getAllPorEspecialidad(especialidadMedico));
            }
            return ResponseEntity.ok(personas);
        }else if (tipo.equals(TipoPersona.PACIENTE)){
            log.info("Buscando personsa tipo PACIENTE    .");
            personas = personaMapper.listaPacientes(pacienteService.getAll());
            return ResponseEntity.ok(personas);
        }
        return null;
    }

}
