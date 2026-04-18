package com.example.nexobank2.dto.recordDto;

import com.example.nexobank2.entity.Operation;
import com.example.nexobank2.entity.Transaction;

import java.util.List;

public record OperationDetails(
        Operation operation,
        List<Transaction> transactions
) {
}
