package io.github.amings.mingle.svc.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Model for Payload Encryption
 * @author Ming
 */

@Data
public class PayLoadEncryptionModel {

	@JsonProperty("p1")
	private String body;
	@JsonProperty("p2")
	private String iv;

}
