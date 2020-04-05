package com.fraballi.thorntail.microservice.domain;

import java.io.Serializable;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthMessage implements Serializable {

   private static final long serialVersionUID = -112700007410765628L;

   private String name;

   private String status;

   private Map<String, Object> data;
}
