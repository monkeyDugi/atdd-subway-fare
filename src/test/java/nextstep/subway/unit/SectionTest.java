package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    Station 가양역;
    Station 증미역;
    Station 등촌역;
    Station 신목동역;
    Line line;
    int tenDistance;
    int fourDistance;
    int fiveDuration;
    int threeDuration;

    @BeforeEach
    void setUp() {
        가양역 = new Station("가양역");
        증미역 = new Station("증미역");
        등촌역 = new Station("등촌역");
        신목동역 = new Station("신목동역");
        line = new Line("9호선", "금색");
        tenDistance = 10;
        fourDistance = 4;
        fiveDuration = 5;
        threeDuration = 3;
    }

    @DisplayName("구간 사이에 역 추가")
    @Test
    void updateAddLineBetweenSection() {
        // given
        Section section = new Section(line, 가양역, 등촌역, tenDistance, fiveDuration);
        Section betweenSection = new Section(line, 가양역, 증미역, fourDistance, threeDuration);

        // when
        section.updateAddLineBetweenSection(betweenSection);

        // then
        assertThat(section.getUpStation()).isEqualTo(증미역);
        assertThat(section.getDownStation()).isEqualTo(등촌역);
        assertThat(section.getDistance()).isEqualTo(6);
        assertThat(betweenSection.getDistance()).isEqualTo(4);
        assertThat(section.getDuration()).isEqualTo(2);
        assertThat(betweenSection.getDuration()).isEqualTo(3);
    }

    @DisplayName("구간 사이의 역 삭제 시 다음 구간의 상행역에 삭제된 하행역으로 변경한다.")
    @Test
    void updateRemoveLineBetweenSection() {
        // given
        Section removeSection = new Section(line, 증미역, 등촌역, fourDistance, fiveDuration);
        Section targetSection = new Section(line, 등촌역, 신목동역, fourDistance, threeDuration);

        // when
        targetSection.updateRemoveLineBetweenSection(removeSection);

        // then
        assertThat(targetSection.getUpStation()).isEqualTo(증미역);
        assertThat(targetSection.getDistance()).isEqualTo(8);
        assertThat(targetSection.getDuration()).isEqualTo(8);
    }
}
