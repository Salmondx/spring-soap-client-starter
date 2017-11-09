package com.salmondx.cxf.client.example;

import com.salmondx.cxf.client.annotation.Field;
import com.salmondx.cxf.client.annotation.Response;
import lombok.Data;

/**
 * Created by Salmond on 09/11/17.
 */
@Data
@Response("resultSetRow")
public class AccountInfoRow {
    @Field("accNumber")
    private String accountNumber;
    private Long balance;
    @Field("currencyUnit")
    private String currency;
}
