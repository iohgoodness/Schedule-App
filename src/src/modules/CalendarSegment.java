package modules;

public class CalendarSegment {
    private String segmentData;
    private String segmentDate;

    public CalendarSegment(String segmentData, String segmentDate) {
        this.segmentData = segmentData;
        this.segmentDate = segmentDate;
    }

    public String getSegmentData() {
        return segmentData;
    }

    public void setSegmentData(String segmentData) {
        this.segmentData = segmentData;
    }

    public String getSegmentDate() {
        return segmentDate;
    }

    public void setSegmentDate(String segmentDate) {
        this.segmentDate = segmentDate;
    }
}
