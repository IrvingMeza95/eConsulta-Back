package com.iamf.commons.mappers;

import com.iamf.commons.dtos.PersonaDTO;
import com.iamf.commons.models.Medico;
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
        PersonaDTO personaDTO = PersonaDTO.builder()
        		.id(persona.getId())
        		.pais(persona.getPais())
        		.ciudad(persona.getCiudad())
        		.direccion(persona.getDireccion())
        		.numeroExterior(persona.getNumeroExterior())
        		.codigoPostal(persona.getCodigoPostal())
        		.credenciales(usuarioMapper.getUsuarioDTO(persona.getCredenciales()))
        		.verificado(persona.getVerificado())
				.tipoPersona(persona.getTipoPersona())
//				.archivos(resoonseFileMapper.getResponseFileList2(persona.getArchivos()))
        		.build();
        return personaDTO;
    }

	public PersonaDTO getMedicoDTO(Medico medico){
		PersonaDTO personaDTO = new PersonaDTO();
		fillPersonaDTO(medico,personaDTO);
		personaDTO.setEspecialidad(medico.getEspecialidad());
		personaDTO.setSueldo(medico.getSueldo());
		return personaDTO;
	}

	public void fillPersonaDTO(Persona persona, PersonaDTO personaDTO){
		personaDTO.setId(persona.getId());
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

}
