package com.example.nexobank2.controller;

import com.example.nexobank2.dto.ClientRequest;
import com.example.nexobank2.dto.ClientResponse;

import com.example.nexobank2.entity.Client;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.ClientStatus;
import com.example.nexobank2.mapper.ClientMapper;
import com.example.nexobank2.service.ClientService;
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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/client")
@AllArgsConstructor
@Tag(name = "Клиенты", description = "API для управления клиентами банка")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class ClientController {
    private final ClientMapper clientMapper;
    private final ClientService clientServiceImpl;
    @PostMapping("/save")
    @Operation(
        summary = "Регистрация нового клиента",
        description = "Создает нового клиента с паспортными данными. На указанную почту отправляется код подтверждения"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Клиент успешно зарегистрирован"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные"),
        @ApiResponse(responseCode = "409", description = "Клиент с таким email уже существует")
    })
    public ResponseEntity<?> save(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные клиента для регистрации",
            required = true
        ) @RequestBody @Valid ClientRequest request){
        Client client = clientMapper.toEntity(request);
        clientServiceImpl.save(client);
        return ResponseEntity.ok("Аккаунт создан! на вашу почту был отправлен код, пожалуйста, подтвердите аккаунт");
    }
    @GetMapping("/getAll")
    @Operation(
        summary = "Получить всех клиентов по статусу",
        description = "Возвращает постраничный список клиентов с указанным статусом"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список клиентов получен")
    })
    public ResponseEntity<List<ClientResponse>> getAll(
        @Parameter(description = "Статус клиента", required = true) @RequestParam @NotNull ClientStatus status,
        @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(clientMapper.toResponseList(clientServiceImpl.findAll(status, page, size)));
    }
    @GetMapping("/getBy/{id}")
    @Operation(
        summary = "Получить клиента по ID",
        description = "Возвращает детальную информацию о клиенте"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Клиент найден"),
        @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public ResponseEntity<ClientResponse> getById(
        @Parameter(description = "ID клиента", required = true) @PathVariable @Min(1) Long id){
        return ResponseEntity.ok(clientMapper.toResponse(clientServiceImpl.findById(id)));
    }
    @DeleteMapping("/deleteBy/{id}")
    @Operation(
        summary = "Удалить клиента",
        description = "Удаляет клиента из системы (мягкое удаление)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Клиент успешно удален"),
        @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public ResponseEntity<?> deleteById(
        @Parameter(description = "ID клиента", required = true) @PathVariable @Min(1) Long id){
        clientServiceImpl.deleteById(id);
        return ResponseEntity.ok("Клиент удален");
    }
    @GetMapping("/findByDate")
    @Operation(
        summary = "Найти клиентов по дате регистрации",
        description = "Возвращает список клиентов, зарегистрированных в указанную дату"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список клиентов получен")
    })
    public ResponseEntity<List<ClientResponse>> findByDate(
        @Parameter(description = "Дата регистрации", required = true) @RequestParam @NotNull LocalDateTime date){
        return ResponseEntity.ok(clientMapper.toResponseList(clientServiceImpl.findByCreatedAt(date)));
    }
    @GetMapping("/findByUserId")
    @Operation(
        summary = "Найти клиента по ID пользователя",
        description = "Возвращает клиента по идентификатору связанного пользователя"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Клиент найден"),
        @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public ResponseEntity<ClientResponse> findByUserId(
        @Parameter(description = "ID пользователя", required = true) @RequestParam @Min(1) Long userId){
        return ResponseEntity.ok(clientMapper.toResponse(clientServiceImpl.findByUserId(userId)));
    }
    @GetMapping("/findByDateRange")
    @Operation(
        summary = "Найти клиентов за период",
        description = "Возвращает список клиентов с указанным статусом, зарегистрированных в указанном диапазоне дат"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Список клиентов получен")
    })
    public ResponseEntity<List<ClientResponse>> findByDateRange(
        @Parameter(description = "Статус клиента", required = true) @RequestParam @NotNull ClientStatus status,
        @Parameter(description = "Начальная дата", required = true) @RequestParam @NotNull LocalDateTime startDate,
        @Parameter(description = "Конечная дата", required = true) @RequestParam @NotNull LocalDateTime endDate){
        return ResponseEntity.ok(clientMapper.toResponseList(clientServiceImpl.findByDateRange(status, startDate, endDate)));
    }
    @PutMapping("/updateStatus/{id}")
    @Operation(
        summary = "Изменить статус клиента",
        description = "Обновляет статус клиента (ACTIVE, BLOCKED, PENDING)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Статус успешно изменен"),
        @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    public ResponseEntity<?> updateStatus(
        @Parameter(description = "ID клиента", required = true) @PathVariable @Min(1) Long id,
        @Parameter(description = "Новый статус", required = true) @RequestParam @NotNull ClientStatus status){
        clientServiceImpl.updateClientStatus(id, status);
        return ResponseEntity.ok("Статус клиента изменен");
    }
    @GetMapping("/getClientCountByStatus")
    @Operation(
        summary = "Получить количество клиентов по статусу",
        description = "Возвращает количество клиентов с указанным статусом"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Количество получено")
    })
    public ResponseEntity<Integer> getClientCountByStatus(
        @Parameter(description = "Статус клиента", required = true) @RequestParam @NotNull ClientStatus status){
        return ResponseEntity.ok(clientServiceImpl.getClientCountByStatus(status));
    }

    @GetMapping("/getMyProfile")
    @Operation(
        summary = "Получить мой профиль",
        description = "Возвращает профиль текущего авторизованного клиента"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Профиль получен"),
        @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public ResponseEntity<ClientResponse> myProfile(
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(clientMapper.toResponse(clientServiceImpl.getMyProfile(user.getId())));
}
}
