package com.user.member.entity;

import com.user.audit.Auditable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Member extends Auditable {
    // todo 나중에 db 테이블 직접 확인해서 카멜 케이스가 스네이크 케이스로 자동 변경되는지 확인해보기!!!
    // 안바뀌면 name 옵션으로 직접 바꾸기

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 24)
    private String password;

    @Column(nullable = false, length = 10)
    private String displayName;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Getter
    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        private final String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }
}
