package se.chalmers.agile5.entities;

/**
 * Created by armin on 4/29/14.
 */
public class RoadMapEntry {

    private String title;

    private String description;

    public RoadMapEntry(String title) {
        this(title, "");
    }

    public RoadMapEntry(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return title;
    }
}
