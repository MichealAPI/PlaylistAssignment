package it.mikeslab.playlist.service;

import it.mikeslab.playlist.pojo.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SongPersistenceServiceTest {

    private SongPersistenceService persistenceService;
    private static final String TEST_CSV_FILE = "test-songs.csv";
    private static final String ORIGINAL_CSV_FILE = "songs.csv";

    @BeforeEach
    void setUp() throws IOException {
        // Backup original file if it exists
        File originalFile = new File(ORIGINAL_CSV_FILE);
        if (originalFile.exists()) {
            Files.copy(originalFile.toPath(), Path.of(ORIGINAL_CSV_FILE + ".bak"));
        }

        // Create persistence service and set test file path
        persistenceService = new SongPersistenceService();
        try {
            java.lang.reflect.Field field = SongPersistenceService.class.getDeclaredField("csvFilePath");
            field.setAccessible(true);
            field.set(persistenceService, TEST_CSV_FILE);
        } catch (Exception e) {
            System.err.println("Error setting test file path: " + e.getMessage());
        }

        // Delete test file if it exists
        File testFile = new File(TEST_CSV_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete test file
        File testFile = new File(TEST_CSV_FILE);
        if (testFile.exists()) {
            testFile.delete();
        }

        // Restore original file if backup exists
        File backupFile = new File(ORIGINAL_CSV_FILE + ".bak");
        if (backupFile.exists()) {
            Files.copy(backupFile.toPath(), Path.of(ORIGINAL_CSV_FILE));
            backupFile.delete();
        }
    }

    @Test
    void testLoadSongsFromEmptyFile() {
        List<Song> songs = persistenceService.loadSongs();
        assertNotNull(songs);
        assertTrue(songs.isEmpty());
    }

    @Test
    void testSaveAndLoadSongs() {
        // Create test songs
        List<Song> testSongs = new ArrayList<>();
        Song song1 = new Song();
        song1.setTitle("Test Song 1");
        song1.setArtist("Test Artist 1");
        song1.setGenre("Test Genre 1");
        testSongs.add(song1);

        Song song2 = new Song();
        song2.setTitle("Test Song 2");
        song2.setArtist("Test Artist 2");
        song2.setGenre("Test Genre 2");
        testSongs.add(song2);

        // Save songs
        persistenceService.saveSongs(testSongs);

        // Load songs
        List<Song> loadedSongs = persistenceService.loadSongs();

        // Verify
        assertNotNull(loadedSongs);
        assertEquals(2, loadedSongs.size());

        // Verify first song
        Song loadedSong1 = loadedSongs.get(0);
        assertEquals("Test Song 1", loadedSong1.getTitle());
        assertEquals("Test Artist 1", loadedSong1.getArtist());
        assertEquals("Test Genre 1", loadedSong1.getGenre());

        // Verify second song
        Song loadedSong2 = loadedSongs.get(1);
        assertEquals("Test Song 2", loadedSong2.getTitle());
        assertEquals("Test Artist 2", loadedSong2.getArtist());
        assertEquals("Test Genre 2", loadedSong2.getGenre());
    }

    @Test
    void testSaveEmptyList() {
        List<Song> emptySongs = new ArrayList<>();
        persistenceService.saveSongs(emptySongs);

        List<Song> loadedSongs = persistenceService.loadSongs();
        assertNotNull(loadedSongs);
        assertTrue(loadedSongs.isEmpty());
    }
}
