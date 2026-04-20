package com.example.nexobank2.controller;

import com.example.nexobank2.dto.ClientRequest;
import com.example.nexobank2.dto.ClientResponse;

import com.example.nexobank2.entity.Client;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.ClientStatus;
import com.example.nexobank2.mapper.ClientMapper;
import com.example.nexobank2.service.impl.ClientServiceImpl;
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
@Tag(name = "Контроллер клиента")
@Validated
public class ClientController {
    private final ClientMapper clientMapper;
    private final ClientServiceImpl clientServiceImpl;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @Valid ClientRequest request){
        Client client = clientMapper.toEntity(request);
        clientServiceImpl.save(client);
        return ResponseEntity.ok("Аккаунт создан! на вашу почту был отправлен код, пожалуйста, подтвердите аккаунт");
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<ClientResponse>> getAll(@RequestParam @NotNull ClientStatus status, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(clientMapper.toResponseList(clientServiceImpl.findAll(status, page, size)));
    }
    @GetMapping("/getBy/{id}")
    public ResponseEntity<ClientResponse> getById(@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(clientMapper.toResponse(clientServiceImpl.findById(id)));
    }
    @DeleteMapping("/deleteBy/{id}")
    public ResponseEntity<?> deleteById(@PathVariable @Min(1) Long id){
        clientServiceImpl.deleteById(id);
        return ResponseEntity.ok("Клиент удален");
    }
    @GetMapping("/findByDate")
    public ResponseEntity<List<ClientResponse>> findByDate(@RequestParam @NotNull LocalDateTime date){
        return ResponseEntity.ok(clientMapper.toResponseList(clientServiceImpl.findByCreatedAt(date)));
    }
    @GetMapping("/findByUserId")
    public ResponseEntity<ClientResponse> findByUserId(@RequestParam @Min(1) Long userId){
        return ResponseEntity.ok(clientMapper.toResponse(clientServiceImpl.findByUserId(userId)));
    }
    @GetMapping("/findByDateRange")
    public ResponseEntity<List<ClientResponse>> findByDateRange(@RequestParam @NotNull ClientStatus status, @RequestParam @NotNull LocalDateTime startDate, @RequestParam @NotNull LocalDateTime endDate){
        return ResponseEntity.ok(clientMapper.toResponseList(clientServiceImpl.findByDateRange(status, startDate, endDate)));
    }
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable @Min(1) Long id, @RequestParam @NotNull ClientStatus status){
        clientServiceImpl.updateClientStatus(id, status);
        return ResponseEntity.ok("Статус клиента изменен");
    }
    @GetMapping("/getClientCountByStatus")
    public ResponseEntity<Integer> getClientCountByStatus(@RequestParam @NotNull ClientStatus status){
        return ResponseEntity.ok(clientServiceImpl.getClientCountByStatus(status));
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<ClientResponse> myProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(clientMapper.toResponse(clientServiceImpl.getMyProfile(user.getId())));
}
}
