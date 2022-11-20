package io.github.amings.mingle.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * UUID Utils
 * @author Ming
 */

public class UUIDUtils {


	/**
	 * generate without dash(-) uuid
	 * @return String
	 */
	public static String generateUuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * generate with dash(-) uuid
	 * @return String
	 */
	public static String generateUuid4Origin() {
		return UUID.randomUUID().toString();
	}

	/**
	 * generate uuid with random value
	 * <br>
	 * uuid + 8 random value
	 *
	 * @return String
	 */
	public static String generateUuidRandom() {
		StringBuilder randomValue = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			randomValue.append(ThreadLocalRandom.current().nextInt(9));
		}
		return generateUuid() + randomValue;
	}

}
