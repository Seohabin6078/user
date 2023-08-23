package com.user.member.entity;

import com.user.audit.Auditable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Getter
@NoArgsConstructor
@Entity
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

    @Builder
    public Member(Long memberId, String email, String password, String displayName) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void changeMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

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
