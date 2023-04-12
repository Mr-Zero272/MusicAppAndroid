package thud.mymusicapp.data;

public class Album {
    private int id;
    private String albumName;
    private int userid;

    public Album(){}

    public Album(int id, String albumName, int userid) {
        this.id = id;
        this.albumName = albumName;
        this.userid = userid;
    }

    public Album(String albumName, int userid) {
        this.albumName = albumName;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
