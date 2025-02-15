//package com.iamf.servicioConsultas.redis;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class RedisHealthCheck implements CommandLineRunner {
//
//    private final RedisConnectionFactory redisConnectionFactory;
//
//    public RedisHealthCheck(RedisConnectionFactory redisConnectionFactory) {
//        this.redisConnectionFactory = redisConnectionFactory;
//    }
//
//    @Override
//    public void run(String... args) {
//        try {
//            var connection = redisConnectionFactory.getConnection();
//            connection.ping();
//            log.info("✅ Conexión a Redis exitosa.");
//        } catch (Exception e) {
//            log.error("❌ Error al conectar con Redis: " + e.getMessage());
//        }
//    }
//}
//
