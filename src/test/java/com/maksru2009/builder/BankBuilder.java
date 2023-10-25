package com.maksru2009.builder;

import com.maksru2009.entity.Bank;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "sBank")
public class BankBuilder implements TestBuilder<Bank>{
    private int id = 1;
    private String name = "Bank";
    @Override
    public Bank build() {
        return Bank.builder()
                .name(name)
                .id(id)
                .build();
    }
}
