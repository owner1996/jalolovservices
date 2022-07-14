package com.jalolov.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email) {
}
