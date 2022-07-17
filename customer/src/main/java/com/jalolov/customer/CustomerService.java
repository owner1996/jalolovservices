package com.jalolov.customer;

import com.jalolov.clients.fraud.FraudCheckResponse;
import com.jalolov.clients.fraud.FraudClient;
import com.jalolov.clients.notification.NotificationClient;
import com.jalolov.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService{

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // todo: check if email not taken
        // todo: check if fraudster
        customerRepository.saveAndFlush(customer);
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse != null && fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }
        notificationClient.send(
                new NotificationRequest(customer.getId(),
                        customer.getEmail(),
                        "Jalolov",
                        customer.getFirstName() + ", welcome to application"
                        ));
    }
}
