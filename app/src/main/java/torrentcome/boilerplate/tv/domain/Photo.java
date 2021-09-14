package torrentcome.boilerplate.tv.domain;

public class Photo {
    public String title;
    public String description;
    public String url;
    public String thumbnailUrl = null;

    public Photo(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public Photo(String title, String description, String url, String thumbnailUrl) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }
}