package com.iamf.commons.mappers;

import com.iamf.commons.dtos.ConsultaDTO;
import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.dtos.UsuarioDTO;
import com.iamf.commons.models.Consulta;

import java.util.List;
import java.util.stream.Collectors;

public class ConsultaMapper {

    private final PersonaMapper personaMapper = new PersonaMapper();

    public ConsultaDTO getConsultaDTO(Consulta consulta){
        ConsultaDTO consultaDTO = new ConsultaDTO();
        consultaDTO.setId(consulta.getId());
        consultaDTO.setFecha(consulta.getFecha());
        consultaDTO.setHorario(consulta.getHorario());
        consultaDTO.setMedico(personaMapper.getMedicoDTO(consulta.getMedico()));
        consultaDTO.setPaciente(personaMapper.getPersonaDTO(consulta.getPaciente()));
        if (consulta.getIdServicioMedico() != null)
            consultaDTO.setIdServicioMedico(consulta.getIdServicioMedico());
        if (consulta.getIdPaquete() != null)
            consultaDTO.setIdPaquete(consulta.getIdPaquete());
        consultaDTO.setTotal(consulta.getTotal());
        consultaDTO.setPagado(consulta.getPagado());
        consultaDTO.setServiciosContratados(consulta.getServiciosContratados());
        return consultaDTO;
    }

    public ConsultaDTO getBasicConsultaDTO(Consulta consulta){
        ConsultaDTO consultaDTO = getConsultaDTO(consulta);
        //PAciente
        PersonaDTO paciente = new PersonaDTO();
        paciente.setNombre(consultaDTO.getPaciente().getNombre());
        paciente.setApellido(consultaDTO.getPaciente().getApellido());
        UsuarioDTO pacienteCred = new UsuarioDTO();
        pacienteCred.setEmail(consultaDTO.getPaciente().getCredenciales().getEmail());
        paciente.setCredenciales(pacienteCred);
        consultaDTO.setPaciente(paciente);

        //Medico
        PersonaDTO medico = new PersonaDTO();
        medico.setNombre(consultaDTO.getMedico().getNombre());
        medico.setApellido(consultaDTO.getMedico().getApellido());
        UsuarioDTO medicoCred = new UsuarioDTO();
        medicoCred.setEmail(consultaDTO.getMedico().getCredenciales().getEmail());
        medico.setCredenciales(medicoCred);
        consultaDTO.setMedico(medico);

        consultaDTO.setServiciosContratados(null);

        return consultaDTO;
    }

    public List<ConsultaDTO> getConsultas(List<Consulta> consultas){
        return consultas.stream().map(this::getConsultaDTO).collect(Collectors.toList());
    }

    public List<ConsultaDTO> getConsultasBasic(List<Consulta> consultas){
        return consultas.stream().map(this::getBasicConsultaDTO).collect(Collectors.toList());
    }

}
