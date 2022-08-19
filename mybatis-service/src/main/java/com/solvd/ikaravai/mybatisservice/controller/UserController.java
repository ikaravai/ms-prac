package com.solvd.ikaravai.mybatisservice.controller;

import com.solvd.ikaravai.mybatisservice.config.RabbitComplexOutputProperties;
import com.solvd.ikaravai.mybatisservice.config.RabbitSimpleOutputProperties;
import com.solvd.ikaravai.mybatisservice.event.model.MessagePayload;
import com.solvd.ikaravai.mybatisservice.mapper.UserMapper;
import com.solvd.ikaravai.mybatisservice.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mybatis")
public class UserController {

    private final UserMapper userMapper;
    private final RabbitTemplate template;
    private final RabbitSimpleOutputProperties simpleOutputProperties;
    private final RabbitComplexOutputProperties complexOutputProperties;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        log.info("Invoking UserController - getUsers method");
        return userMapper.findAllUsers();
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Long id) {
        log.info("Invoking UserController - getUserById method with id : {}", id);
        User user = userMapper.findUserById(id);
        MessagePayload messagePayload = new MessagePayload();
        messagePayload.setMessage("Mybatis-service, getUserById");
        messagePayload.setVersion(1);
        messagePayload.setUser(user);
        template.convertAndSend(complexOutputProperties.getExchange(), "", messagePayload);
        return user;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User saveUser(@RequestBody User user) {
        log.info("Invoking UserController - saveUser method with user : {}", user);
        userMapper.saveUser(user);
        MessagePayload message = new MessagePayload();
        message.setMessage("User saved (probably lol)");
        message.setVersion(1);
        message.setUser(user);
        template.convertAndSend(simpleOutputProperties.getExchange(), simpleOutputProperties.getRoutingKey(), message);
        log.info("Message sent from MybatisService saveUser() with v.{}, Time : {}", message.getVersion(), LocalTime.now());
        return user;
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        log.info("Invoking UserController - updateUser method with id : {}, user : {}", id, user);
        userMapper.updateUser(user, id);
        return user;
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User saveUser(@PathVariable Long id) {
        log.info("Invoking UserController - deleteUser method with id : {}", id);
        User user = userMapper.findUserById(id);
        userMapper.deleteUser(id);
        return user;
    }
}
