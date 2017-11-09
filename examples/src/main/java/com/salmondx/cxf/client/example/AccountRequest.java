package com.salmondx.cxf.client.example;

import com.salmondx.cxf.client.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * Created by Salmond on 09/11/17.
 */
@Data
@AllArgsConstructor
public class AccountRequest {
    @Field("accNumber")
    private String accountNumber;
    @Field("date")
    private LocalDate creationDate;
}
