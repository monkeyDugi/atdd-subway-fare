package nextstep.subway.applicaion.dto;

public class FavoritesRequest {
    private Long source;
    private Long target;

    FavoritesRequest() {
    }

    public FavoritesRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
