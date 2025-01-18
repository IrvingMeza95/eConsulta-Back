package com.iamf.filesCommons.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseFile {
    private String id;
    private String name;
    private String url;
    private String type;
    private Integer size;

}
