package com.example.nexobank2.dto;

import com.example.nexobank2.enums.ClientStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequest {
    private Long userId;
    private Integer creditRating;
    private ClientStatus clientStatus;
}
