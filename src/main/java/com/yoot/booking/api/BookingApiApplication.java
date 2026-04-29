package com.yoot.booking.api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingApiApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing() // tránh crash nếu thiếu file
				.load();

		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET", ""));
		System.setProperty("JWT_ISSUER", dotenv.get("JWT_ISSUER", ""));
		System.setProperty("JWT_AUDIENCE", dotenv.get("JWT_AUDIENCE", ""));
		System.setProperty("JWT_ACCESS_EXPIRATION", dotenv.get("JWT_ACCESS_EXPIRATION", "86400000"));
		System.setProperty("JWT_REFRESH_EXPIRATION", dotenv.get("JWT_REFRESH_EXPIRATION", "604800000"));

		// Frontend
		System.setProperty("FRONTEND_URLS", dotenv.get("FRONTEND_URLS"));

		// Email
		System.setProperty("EMAIL_USERNAME", dotenv.get("EMAIL_USERNAME"));
		System.setProperty("EMAIL_PASSWORD", dotenv.get("EMAIL_PASSWORD"));

		// VNPay
		System.setProperty("VNPAY_URL", dotenv.get("VNPAY_URL"));
		System.setProperty("VNPAY_API", dotenv.get("VNPAY_API"));
		System.setProperty("VNPAY_TMN_CODE", dotenv.get("VNPAY_TMN_CODE"));
		System.setProperty("VNPAY_HASH_SECRET", dotenv.get("VNPAY_HASH_SECRET"));
		System.setProperty("VNPAY_RETURN_URL", dotenv.get("VNPAY_RETURN_URL"));
		System.setProperty("VNPAY_FRONTEND_RETURN_URL", dotenv.get("VNPAY_FRONTEND_RETURN_URL"));

		SpringApplication.run(BookingApiApplication.class, args);
	}
}