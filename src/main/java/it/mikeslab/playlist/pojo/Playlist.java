package it.mikeslab.playlist.pojo;

import it.mikeslab.playlist.service.SongPersistenceService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Playlist {
    private List<Song> songs = new ArrayList<>();
    private String playlistName;

    // Add a messages list
    private List<String> messages = new ArrayList<>();

    @Autowired
    @Lazy
    private SongPersistenceService persistenceService;

    @PostConstruct
    public void init() {
        // Load songs from CSV file on initialization
        songs = persistenceService.loadSongs();
    }

    public void addSong(Song song) {
        songs.add(song);
        messages.add("Canzone aggiunta: " + song.getTitle());
        // Save to CSV file
        persistenceService.saveSongs(songs);
    }

    public boolean removeSong(String songName) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getTitle().equals(songName)) {
                songs.remove(i);
                messages.add("Canzone rimossa: " + songName);
                // Save to CSV file
                persistenceService.saveSongs(songs);
                return true;
            }
        }
        return false;
    }

    public boolean updateSong(String songName, Song newSong) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getTitle().equals(songName)) {
                songs.set(i, newSong);
                messages.add("Canzone aggiornata: " + newSong.getTitle());
                // Save to CSV file
                persistenceService.saveSongs(songs);
                return true;
            }
        }
        return false;
    }
}
