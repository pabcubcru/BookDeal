package com.pabcubcru.infobooks.service;

import com.pabcubcru.infobooks.services.RequestService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RequestServiceTests {

    @Autowired
    private RequestService requestService;  
    
}
