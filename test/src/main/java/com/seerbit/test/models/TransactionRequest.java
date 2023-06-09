package com.seerbit.test.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionRequest {
    private String amount;
    private String timestamp;

}

