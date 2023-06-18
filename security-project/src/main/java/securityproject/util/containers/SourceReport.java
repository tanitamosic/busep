package securityproject.util.containers;


public class SourceReport {
    public String source;
    public Integer numOfLogs;

    public SourceReport(String source, Integer amount) {
        this.source = source;
        this.numOfLogs = amount;
    }
}
