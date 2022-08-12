package com.example.demo.SAGA.query.queries;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class FindParcelQuery {
    private String trackingNumber;
}
