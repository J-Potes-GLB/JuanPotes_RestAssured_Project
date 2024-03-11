package com.jp.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String name;
    private String lastName;
    private String country;
    private String city;
    private String email;
    private String phone;
    private String id;

    public Client(String name, String lastName, String country, String city, String email, String phone) {
        this.name = name;
        this.lastName = lastName;
        this.country = country;
        this.city = city;
        this.email = email;
        this.phone = phone;
    }
}
