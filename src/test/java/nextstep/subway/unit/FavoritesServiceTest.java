package nextstep.subway.unit;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.FavoritesService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoritesResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorites;
import nextstep.subway.domain.FavoritesRepository;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoritesServiceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final Long MEMBER_ID = 1L;

    @Mock
    private FavoritesRepository favoritesRepository;
    @Mock
    private StationService stationService;
    @Mock
    private MemberRepository memberRepository;

    private FavoritesService favoritesService;

    private Station 교대역;
    private Station 강남역;
    private Station 역삼역;
    private Member member;

    @BeforeEach
    void setUp() {
        교대역 = new Station(1L, "교대역");
        강남역 = new Station(2L, "강남역");
        역삼역 = new Station(3L, "역삼역");
        member = new Member(MEMBER_ID, EMAIL, PASSWORD, AGE);

        favoritesService = new FavoritesService(favoritesRepository ,stationService, memberRepository);
    }

    @DisplayName("내 즐겨찾기 목록 조회")
    @Test
    void getFavorites() {
        // given
        LoginMember loginMember = new LoginMember(MEMBER_ID, EMAIL, PASSWORD, AGE);

        Favorites favorites1 = new Favorites(1L, member, 교대역, 강남역);
        Favorites favorites2 = new Favorites(2L, member, 교대역, 역삼역);
        List<Favorites> favoriteses = Arrays.asList(favorites1, favorites2);
        when(favoritesRepository.findByMemberId(MEMBER_ID)).thenReturn(favoriteses);

        // when
        List<FavoritesResponse> favoritesResponses = favoritesService.findByMemberId(loginMember);

        // then
        assertThat(favoritesResponses.size()).isEqualTo(2);

        FavoritesResponse favoritesResponse1 = new FavoritesResponse(favorites1.getId(), StationResponse.createStationResponse(교대역), StationResponse.createStationResponse(강남역));
        FavoritesResponse favoritesResponse2 = new FavoritesResponse(favorites2.getId(), StationResponse.createStationResponse(교대역), StationResponse.createStationResponse(역삼역));
        assertThat(favoritesResponses.get(0)).isEqualTo(favoritesResponse1);
        assertThat(favoritesResponses.get(1)).isEqualTo(favoritesResponse2);
    }
}
