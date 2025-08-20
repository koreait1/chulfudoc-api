package org.backend.chulfudoc.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.backend.chulfudoc.global.entities.BaseEntity;
import org.backend.chulfudoc.member.constants.Authority;
import org.backend.chulfudoc.member.constants.SocialChannel;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(indexes = {
        @Index(name="idx_member_created_at", columnList = "createdAt DESC"),
        @Index(name="idx_member_name", columnList = "name"),
        @Index(name="idx_member_mobile", columnList = "mobile"),
        @Index(name="idx_member_social", columnList = "socialChannel, socialToken")
})
public class Member extends BaseEntity implements Serializable {
    // 변경하실때 오늘의 할일 쪽 구글 시트에 클래스랑 작업자 적어주세요
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String PUUID;

    @Column(length = 50, unique = true, nullable = false)
    private String userId;

    @Column(length=75, unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(length=65)
    private String password;

    @Column(length=45, nullable = false)
    private String name;

    @Column(length=15, nullable = false)
    private String mobile;

    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.MEMBER;

    private boolean termsAgree;

    private boolean locked; // 계정 중지 상태인지

    private LocalDateTime expired; // 계정 만료 일자, null이면 만료 X

    private LocalDateTime credentialChangedAt; // 비밀번호 변경 일시

    @Enumerated(EnumType.STRING)
    public SocialChannel socialChannel;

    @Column(length = 45)
    public String socialToken;

    public boolean isAdmin() {
        return authority != null && authority == Authority.ADMIN;
    }

}
