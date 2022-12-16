package fileio;

public class ActionInput {
    String type;
    String page;
    String feature;
    CredentialsInput credentials;
    String movie;
    String startsWith;
    String count;
    Integer rate;
    FilterInput filters;

    public ActionInput() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovieTitle(String movie) {
        this.movie = movie;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public CredentialsInput getCredentials() {
        return credentials;
    }

    public void setCredentials(CredentialsInput credentials) {
        this.credentials = credentials;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public void setStartsWith(String startsWith) {
        this.startsWith = startsWith;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public FilterInput getFilters() {
        return filters;
    }

    public void setFilters(FilterInput filters) {
        this.filters = filters;
    }

    @Override
    public String toString() {
        return "ActionInput{" +
                "command='" + type + '\'' +
                ", page='" + page + '\'' +
                ", movieTitle='" + movie + '\'' +
                ", feature='" + feature + '\'' +
                ", credentials=" + credentials +
                ", startsWith='" + startsWith + '\'' +
                ", count='" + count + '\'' +
                ", rate=" + rate +
                '}';
    }
}
