package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.onlineshop.model.entity.apicount.ApiRequestCount;
import uz.com.onlineshop.repository.ApiRequestCountRepository;

@Service
@RequiredArgsConstructor
public class ApiRequestCountService {

    private final ApiRequestCountRepository apiRequestCountRepository;

    @Transactional
    public void incrementRequestCount(String apiEndpoint) {
        ApiRequestCount apiRequestCount = apiRequestCountRepository.findByApiEndpoint(apiEndpoint)
                .orElse(new ApiRequestCount(apiEndpoint, 0));

        apiRequestCount.setRequestCount(apiRequestCount.getRequestCount() + 1);
        apiRequestCountRepository.save(apiRequestCount);
    }
}
