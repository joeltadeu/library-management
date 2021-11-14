package com.paramservice.business.web.controller;

import com.paramservice.business.persistence.entity.ParamEntity;
import com.paramservice.business.web.dto.ParamDTO;
import com.paramservice.business.web.helper.ParamHelper;
import com.paramservice.business.service.ParamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/params")
public class ParamController {

    private final ParamService service;
    private final ParamHelper helper;

    public ParamController(ParamService service, ParamHelper helper) {
        this.service = service;
        this.helper = helper;
    }

    @GetMapping(value = "/by-key/{key}", produces = "application/json")
    @Operation(summary = "Get a param by key")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the param",
                     content = { @Content(mediaType = "application/json",
                                          schema = @Schema(implementation = ParamEntity.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid key supplied",
                     content = @Content),
        @ApiResponse(responseCode = "404", description = "Param not found",
                     content = @Content) })
    public ResponseEntity<ParamDTO> find(@Parameter(description = "Param's key to be searched")
                                             @PathVariable(value = "key") String key) {
        log.info("Param key requested: {}", key);
        ParamEntity param = service.findByKey(key);
        log.info("Param requested: [{}]", param);
        return ResponseEntity.ok(helper.toModel(param));
    }
}