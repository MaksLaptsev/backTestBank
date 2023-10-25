package com.maksru2009.builder;

import com.maksru2009.entity.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "sUser")
public class UserBuilder implements TestBuilder<User>{
    private int id = 1;
    private String name = "Vasya";
    private String lastName = "Novik";
    private String secondName = "Petrovich";
    private String phoneNumber = "+3752225632012";
    @Override
    public User build() {
        return User.builder()
                .id(id)
                .name(name)
                .lastName(lastName)
                .secondName(secondName)
                .phoneNumber(phoneNumber)
                .build();
    }
}
