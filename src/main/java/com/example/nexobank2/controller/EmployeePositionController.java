package com.example.nexobank2.controller;

import com.example.nexobank2.dto.EmployeePositionRequest;
import com.example.nexobank2.dto.EmployeePositionResponse;
import com.example.nexobank2.mapper.EmployeePositionMapper;
import com.example.nexobank2.service.impl.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-position")
@AllArgsConstructor
@Tag(name = "Должности сотрудников", description = "API для управления должностями в банке")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class EmployeePositionController {
    private final PositionService service;
    private final EmployeePositionMapper mapper;
    @PostMapping("/save")
    @Operation(
        summary = "Создать новую должность",
        description = "Добавляет новую должность в справочник должностей банка"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Должность успешно создана"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<EmployeePositionResponse> save(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные должности (название, описание)",
            required = true
        ) @RequestBody @Valid EmployeePositionRequest request){
        return ResponseEntity.ok(mapper.toResponse(service.save(mapper.toEntity(request))));
    }
    @DeleteMapping("/delete/{id}")
    @Operation(
        summary = "Удалить должность",
        description = "Удаляет должность из справочника"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Должность успешно удалена"),
        @ApiResponse(responseCode = "404", description = "Должность не найдена")
    })
    public ResponseEntity<String> delete(
        @Parameter(description = "ID должности", required = true) @PathVariable @Min(1) Long id){
        service.deleteById(id);
        return ResponseEntity.ok("Должность удалена");
    }

    @GetMapping("/getByid/{id}")
    @Operation(
        summary = "Получить должность по ID",
        description = "Возвращает информацию о должности по её идентификатору"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Должность найдена"),
        @ApiResponse(responseCode = "404", description = "Должность не найдена")
    })
    public ResponseEntity<EmployeePositionResponse> get(
        @Parameter(description = "ID должности", required = true) @PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }

    @GetMapping("/getAll")
    @Operation(
        summary = "Получить все должности",
        description = "Возвращает постраничный список всех должностей в банке"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список должностей получен")
    })
    public ResponseEntity<List<EmployeePositionResponse>> getAll(
        @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(mapper.toResponseList(service.findAll(page, size)));
    }
}
