package com.user.member.mapper;

import com.user.member.dto.MemberDto;
import com.user.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberDto.Post post);

    Member memberPatchDtoToMember(MemberDto.Patch patch);

    MemberDto.Response memberToMemberResponseDto(Member member);
}
