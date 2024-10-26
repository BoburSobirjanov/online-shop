package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.repository.CreditRepository;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
}
