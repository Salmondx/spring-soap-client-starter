package com.salmondx.cxf.client.example;

import com.salmondx.cxf.client.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Salmond on 09/11/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
    @Field("accNumber")
    private String accountNumber;
    private Long balance;
    @Field("currencyUnit")
    private String currency;
}
