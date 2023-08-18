package com.user.member.controller;

import com.google.gson.Gson;
import com.user.member.dto.MemberDto;
import com.user.member.entity.Member;
import com.user.member.mapper.MemberMapper;
import com.user.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

        BDDMockito.given(memberService.createMember(Mockito.any(Member.class))).willReturn(new Member());
        BDDMockito.given(mapper.memberPostDtoToMember(Mockito.any(MemberDto.Post.class))).willReturn(new Member());
        BDDMockito.given(mapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        //then
        actions
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(post.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(post.getDisplayName()));
    }

    @Test
    void patchMemberTest() {
        //given
        MemberDto.Patch patch =
                MemberDto.Patch.builder()
                        .memberId(1L)
                        .displayName("")
                        .build();


        //when

        //then
    }

    @Test
    void getMemberTest() {
        //given

        //when

        //then
    }

    @Test
    void deleteMemberTest() {
        //given

        //when

        //then
    }
}
