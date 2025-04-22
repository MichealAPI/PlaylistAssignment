package it.mikeslab.playlist.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Playlist {
    private List<Song> songs = new ArrayList<>();
    private String playlistName;

    // Add a messages list
    private List<String> messages = new ArrayList<>();

    public void addSong(Song song) {
        songs.add(song);
        messages.add("Song added: " + song.getTitle());
    }

    public boolean removeSong(String songName) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getTitle().equals(songName)) {
                songs.remove(i);
                messages.add("Song removed: " + songName);
                return true;
            }
        }
        return false;
    }

    public boolean updateSong(String songName, Song newSong) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getTitle().equals(songName)) {
                songs.set(i, newSong);
                messages.add("Song updated: " + newSong.getTitle());
                return true;
            }
        }
        return false;
    }
}