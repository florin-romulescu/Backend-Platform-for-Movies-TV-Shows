package fileio;

import java.util.List;

public final class ContainInput {
    private List<String> actors;
    private List<String> genre;

    public ContainInput() {
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(final List<String> actors) {
        this.actors = actors;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(final List<String> genre) {
        this.genre = genre;
    }

}
