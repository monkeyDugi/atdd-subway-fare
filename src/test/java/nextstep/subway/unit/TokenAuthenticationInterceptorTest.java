package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthenticationTokenConverter;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.authentication.TokenAuthenticationTokenConverter;
import nextstep.auth.authentication.UserDetailService;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    public static final Long MEMBER_ID = 1L;
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Test
    void convert() throws IOException {
        // given
        AuthenticationTokenConverter converter = new TokenAuthenticationTokenConverter(new ObjectMapper());
        MockHttpServletRequest request = createMockRequest();

        // when
        AuthenticationToken authenticationToken = converter.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @Test
    void afterAuthentication() throws IOException {
        // given
        UserDetailService userDetailsService = mock(UserDetailService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        AuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, new ObjectMapper());

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(MEMBER_ID, EMAIL, PASSWORD, AGE));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        Authentication authentication = interceptor.authenticate(authenticationToken);

        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        interceptor.afterAuthentication(request, response, authentication);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
