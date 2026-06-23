package com.epam.project.client;

import com.epam.project.dto.WorkloadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "trainer-workload-service", fallback = WorkloadFeignClientFallback.class)
public interface WorkloadFeignClient {

    @PostMapping("/api/workloads")
    void updateWorkload(@RequestBody WorkloadRequest request);

}