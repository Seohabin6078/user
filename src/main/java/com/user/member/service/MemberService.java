package com.user.member.service;

import com.user.member.entity.Member;
import com.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    // todo 이후에 예외처리 더 친철하게 하는 방향으로 변경하기!
    private final MemberRepository memberRepository;

    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());

        return memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        Member findMember = findMember(member.getMemberId());
        Optional.ofNullable(member.getPassword())
                .ifPresent(findMember::setPassword);
        Optional.ofNullable(member.getDisplayName())
                .ifPresent(findMember::setDisplayName);
        Optional.ofNullable(member.getMemberStatus())
                .ifPresent(findMember::setMemberStatus);

        return memberRepository.save(findMember);
    }

    public Member findMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(RuntimeException::new);
        if (member.getMemberStatus() != Member.MemberStatus.MEMBER_ACTIVE) {
            throw new RuntimeException();
        }
        return member;
    }

    // 최근 가입한 순서대로 모든 회원을 반환
    public List<Member> findAllMembers() {
        return memberRepository.findAllByOrderByCreatedAtDesc();
    }

    // soft delete 적용
    public Member deleteMember(Long memberId) {
        Member member = findMember(memberId);
        member.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        return memberRepository.save(member);
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            throw new RuntimeException();
        }
    }
}
