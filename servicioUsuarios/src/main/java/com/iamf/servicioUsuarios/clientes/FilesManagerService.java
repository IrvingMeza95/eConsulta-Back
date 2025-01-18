package com.iamf.servicioUsuarios.clientes;

import com.iamf.filesCommons.responses.ResponseFile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="filesManagerService")
public interface FilesManagerService {
    @GetMapping("/files/files-paths")
    List<ResponseFile> listarArchivos(@RequestParam List<String> filesIds);
}
