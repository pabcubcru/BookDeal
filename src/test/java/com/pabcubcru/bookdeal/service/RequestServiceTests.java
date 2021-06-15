package com.pabcubcru.bookdeal.service;

import java.util.ArrayList;
import java.util.List;

import com.pabcubcru.bookdeal.models.Request;
import com.pabcubcru.bookdeal.models.RequestStatus;
import com.pabcubcru.bookdeal.services.RequestService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RequestServiceTests {

    @Autowired
    private RequestService requestService;

    @Test
    public void shouldFindMyRequests() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 21);
        Page<Request> requests = this.requestService.listMyRequests("test001", pageRequest);

        Assertions.assertThat(requests.getTotalElements()).isEqualTo(2L);
    }

    @Test
    public void shouldFindReceivedRequests() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 21);
        Page<Request> requests = this.requestService.listReceivedRequests("test002", pageRequest);

        Assertions.assertThat(requests.getTotalElements()).isEqualTo(2L);
    }

    @Test
    public void shouldFindByUsername1AndIdBook2() throws Exception {
        Request request = this.requestService.findByUsername1AndIdBook2("test001", "book-002");

        Assertions.assertThat(request.getIdBook2()).isEqualTo("book-002");
    }

    @Test
    public void shouldFindByUsername2AndIdBook2AndNotRejected() throws Exception {
        List<Request> requests = this.requestService.findByUsername2AndIdBook2AndStatusNot("test001", "book-002",
                RequestStatus.RECHAZADA.toString());
        Assertions.assertThat(requests).isEmpty();

        requests = this.requestService.findByUsername2AndIdBook2AndStatusNot("test002", "book-002",
                RequestStatus.RECHAZADA.toString());
        Assertions.assertThat(requests.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindFirstByIdBook1AndStatus() throws Exception {
        Request request = this.requestService.findFirstByIdBook1AndStatus("book-001",
                RequestStatus.ACEPTADA.toString());

        Assertions.assertThat(request.getId()).isEqualTo("request-002");
        Assertions.assertThat(request.getStatus()).isEqualTo(RequestStatus.ACEPTADA.toString());
    }

    @Test
    public void shouldFindFirstByIdBook2AndStatus() throws Exception {
        Request request = this.requestService.findFirstByIdBook2AndStatus("book-002",
                RequestStatus.PENDIENTE.toString());

        Assertions.assertThat(request.getIdBook2()).isEqualTo("book-002");
        Assertions.assertThat(request.getStatus()).isEqualTo(RequestStatus.PENDIENTE.toString());
    }

    @Test
    public void shouldFindById() throws Exception {
        Request request = this.requestService.findById("request-001");

        Assertions.assertThat(request.getPay()).isEqualTo(10.);
        Assertions.assertThat(request.getAction()).isEqualTo("COMPRA");
    }

    @Test
    public void shouldFindByIdBook1AndNotRejectedAndNotCanceledAndStatusIntercambio() {
        List<Request> requests = this.requestService.findByIdBook1AndStatusNotAndStatusNotAndAction("book-001",
                "INTERCAMBIO");

        Assertions.assertThat(requests.size()).isEqualTo(1);
        Assertions.assertThat(requests.get(0).getStatus()).isEqualTo("ACEPTADA");
    }

    @Test
    public void shouldFindByIdBook2AndNotRejectedAndNotCanceledAndStatusCompra() throws Exception {
        List<Request> requests = this.requestService.findByIdBook2AndStatusNotAndStatusNotAndAction("book-002",
                "COMPRA");

        Assertions.assertThat(requests.size()).isEqualTo(1);
        Assertions.assertThat(requests.get(0).getStatus()).isEqualTo("PENDIENTE");
    }

    @Test
    public void findByIdBook1OrIdBook2() throws Exception {
        String idBook = "book-002";
        List<Request> requests = this.requestService.findByIdBook1OrIdBook2(idBook, idBook);

        Assertions.assertThat(requests.size()).isEqualTo(2);
    }

    @Test
    public void findFirstByIdBook1OrIdBook2() throws Exception {
        Request request = this.requestService.findFirstByIdBook1OrIdBook2("book-001");

        Assertions.assertThat(request.getUsername1()).isEqualTo("test001");
        Assertions.assertThat(request.getUsername2()).isEqualTo("test002");
    }

    @Test
    public void shouldDeleteById() throws Exception {
        Request request = this.requestService.findById("request-003");
        Assertions.assertThat(request).isNotNull();

        this.requestService.deleteById("request-003");
        request = this.requestService.findById("request-003");
        Assertions.assertThat(request).isNull();
    }

    @Test
    public void shouldDeleteAll() throws Exception {
        List<Request> requests = new ArrayList<>();
        requests.add(this.requestService.findById("request-004"));
        requests.add(this.requestService.findById("request-005"));

        Assertions.assertThat(requests.get(0)).isNotNull();
        Assertions.assertThat(requests.get(1)).isNotNull();

        this.requestService.deleteAll(requests);

        Request request = this.requestService.findById("request-004");
        Assertions.assertThat(request).isNull();
        request = this.requestService.findById("request-005");
        Assertions.assertThat(request).isNull();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "../csv/services/Requests.csv", encoding = "utf-8", numLinesToSkip = 1, delimiterString = ";")
    public void shouldSave(String username1, String username2, String idBook1, String idBook2, String status,
            String action, Double pay, String comment) throws Exception {
        Integer numRequestsBefore = this.requestService.findByIdBook1OrIdBook2(idBook1, idBook2).size();
        
        Request request = new Request(username1, username2, idBook1, idBook2, status, action, pay, comment);
        this.requestService.save(request);

        Integer numRequestsAfter = this.requestService.findByIdBook1OrIdBook2(idBook1, idBook2).size();
        Assertions.assertThat(numRequestsAfter).isEqualTo(numRequestsBefore + 1);
    }
}
