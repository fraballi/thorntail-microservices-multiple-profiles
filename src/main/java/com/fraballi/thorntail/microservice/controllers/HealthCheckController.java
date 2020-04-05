package com.fraballi.thorntail.microservice.controllers;

import java.util.Collections;

import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.health.HealthCheckResponse;

import com.fraballi.thorntail.microservice.domain.HealthMessage;
import com.fraballi.thorntail.microservice.services.HealthCheckService;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Stateless
@Path(value = "health")
public class HealthCheckController {

   private Instance<HealthCheckService> service;

   public HealthCheckController() {
   }

   @Inject
   public HealthCheckController(@Any Instance<HealthCheckService> service) {
      this.service = service;
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public Response get() {
      final HealthCheckResponse response = service.get().call();
      CacheControl cache = new CacheControl();
      cache.setNoCache(true);

      Response.ResponseBuilder builder = Response.ok(HealthMessage
            .builder()
            .name(response.getName())
            .status(response.getState().name())
            .data(response.getData().orElse(Collections.emptyMap()))
            .build());

      builder.cacheControl(cache);
      return builder.build();
   }
}
