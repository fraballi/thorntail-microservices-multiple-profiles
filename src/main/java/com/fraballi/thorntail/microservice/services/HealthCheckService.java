package com.fraballi.thorntail.microservice.services;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Liveness
@ApplicationScoped
public class HealthCheckService implements HealthCheck {

   public HealthCheckService() {
   }

   @Override
   public HealthCheckResponse call() {
      MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
      long memUsed = memBean.getHeapMemoryUsage().getUsed();
      long memMax = memBean.getHeapMemoryUsage().getMax();

      return HealthCheckResponse
            .named(HealthCheckService.class.getSimpleName() + "Liveness")
            .withData("memory used", memUsed)
            .withData("memory max", memMax)
            .state(memUsed < memMax * 0.9)
            .build();
   }
}
