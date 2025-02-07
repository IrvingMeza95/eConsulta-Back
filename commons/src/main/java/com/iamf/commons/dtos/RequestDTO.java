package com.iamf.commons.dtos;

import com.iamf.filesCommons.responses.ResponseFile;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDTO {
	private String to;
	private String username;
	private String subject;
	private String template;
	private String fecha;
	private String ubicacion;
	private String dispositivo;
	private Integer codigo;
	private List<MetaData> metaData;
	private ResponseFile responseFile;
}
