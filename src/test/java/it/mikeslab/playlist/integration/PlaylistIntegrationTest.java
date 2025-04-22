package it.mikeslab.playlist.integration;

import it.mikeslab.playlist.pojo.Playlist;
import it.mikeslab.playlist.pojo.Song;
import it.mikeslab.playlist.service.SongPersistenceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "song.csv.file.path=test-integration-songs.csv"
})
class PlaylistIntegrationTest {

    @Autowired
    private Playlist playlist;
    
    @Autowired
    private SongPersistenceService persistenceService;
    
    private static final String TEST_CSV_FILE = "test-integration-songs.csv";
    
    @BeforeEach
    void setUp() {
        // Delete test file if it exists
        File testFile = new File(TEST_CSV_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
        
        // Clear the playlist
        playlist.getSongs().clear();
        playlist.getMessages().clear();
    }
    
    @AfterEach
    void tearDown() {
        // Delete test file
        File testFile = new File(TEST_CSV_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    
    @Test
    void testFullCrudOperations() {
        // Verify playlist is empty
        assertTrue(playlist.getSongs().isEmpty());
        
        // Create and add songs
        Song song1 = new Song();
        song1.setTitle("Song 1");
        song1.setArtist("Artist 1");
        song1.setGenre("Genre 1");
        
        Song song2 = new Song();
        song2.setTitle("Song 2");
        song2.setArtist("Artist 2");
        song2.setGenre("Genre 2");
        
        // Add songs to playlist
        playlist.addSong(song1);
        playlist.addSong(song2);
        
        // Verify songs were added
        assertEquals(2, playlist.getSongs().size());
        assertEquals("Song 1", playlist.getSongs().get(0).getTitle());
        assertEquals("Song 2", playlist.getSongs().get(1).getTitle());
        
        // Verify messages were added
        assertEquals(2, playlist.getMessages().size());
        assertEquals("Canzone aggiunta: Song 1", playlist.getMessages().get(0));
        assertEquals("Canzone aggiunta: Song 2", playlist.getMessages().get(1));
        
        // Update a song
        Song updatedSong = new Song();
        updatedSong.setTitle("Updated Song");
        updatedSong.setArtist("Updated Artist");
        updatedSong.setGenre("Updated Genre");
        
        boolean updateResult = playlist.updateSong("Song 1", updatedSong);
        
        // Verify update was successful
        assertTrue(updateResult);
        assertEquals(2, playlist.getSongs().size());
        assertEquals("Updated Song", playlist.getSongs().get(0).getTitle());
        assertEquals("Updated Artist", playlist.getSongs().get(0).getArtist());
        assertEquals("Updated Genre", playlist.getSongs().get(0).getGenre());
        
        // Verify message was added
        assertEquals(3, playlist.getMessages().size());
        assertEquals("Canzone aggiornata: Updated Song", playlist.getMessages().get(2));
        
        // Remove a song
        boolean removeResult = playlist.removeSong("Song 2");
        
        // Verify remove was successful
        assertTrue(removeResult);
        assertEquals(1, playlist.getSongs().size());
        assertEquals("Updated Song", playlist.getSongs().get(0).getTitle());
        
        // Verify message was added
        assertEquals(4, playlist.getMessages().size());
        assertEquals("Canzone rimossa: Song 2", playlist.getMessages().get(3));
        
        // Load songs from file to verify persistence
        List<Song> loadedSongs = persistenceService.loadSongs();
        
        // Verify loaded songs match the current playlist
        assertEquals(1, loadedSongs.size());
        assertEquals("Updated Song", loadedSongs.get(0).getTitle());
        assertEquals("Updated Artist", loadedSongs.get(0).getArtist());
        assertEquals("Updated Genre", loadedSongs.get(0).getGenre());
    }
}