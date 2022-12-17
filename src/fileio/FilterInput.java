package fileio;

public final class FilterInput {
    private SortInput sort;
    private ContainInput contains;

    public FilterInput() {
    }

    public SortInput getSort() {
        return sort;
    }

    public void setSort(final SortInput sort) {
        this.sort = sort;
    }

    public ContainInput getContains() {
        return contains;
    }

    public void setContains(final ContainInput contains) {
        this.contains = contains;
    }

}
