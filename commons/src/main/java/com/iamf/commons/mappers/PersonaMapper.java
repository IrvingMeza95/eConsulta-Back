package com.iamf.commons.mappers;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.enums.TipoPersona;
import com.iamf.commons.models.Medico;
import com.iamf.commons.models.Paciente;
import com.iamf.commons.models.Persona;

import java.util.List;
import java.util.stream.Collectors;

public class PersonaMapper {

    private final UsuarioMapper usuarioMapper = new UsuarioMapper();

	public void fillPersona(Persona persona, PersonaDTO personaDTO){
		persona.setId(personaDTO.getId());
		persona.setCredenciales(usuarioMapper.getUsuario(personaDTO.getCredenciales()));
//		persona.setArchivos(resoonseFileMapper.getFileList(personaDTO.getArchivos()));
	}

    public PersonaDTO getPersonaDTO(Persona persona){
		PersonaDTO personaDTO = new PersonaDTO();
		if (persona.getTipoPersona().equals(TipoPersona.MEDICO)){
			personaDTO = getMedicoDTO((Medico)persona);
		} else {
			personaDTO = getPacienteDTO((Paciente) persona);
		}
		return personaDTO;
    }

	public PersonaDTO getPacienteDTO(Paciente paciente){
		PersonaDTO personaDTO = new PersonaDTO();
		fillPersonaDTO(paciente,personaDTO);
		personaDTO.setObraSocial(paciente.getObraSocial());
		personaDTO.getCredenciales().setPassword(null);
		return personaDTO;
	}

	public PersonaDTO getMedicoDTO(Medico medico){
		PersonaDTO personaDTO = new PersonaDTO();
		fillPersonaDTO(medico,personaDTO);
		if (medico.getEspecialidad() != null)
			personaDTO.setEspecialidad(medico.getEspecialidad());
//		if (medico.getTurnos() != null)
//			personaDTO.setTurnos(medico.getTurnos());
		personaDTO.setSueldo(medico.getSueldo());
		personaDTO.getCredenciales().setPassword(null);
		return personaDTO;
	}

	public void fillPersonaDTO(Persona persona, PersonaDTO personaDTO){
		personaDTO.setId(persona.getId());
		personaDTO.setTipoPersona(persona.getTipoPersona());
		personaDTO.setDni(persona.getDni());
		personaDTO.setNombre(persona.getNombre());
		personaDTO.setApellido(persona.getApellido());
		personaDTO.setFechaNacimiento(persona.getFechaNacimiento());
	    personaDTO.setPais(persona.getPais());
	    personaDTO.setCiudad(persona.getCiudad());
	    personaDTO.setDireccion(persona.getDireccion());
	    personaDTO.setNumeroExterior(persona.getNumeroExterior());
	    personaDTO.setCodigoPostal(persona.getCodigoPostal());
	    personaDTO.setCredenciales(usuarioMapper.getUsuarioDTO(persona.getCredenciales()));
	    personaDTO.setVerificado(persona.getVerificado());
		personaDTO.setTipoPersona(persona.getTipoPersona());
//		if (persona.getArchivos() != null)
//			personaDTO.setArchivos(resoonseFileMapper.getResponseFileList2(persona.getArchivos()));
	}

	public List<PersonaDTO> listaPersonaDTO(List<Persona> personas){
		return personas.stream().map(this::getPersonaDTO).collect(Collectors.toList());
	}

	public List<PersonaDTO> listaMedicos(List<Medico> medicos){
		return medicos.stream().map(this::getMedicoDTO).collect(Collectors.toList());
	}

	public List<PersonaDTO> listaPacientes(List<Paciente> medicos){
		return medicos.stream().map(this::getPacienteDTO).collect(Collectors.toList());
	}

}
