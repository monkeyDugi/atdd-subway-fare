package nextstep.subway.domain;

import nextstep.subway.ui.exception.PathException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PathFinder {
    private final Lines lines;

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPathDuration;

    public PathFinder(List<Line> lines) {
        this.lines = new Lines(lines);
        dijkstraShortestPath = createDijkstraShortestPath(PathType.DISTANCE);
        dijkstraShortestPathDuration = createDijkstraShortestPath(PathType.DURATION);
    }

    public List<Station> shortsPathStations(Station source, Station target, PathType type) {
        GraphPath<Station, DefaultWeightedEdge> path;
        try {
            path = type.getPath(dijkstraShortestPath, dijkstraShortestPathDuration, source, target);
        } catch (IllegalArgumentException e) {
            throw new PathException("노선에 등록되지 않은 역입니다.");
        }
        if (path == null) {
            throw new PathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
        return path.getVertexList();
    }

    public Path shortsPath(Station source, Station target, PathType type) {
        List<Station> stations = shortsPathStations(source, target, type);

        if (type == PathType.DISTANCE) {
            return new Path(type.getPathWeight(dijkstraShortestPath, source, target), lines.pathTotalDuration(stations));
        }
        return new Path(lines.pathTotalDistance(stations), type.getPathWeight(dijkstraShortestPathDuration, source, target));
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createDijkstraShortestPath(PathType type) {
        Set<Station> stations = lines.getStations();
        List<Section> sections = lines.getSections();

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : stations) {
            graph.addVertex(station);
        }

        type.setEdgeWeight(sections, graph);
        return new DijkstraShortestPath<>(graph);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathFinder that = (PathFinder) o;
        return Objects.equals(lines, that.lines) && Objects.equals(dijkstraShortestPath, that.dijkstraShortestPath) && Objects.equals(dijkstraShortestPathDuration, that.dijkstraShortestPathDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines, dijkstraShortestPath, dijkstraShortestPathDuration);
    }
}
