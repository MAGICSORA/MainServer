package com.example.servertest.main.member.entity;

import com.example.servertest.main.member.type.MemberType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@DynamicUpdate
public class
Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    @Email
    private String email;

//    @Size(min = 6)
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    private LocalDateTime regDt;
    private LocalDateTime updateDt;

}
