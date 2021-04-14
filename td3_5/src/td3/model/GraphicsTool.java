package td3.model;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart.Series;

public class GraphicsTool {
    private final List<Group> groups;

    public GraphicsTool(List<Group> groups) {
        this.groups = groups;
    }

    public List<Data> contactPieChartData() {
        List<Data> ldata = (List)this.groups.stream().map((group) -> {
            return new Data(group.getName(), (double)group.contactSize());
        }).collect(Collectors.toList());
        return ldata;
    }

    public Series<String, Number> citiesSeriesData() {
        Series<String, Number> citySerie = new Series();
        ((Map)this.groups.stream().flatMap((group) -> {
            return group.getContacts().stream();
        }).collect(Collectors.groupingBy((contact) -> {
            return contact.getCity();
        }, Collectors.counting()))).forEach((city, nb) -> {
            citySerie.getData().add(new javafx.scene.chart.XYChart.Data(city, nb));
        });
        return citySerie;
    }
}
