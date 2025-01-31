package com.iamf.commons.mappers;

import com.iamf.commons.dtos.ConsultaDTO;
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

    public List<ConsultaDTO> getConsultas(List<Consulta> consultas){
        return consultas.stream().map(this::getConsultaDTO).collect(Collectors.toList());
    }

}
