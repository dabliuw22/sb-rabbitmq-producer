
package com.leysoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leysoft.model.SimpleMessage;
import com.leysoft.service.inter.SenderService;

@RestController
@RequestMapping(
        value = {
            "/message"
        })
public class MessageController {

    @Autowired
    private SenderService senderService;

    @PostMapping(
            value = {
                "/send"
            })
    public ResponseEntity<String> sendMessage(@RequestBody SimpleMessage message) {
        return ResponseEntity.ok(senderService.send(message));
    }
}
