//package com.ssafy.api.controller;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class ServerControllerTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    void server는_인증없이_호출된다() throws Exception{
//        //given
//        String expected = "default";
//        ResponseEntity<String> response = restTemplate.getForEntity("/server", String.class);
//
//        //when
//        //then
//        assertEquals( HttpStatus.OK, response.getStatusCode());
//        assertEquals(expected, response.getBody());
//    }
//
//}
