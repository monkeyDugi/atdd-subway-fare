package nextstep.subway.domain;

import nextstep.subway.ui.exception.LineException;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int additionFare;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.additionFare = 0;
    }

    public Line(String name, String color, int additionFare) {
        this.name = name;
        this.color = color;
        validateAddFare(additionFare);
        this.additionFare = additionFare;
    }

    public void addSection(Section newSection) {
        sections.addSection(newSection);
    }

    public void removeSection(Station downStation) {
        sections.remove(downStation);
    }

    public void updateLine(String name,  String color, int additionFare) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
        validateAddFare(additionFare);
        this.additionFare = additionFare;
    }

    private void validateAddFare(int additionFare) {
        if (additionFare < 0) {
            throw new LineException("추가 요금은 최소 0원 이상이어야 합니다.");
        }
    }

    public boolean isEmptySections() {
        return sections.isEmpty();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public int pathTotalDistance(List<Station> stations) {
        return sections.pathTotalDistance(stations);
    }

    public int pathTotalDuration(List<Station> stations) {
        return sections.pathTotalDuration(stations);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getAdditionFare() {
        return additionFare;
    }

    public int sectionsSize() {
        return sections.size();
    }
}
