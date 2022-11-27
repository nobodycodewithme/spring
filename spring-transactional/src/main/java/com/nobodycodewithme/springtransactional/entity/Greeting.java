package com.nobodycodewithme.springtransactional.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Greeting {
    private Long id;
    private String message;
}
