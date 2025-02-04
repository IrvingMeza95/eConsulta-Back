package com.iamf.servicioConsultas.services.impl;

import com.iamf.commons.exceptions.MyException;
import com.iamf.commons.models.Paquete;
import com.iamf.commons.models.ServicioMedico;
import com.iamf.commons.models.TipoServicio;
import com.iamf.commons.responses.ResponseMessage;
import com.iamf.servicioConsultas.repositories.PaqueteRepo;
import com.iamf.servicioConsultas.services.interfaces.PaqueteService;
import com.iamf.servicioConsultas.services.interfaces.ServicioMedicoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaqueteServiceImpl implements PaqueteService {

    @Autowired
    private PaqueteRepo paqueteRepo;
    @Autowired
    private ServicioMedicoService servicioMedicoService;

    private Paquete guardar(Paquete paquete){
        return paqueteRepo.save(paquete);
    }
    @Value("${porcentaje.descuento.paquete}")
    private Double porcentajeDescuentoPaquete;

    @Override
    public Paquete crear(List<Long> ids) throws MyException {
        List<Paquete> paqueteExxistente = buscarPaquetePorServiciosIds(ids);
        if (!paqueteExxistente.isEmpty())
            throw new MyException("Ya existe un paquete con los servicios seleccionados, su id es " + paqueteExxistente.get(0).getId() + ".");
        log.info("Creando paquete");
        List<ServicioMedico> servicioMedicos = servicioMedicoService.getServiciosMedicos(ids);
        Double precio = servicioMedicos.stream()
                .mapToDouble(ServicioMedico::getPrecio)
                .sum();
        if (servicioMedicos.isEmpty() || servicioMedicos.size() == 0)
            throw new MyException("La lista de servicios seleccionados esta vacia.");
        Paquete paquete = Paquete.builder()
                .servicios(servicioMedicos)
                .precio(precio * (1 - porcentajeDescuentoPaquete))
                .build();
        return guardar(paquete);
    }

    @Override
    public Paquete getPaquete(Long id) throws MyException {
        log.info("Buscando paquete con id " + id);
        if (id == null)
            throw new MyException("El id del paquete no puede ser nulo.");
        Optional<Paquete> paquete = paqueteRepo.findById(id);
        if (paquete.isEmpty())
            throw new MyException("No se encontro el paquete con el id " + id + ".");
        return paquete.get();
    }

    @Override
    public Paquete modificar(Long id, Paquete nuevoPaquete) throws MyException {
        Paquete paqueteBd = getPaquete(id);
//        if (nuevoPaquete.getServicios() != null){
//            List<ServicioMedico> nuevaLista = nuevoPaquete.getServicios().stream().map(s -> {
//                ServicioMedico nuevoServicio = new ServicioMedico();
//                try{
//                    nuevoServicio = servicioMedicoService.getServicioMedico(s.getId());
//                }catch (MyException e){
//                    log.error(e.getMessage());
//                    throw new RuntimeException(e.getMessage());
//                }
//                return nuevoServicio;
//            }).toList();
//            paqueteBd.setServicios(nuevaLista);
//            log.info("Se actualizo la lista de servicios.");
//        }
        if (nuevoPaquete.getEnabled() != null)
            paqueteBd.setEnabled(nuevoPaquete.getEnabled());
        return guardar(paqueteBd);
    }

    @Override
    public List<Paquete> buscarPaquetePorServiciosIds(List<Long> serviciosIds) throws MyException {
        if (serviciosIds.isEmpty())
            throw new MyException("No se enviaron servicios para buscar paquete.");
        return paqueteRepo.buscarPaquetePorServiciosIds(serviciosIds, serviciosIds.size());
    }

    @Override
    public List<Paquete> getAll() {
        return paqueteRepo.findAll();
    }

    @Override
    public ResponseMessage eliminar(Long id) throws MyException {
        validarExistenciaEnConsultas(id);
        Paquete paquete = getPaquete(id);
        paqueteRepo.delete(paquete);
        return new ResponseMessage("Paquete con id " + id + " fue eliminado correctamente.");
    }

    private void validarExistenciaEnConsultas(Long id) throws MyException {
        if (id == null)
            throw new MyException("Es necesario un id.");
        List<Object[]> respuesta = paqueteRepo.existeEnConsuñtas(id);
        if (String.valueOf(respuesta.get(0)[0]).equals("1"))
            throw new MyException("No es posible eliminar el paquete con id " + id +
                    " debido a que ya ha sido usado en consultas.");
    }

}
