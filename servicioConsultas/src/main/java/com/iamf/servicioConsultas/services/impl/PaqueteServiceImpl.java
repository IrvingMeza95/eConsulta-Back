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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaqueteServiceImpl implements PaqueteService {

    @Autowired
    private PaqueteRepo paqueteRepo;
    @Autowired
    private ServicioMedicoService servicioMedicoService;
    @Value("${porcentaje.descuento.paquete}")
    private Double porcentajeDescuentoPaquete;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private Paquete guardar(Paquete paquete) {
        Paquete paqueteDb = paqueteRepo.save(paquete);
        calcularPrecio(paqueteDb);
        return paqueteDb;
    }

    @Override
    public synchronized Paquete creacionPaqueteControlado(List<Long> ids) throws MyException {
        /*
            - Metodo de creacion de paquetes para controlar transacciones concurrentes evitando el efecto carrera
            - Solo funciona para apis con solo 1 instancia del servicio
            - Para multiples instancias del mismo servicio, se implementa Redis
        */
        return crear(ids);
    }

    private Paquete crear(List<Long> ids) throws MyException {
//        String lockKey = "lock:paquete:" + ids.hashCode();
//
//        Boolean locked = null;
//        try{
//            log.info("Validando si la lista con lockkey: " + lockKey + " ya esta siendo creada.");
//            locked = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 5, TimeUnit.SECONDS);
//        }catch (RuntimeException e){
//            log.error(String.valueOf(e));
//            throw new RuntimeException(e.getMessage());
//        }
//
//        if (Boolean.FALSE.equals(locked))
//            throw new MyException("Otro proceso está creando este paquete, inténtalo más tarde.");

        List<Paquete> paqueteExxistente = buscarPaquetePorServiciosIds(ids);
        if (!paqueteExxistente.isEmpty())
            throw new MyException("Ya existe un paquete con los servicios seleccionados, su id es " + paqueteExxistente.get(0).getId() + ".");
        log.info("Creando paquete");
        List<ServicioMedico> servicioMedicos = servicioMedicoService.getServiciosMedicos(ids);
        if (servicioMedicos.isEmpty() || servicioMedicos.size() == 0)
            throw new MyException("La lista de servicios seleccionados esta vacia.");

        Paquete paquete = Paquete.builder()
                .servicios(servicioMedicos)
                .precio(0.0)
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
        Paquete paqueteBd = paquete.get();
        calcularPrecio(paqueteBd);
        return paqueteBd;
    }

    private void calcularPrecio(Paquete paquete) {
        Double precio = paquete.getServicios().stream().mapToDouble(ServicioMedico::getPrecio).sum();
        paquete.setPrecio(precio * (1 - porcentajeDescuentoPaquete));
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
        List<Paquete> paquetes = paqueteRepo.buscarPaquetePorServiciosIds(serviciosIds, serviciosIds.size());
        return paquetes.stream().map(p -> {
            calcularPrecio(p);
            return p;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Paquete> getAll() {
        List<Paquete> paquetes = paqueteRepo.findAll();
        return paquetes.stream().map(p -> {
            calcularPrecio(p);
            return p;
        }).collect(Collectors.toList());
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
