package com.example.nexobank2.controller;

import com.example.nexobank2.dto.EmployeeRequest;
import com.example.nexobank2.dto.EmployeeResponse;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.EmployeeStatus;
import com.example.nexobank2.mapper.EmployeeMapper;
import com.example.nexobank2.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Управление персоналом", description = "API для управления персоналом банка")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
@Validated
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @PostMapping("/save")
    @Operation(
        summary = "Создать нового сотрудника",
        description = "Регистрирует нового сотрудника банка с паспортными данными и должностями"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Сотрудник успешно создан"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<EmployeeResponse> save(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные сотрудника",
            required = true
        ) @RequestBody @Valid EmployeeRequest employeeRequest) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.save(employeeMapper.toEntity(employeeRequest))));
    }
    @PutMapping("/changeSalary/{id}")
    @Operation(
        summary = "Изменить зарплату сотрудника",
        description = "Обновляет размер зарплаты сотрудника"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Зарплата успешно изменена"),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    public ResponseEntity<?> changeSalary(
        @Parameter(description = "ID сотрудника", required = true) @PathVariable @Min(1) Long id,
        @Parameter(description = "Новый размер зарплаты", required = true) @RequestParam
    @NotNull(message = "Зарплата обязательна!")
    @DecimalMin(value = "0.01", message = "Зарплата должна быть больше 0")
    @Digits(integer = 19, fraction = 4, message = "Некорректный формат зарплаты") BigDecimal newSalary) {
        employeeService.changeSalary(id, newSalary);
        return ResponseEntity.ok("Зарплата изменена!");
    }
    @DeleteMapping("/FireEmployee/{id}")
    @Operation(
        summary = "Уволить сотрудника",
        description = "Удаляет сотрудника из системы (увольнение)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Сотрудник уволен"),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    public ResponseEntity<?> fireEmployee(
        @Parameter(description = "ID сотрудника", required = true) @PathVariable @Min(1) Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok("Сотрудник уволен!");
    }
    @GetMapping("getById/{id}")
    @Operation(
        summary = "Получить сотрудника по ID",
        description = "Возвращает детальную информацию о сотруднике"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Сотрудник найден"),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    public ResponseEntity<?> getById(
        @Parameter(description = "ID сотрудника", required = true) @PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.findById(id)));
    }
    @GetMapping("/getByFirstName")
    @Operation(
        summary = "Найти сотрудника по имени",
        description = "Возвращает сотрудника по имени из паспортных данных"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Сотрудник найден"),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    public ResponseEntity<EmployeeResponse> getByFirstName(
        @Parameter(description = "Имя сотрудника", required = true) @RequestParam @NotBlank String firstName) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.getByPassportFirstName(firstName)));
    }
    @GetMapping("/getByEmail")
    @Operation(
        summary = "Найти сотрудника по email",
        description = "Возвращает сотрудника по адресу электронной почты"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Сотрудник найден"),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    public ResponseEntity<EmployeeResponse> getByEmail(
        @Parameter(description = "Email сотрудника", required = true) @RequestParam @Email String email) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.getByUserEmail(email)));
    }
    @GetMapping("/getuserById")
    @Operation(
        summary = "Получить сотрудника по ID пользователя",
        description = "Возвращает сотрудника по идентификатору связанного пользователя"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Сотрудник найден"),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    public ResponseEntity<EmployeeResponse> getUserById(
        @Parameter(description = "ID пользователя", required = true) @RequestParam @Min(1) Long id) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.getUserById(id)));
    }
    @GetMapping("/getAll")
    @Operation(
        summary = "Получить всех сотрудников",
        description = "Возвращает постраничный список всех сотрудников банка"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список сотрудников получен")
    })
    public ResponseEntity<List<EmployeeResponse>> getAll(
        @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeMapper.toResponseList(employeeService.findAll(page,size)));
    }
    @GetMapping("/getByEmployeeStatus")
    @Operation(
        summary = "Получить сотрудников по статусу",
        description = "Возвращает список сотрудников с указанным статусом (ACTIVE, ON_VACATION, FIRED)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список сотрудников получен")
    })
    public ResponseEntity<List<EmployeeResponse>> getByEmployeeStatus(
        @Parameter(description = "Статус сотрудника", required = true) @RequestParam @NotNull EmployeeStatus employeeStatus) {
        return ResponseEntity.ok(employeeMapper.toResponseList(employeeService.getByEmployeeStatus(employeeStatus)));
    }
    @GetMapping("/getByPosition/{id}")
    @Operation(
        summary = "Получить сотрудников по должности",
        description = "Возвращает список сотрудников, занимающих указанную должность"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список сотрудников получен"),
        @ApiResponse(responseCode = "404", description = "Должность не найдена")
    })
    public ResponseEntity<List<EmployeeResponse>> getByPosition(
        @Parameter(description = "ID должности", required = true) @PathVariable(name = "id") Long positionId) {
        return ResponseEntity.ok(employeeMapper.toResponseList(employeeService.getEmployeeByPosition(positionId)));
    }
    @PutMapping("/changeStatus/{id}")
    @Operation(
        summary = "Изменить статус сотрудника",
        description = "Обновляет статус сотрудника (ACTIVE, ON_VACATION, FIRED)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Статус успешно изменен"),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    public ResponseEntity<?> changeStatus(
        @Parameter(description = "ID сотрудника", required = true) @PathVariable @Min(1) Long id,
        @Parameter(description = "Новый статус", required = true) @RequestParam @NotNull EmployeeStatus status){
        employeeService.changeEmployeeStatus(id, status);
        return ResponseEntity.ok("Статус сотрудника изменен!");
    }
    @PutMapping("/changePosition/{employeeId}")
    @Operation(
        summary = "Изменить должности сотрудника",
        description = "Заменяет все текущие должности сотрудника на новый список"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Должности успешно изменены"),
        @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    public ResponseEntity<?> changePosition(
        @Parameter(description = "Список ID должностей", required = true) @RequestParam
                                                @NotEmpty(message = "Список должностей не может быть пустым")
                                                @Size(min = 1, message = "Должна быть хотя бы одна должность")
                                                List<@Min(value = 1, message = "ID должности должен быть положительным") Long> positionIds,
        @Parameter(description = "ID сотрудника", required = true) @PathVariable @Min(1) Long employeeId){
        employeeService.changeEmployeePosition(positionIds, employeeId);
        return ResponseEntity.ok("Позиция сотрудника изменена!");
    }
    @PostMapping("/addPosition/{positionId}/{employeeId}")
    @Operation(
        summary = "Добавить должность сотруднику",
        description = "Добавляет новую должность к существующим должностям сотрудника"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Должность успешно добавлена"),
        @ApiResponse(responseCode = "404", description = "Сотрудник или должность не найдены")
    })
    public ResponseEntity<?> addPosition(
        @Parameter(description = "ID должности", required = true) @PathVariable @Min(1) Long positionId,
        @Parameter(description = "ID сотрудника", required = true) @PathVariable @Min(1) Long employeeId){
        employeeService.addEmployeePosition(positionId, employeeId);
        return ResponseEntity.ok("Должность добавлена!");
    }
    @DeleteMapping("/removePosition/{positionId}/{employeeId}")
    @Operation(
        summary = "Удалить должность у сотрудника",
        description = "Удаляет указанную должность у сотрудника"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Должность успешно удалена"),
        @ApiResponse(responseCode = "404", description = "Сотрудник или должность не найдены")
    })
    public ResponseEntity<?> removePosition(
        @Parameter(description = "ID должности", required = true) @PathVariable @Min(1) Long positionId,
        @Parameter(description = "ID сотрудника", required = true) @PathVariable @Min(1) Long employeeId){
        employeeService.removeEmployeePosition(positionId, employeeId);
        return ResponseEntity.ok("Должность удалена!");
    }

    @GetMapping("/myProfile")
    @Operation(
        summary = "Получить мой профиль (сотрудник)",
        description = "Возвращает профиль текущего авторизованного сотрудника"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Профиль получен"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<EmployeeResponse> myProfile(
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(employeeMapper.toResponse(employeeService.getMyProfile(user.getId())));
    }
}
