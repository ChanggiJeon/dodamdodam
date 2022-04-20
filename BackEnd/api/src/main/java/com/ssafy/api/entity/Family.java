package com.ssafy.api.entity;

import lombok.*;

import javax.persistence.*;
import java.io.File;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(unique = true)
    private String code;

    @Setter
    @Column
    private String picture;

}
