package com.ulutman.controller;

import com.ulutman.model.dto.PublishResponse;
import com.ulutman.service.MainPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main-page")
@Tag(name = "Main - Page")
@SecurityRequirement(name = "Authorization")
public class MainPageController {
    private final MainPageService mainPageService;

    @Operation(summary = "Get publishes by category WORK")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category WORK")
    @GetMapping("/work")
    List<PublishResponse> getPublishByCategoryWork() {
        return mainPageService.findPublishByCategoryWork();
    }

    @Operation(summary = "Get publishes by category RENT")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category RENT")
    @GetMapping("/rent")
    List<PublishResponse> getPublishByCategoryRent() {
        return mainPageService.findPublishByCategoryRent();
    }

    @Operation(summary = "Get publishes by category SELL")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category SELL")
    @GetMapping("/sell")
    List<PublishResponse> getPublishByCategorySell() {
        return mainPageService.findPublishByCategorySell();
    }

    @Operation(summary = "Get publishes by category HOTEL")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category HOTEL")
    @GetMapping("/hotel")
    List<PublishResponse> getPublishByCategoryHotel() {
        return mainPageService.findPublishByCategoryHotel();
    }
    @Operation(summary = "Get publishes by category SERVICES")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category SERVICES")
    @GetMapping("/services")
    List<PublishResponse> getPublishByCategoryServices() {
        return mainPageService.findPublishByCategoryServices();
    }

    @Operation(summary = "Get publishes by category REAL_ESTATE")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category REAL_ESTATE")
    @GetMapping("/realEstate")
    List<PublishResponse> getPublishByCategoryRealEstate() {
        return mainPageService.findPublishByCategoryRealEstate();
    }
}