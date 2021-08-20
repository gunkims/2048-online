package dev.gunlog.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gunlog.config.SecurityConfig;
import dev.gunlog.member.domain.Member;
import dev.gunlog.member.domain.MemberRepository;
import dev.gunlog.member.domain.Role;
import dev.gunlog.security.model.LoginRequest;
import dev.gunlog.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtUtil jwtUtil;

    public static final String MEMBER_ID = "testMan";
    public static final String PASSWORD = "test";

    @Value("${jwt.token.issuer}")
    private String issuer;

    @BeforeEach
    void createMember() {
        memberRepository.save(Member.builder()
                .memberId(MEMBER_ID)
                .name("TEST USER")
                .password(passwordEncoder.encode(PASSWORD))
                .role(Role.USER)
                .build());
    }
    @Test
    @DisplayName("로그인 테스트")
    void signInTest() throws Exception {
        final MvcResult result = mockMvc.perform(post(SecurityConfig.AUTHENTICATION_URL)
                .content(getLoginInfo()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        final String jwtToken = result.getResponse().getContentAsString().replaceAll("\"", "");

        final Claims body = jwtUtil.parserToken(jwtToken).getBody();
        assertThat(body.getSubject()).isEqualTo(MEMBER_ID);
        assertThat(body.getIssuer()).isEqualTo(issuer);
    }
    private String getLoginInfo() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(LoginRequest.builder()
                .username(MEMBER_ID)
                .password(PASSWORD)
                .build());
    }
}
