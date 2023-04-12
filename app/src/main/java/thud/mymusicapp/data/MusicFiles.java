package thud.mymusicapp.data;

public class MusicFiles {
    private int id;
    private String path;
    private String title;
    private String artist;
    private int album;
    private String duration;
    private int albumid;

    public MusicFiles(String path, String title, String artist, int album, String duration, int albumid) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.albumid = albumid;
    }

    public MusicFiles(int id, String path, String title, String artist, int album, String duration, int albumid) {
        this.id = id;
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.albumid = albumid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MusicFiles() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getAlbum() {
        return album;
    }

    public void setAlbum(int album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public int getAlbumid() {
        return albumid;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
