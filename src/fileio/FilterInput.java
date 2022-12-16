package fileio;

public class FilterInput {
    private SortInput sort;
    private ContainInput contains;

    public FilterInput() {
    }

    public SortInput getSort() {
        return sort;
    }

    public void setSort(SortInput sort) {
        this.sort = sort;
    }

    public ContainInput getContains() {
        return contains;
    }

    public void setContains(ContainInput contains) {
        this.contains = contains;
    }

    @Override
    public String toString() {
        return "FilterInput{" +
                "sort=" + sort +
                ", contains=" + contains +
                '}';
    }
}
