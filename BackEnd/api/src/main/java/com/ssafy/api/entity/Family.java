package com.ssafy.api.entity;

<<<<<<< HEAD
import lombok.*;

import javax.persistence.*;
import java.io.File;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
=======
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "family")
>>>>>>> 8f090ef8548a32013b2b1f58e23371013e810eab
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

<<<<<<< HEAD
    @Setter
    @Column(unique = true)
    private String code;

    @Setter
    @Column
    private String picture;
=======
>>>>>>> 8f090ef8548a32013b2b1f58e23371013e810eab

}
