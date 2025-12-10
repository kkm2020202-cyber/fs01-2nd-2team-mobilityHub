// 유저 테이블

package com.iot2ndproject.mobilityhub.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.integration.annotation.Default;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    private String userId; // 유저아이디

    @Column(nullable = false)
    private String password; // 패스워드

    @Column(nullable = false)
    private String userName; // 이름

    @Column(nullable = false)
    private String tel; // 휴대폰번호

    @CreationTimestamp
    private LocalDateTime createDate; // 가입날짜

    @Column
    private String role;
    // 회원가입시
    public UserEntity(String userId, String password, String userName, String tel, String role) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.tel = tel;
        this.role = role;
    }
}
