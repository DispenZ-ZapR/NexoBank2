package com.example.nexobank2.Controller;

import com.example.nexobank2.dto.ClientRequest;
import com.example.nexobank2.dto.ClientResponse;
import com.example.nexobank2.dto.UserRequest;
import com.example.nexobank2.entity.Client;
import com.example.nexobank2.entity.User;
import com.example.nexobank2.enums.ClientStatus;
import com.example.nexobank2.mapper.ClientMapper;
import com.example.nexobank2.mapper.UserMapper;
import com.example.nexobank2.service.impl.ClientServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/client")
@AllArgsConstructor
@Tag(name = "Контроллер клиента")
public class ClientController {
    private final ClientMapper clientMapper;
    private final ClientServiceImpl clientServiceImpl;
    private final UserMapper userMapper;
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ClientRequest request){
        Client client = clientMapper.toEntity(request);
        clientServiceImpl.save(client);
        return ResponseEntity.ok("Аккаунт создан! на вашу почту был отправлен код, пожалуйста, подтвердите аккаунт");
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<ClientResponse>> getAll(@RequestParam ClientStatus status){
        return ResponseEntity.ok(clientMapper.toResponseList(clientServiceImpl.findAll(status)));
    }
    @GetMapping("/getById")
    public ResponseEntity<ClientResponse> getById(@RequestParam Long id){
        return ResponseEntity.ok(clientMapper.toResponse(clientServiceImpl.findById(id)));
    }
    @DeleteMapping("/deleteById")
    public ResponseEntity<?> deleteById(@RequestParam Long id){
        clientServiceImpl.deleteById(id);
        return ResponseEntity.ok("Клиент удален");
    }
    @GetMapping("/findByDate")
    public ResponseEntity<List<ClientResponse>> findByDate(@RequestParam LocalDateTime date){
        return ResponseEntity.ok(clientMapper.toResponseList(clientServiceImpl.findByCreatedAt(date)));
    }
    @GetMapping("/findByUserId")
    public ResponseEntity<ClientResponse> findByUserId(@RequestParam Long userId){
        return clientServiceImpl.findByUserId(userId)
                .map(client -> ResponseEntity.ok(clientMapper.toResponse(client)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/findByDateRange")
    public ResponseEntity<List<ClientResponse>> findByDateRange(@RequestParam ClientStatus status, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate){
        return ResponseEntity.ok(clientMapper.toResponseList(clientServiceImpl.findByDateRange(status, startDate, endDate)));
    }
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam ClientStatus status){
        clientServiceImpl.updateClientStatus(id, status);
        return ResponseEntity.ok("Статус клиента изменен");
    }
    @GetMapping("/getClientCountByStatus")
    public ResponseEntity<Integer> getClientCountByStatus(@RequestParam ClientStatus status){
        return ResponseEntity.ok(clientServiceImpl.getClientCountByStatus(status));
    }
}
