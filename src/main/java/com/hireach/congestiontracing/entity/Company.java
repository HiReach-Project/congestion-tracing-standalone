package com.hireach.congestiontracing.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(generator = "company_history_gen_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "company_history_gen_seq", sequenceName = "company_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String accessKey;

}
