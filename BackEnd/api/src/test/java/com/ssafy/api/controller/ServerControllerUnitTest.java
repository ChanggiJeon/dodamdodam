//package com.ssafy.api.controller;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.mock.env.MockEnvironment;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ServerControllerUnitTest {
//
//    @Test
//    void real_server가_조회된다() {
//        //given
//        String expectedserver = "real";
//        MockEnvironment env = new MockEnvironment();
//        env.addActiveProfile(expectedserver);
//        env.addActiveProfile("oauth");
//        env.addActiveProfile("real-db");
//
//        ServerController controller = new ServerController(env);
//
//        //when
//        String server = controller.server();
//        System.out.println(server);
//        //then
//        assertEquals(expectedserver, server);
//    }
//
//    @Test
//    void activate_server가_없으면_default가_조회된다() {
//        //givne
//        String expectedServer = "default";
//        MockEnvironment env = new MockEnvironment();
//        ServerController controller = new ServerController(env);
//
//        //when
//        String server = controller.server();
//
//        //then
//        assertEquals(expectedServer, server);
//
//    }
//
//    @Test
//    void real_server가_없으면_첫_번째가_조회된다() {
//        //given
//        String expectedServer = "oauth";
//        MockEnvironment env = new MockEnvironment();
//
//        env.addActiveProfile(expectedServer);
//        env.addActiveProfile("real-db");
//
//        ServerController controller = new ServerController(env);
//
//        //when
//        String server = controller.server();
//
//        //then
//        assertEquals(expectedServer, server);
//    }
//}