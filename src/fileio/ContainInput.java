package fileio;

import java.util.List;

public class ContainInput {
    private List<String> actors;
    private List<String> genre;

    public ContainInput() {
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "ContainInput{" +
                "actors=" + actors +
                ", genre=" + genre +
                '}';
    }
}
