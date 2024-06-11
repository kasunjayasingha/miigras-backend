package com.kasunjay.miigrasbackend.controller.web;

import com.kasunjay.miigrasbackend.service.core.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/main")
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;
}
