package com.user.member.controller;

import com.google.gson.Gson;
import com.user.member.dto.MemberDto;
import com.user.member.entity.Member;
import com.user.member.mapper.MemberMapper;
import com.user.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class) // @WebMvcTest에는 Jpa 생성과 관련된 기능이 전혀 없기 때문에 JPA Auditing 기능을 사용할 때 필요하다
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean  // 컨트롤러에서 사용하는 서비스가 @WebMvcTest로 인해 등록되지 않았기 때문에 @MockBean을 이용하여 의존성 대체
    private MemberService memberService;

    @MockBean
    private MemberMapper mapper;

    @Test
    void postMemberTest() throws Exception {
        //given
        MemberDto.Post post =
                MemberDto.Post.builder()
                        .email("hgd@gmail.com")
                        .password("test1234!")
                        .displayName("홍길동")
                        .build();
        String json = gson.toJson(post);

        MemberDto.Response response =
                MemberDto.Response.builder()
                        .email("hgd@gmail.com")
                        .displayName("홍길동")
                        .build();

        given(memberService.createMember(Mockito.any(Member.class))).willReturn(new Member());
        given(mapper.memberPostDtoToMember(Mockito.any(MemberDto.Post.class))).willReturn(new Member());
        given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                post("/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.displayName").value(post.getDisplayName()));
    }

    @Test
    void patchMemberTest() throws Exception {
        //given
        MemberDto.Patch patch =
                MemberDto.Patch.builder()
                        .memberId(1L)
                        .displayName("홍길동")
                        .password("test1234!")
                        .build();
        String json = gson.toJson(patch);

        MemberDto.Response response =
                MemberDto.Response.builder()
                        .memberId(1L)
                        .displayName("홍길동")
                        .build();

        given(mapper.memberPatchDtoToMember(Mockito.any(MemberDto.Patch.class))).willReturn(new Member());
        given(memberService.updateMember(Mockito.any(Member.class))).willReturn(new Member());
        given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/members/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value(patch.getDisplayName()));
    }

    @Test
    void getMemberTest() throws Exception {
        //given
        MemberDto.Response response =
                MemberDto.Response.builder()
                        .memberId(1L)
                        .email("hgd@gmail.com")
                        .displayName("홍길동")
                        .build();

        given(memberService.findMember(Mockito.anyLong())).willReturn(new Member());
        given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                get("/members/1")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(response.getMemberId()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.displayName").value(response.getDisplayName()));
    }

    @Test
    void deleteMemberTest() throws Exception {
        //given
        MemberDto.Response response =
                MemberDto.Response.builder()
                        .memberId(1L)
                        .email("hgd@gmail.com")
                        .displayName("홍길동")
                        .build();

        given(memberService.deleteMember(Mockito.anyLong())).willReturn(new Member());
        given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                delete("/members/1")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(response.getMemberId()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.displayName").value(response.getDisplayName()));
    }
}
