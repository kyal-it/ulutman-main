package com.ulutman.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/language")
@Tag(name = "Multi-lingual")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class WelcomeController {

    private final MessageSource messageSource;

    @Operation(
            summary = "Multi-lingual",
            description = "Returns a welcome message based on the specified language.",
            parameters = @Parameter(
                    name = "Accept-Language",
                    description = "Language header for localization (e.g., en, ru, ky, tg)",
                    required = false
            )
    )
    @ApiResponse(responseCode = "201", description = "Successfully recognizes languages")
    @GetMapping("/welcome")
    public String welcomeMessage(
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return messageSource.getMessage("welcome.message", null, locale);
    }
}
