package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.com.onlineshop.service.CreditService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/credit")
public class CreditController {

    private final CreditService creditService;
}
