package it.mikeslab.playlist.pojo;

import it.mikeslab.playlist.service.SongPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistTest {

    @Mock
    private SongPersistenceService persistenceService;

    @InjectMocks
    private Playlist playlist;

    private Song testSong1;
    private Song testSong2;

    @BeforeEach
    void setUp() {
        // Create test songs
        testSong1 = new Song();
        testSong1.setTitle("Test Song 1");
        testSong1.setArtist("Test Artist 1");
        testSong1.setGenre("Test Genre 1");

        testSong2 = new Song();
        testSong2.setTitle("Test Song 2");
        testSong2.setArtist("Test Artist 2");
        testSong2.setGenre("Test Genre 2");
    }

    @Test
    void testInit() {
        // Create a list of songs to be returned by the mock
        List<Song> mockSongs = new ArrayList<>();
        mockSongs.add(testSong1);
        mockSongs.add(testSong2);

        // Configure the mock to return the list
        when(persistenceService.loadSongs()).thenReturn(mockSongs);

        // Call init method
        playlist.init();

        // Verify that the songs list is populated with the mock data
        assertEquals(2, playlist.getSongs().size());
        assertEquals("Test Song 1", playlist.getSongs().get(0).getTitle());
        assertEquals("Test Song 2", playlist.getSongs().get(1).getTitle());

        // Verify that loadSongs was called
        verify(persistenceService, times(1)).loadSongs();
    }

    @Test
    void testAddSong() {
        // Call the method to test
        playlist.addSong(testSong1);

        // Verify that the song was added to the list
        assertEquals(1, playlist.getSongs().size());
        assertEquals("Test Song 1", playlist.getSongs().get(0).getTitle());

        // Verify that a message was added
        assertEquals(1, playlist.getMessages().size());
        assertEquals("Song added: Test Song 1", playlist.getMessages().get(0));

        // Verify that saveSongs was called with the updated list
        verify(persistenceService, times(1)).saveSongs(playlist.getSongs());
    }

    @Test
    void testRemoveSong_Success() {
        // Add songs to the playlist
        playlist.getSongs().add(testSong1);
        playlist.getSongs().add(testSong2);

        // Call the method to test
        boolean result = playlist.removeSong("Test Song 1");

        // Verify the result
        assertTrue(result);
        assertEquals(1, playlist.getSongs().size());
        assertEquals("Test Song 2", playlist.getSongs().get(0).getTitle());

        // Verify that a message was added
        assertEquals(1, playlist.getMessages().size());
        assertEquals("Song removed: Test Song 1", playlist.getMessages().get(0));

        // Verify that saveSongs was called with the updated list
        verify(persistenceService, times(1)).saveSongs(playlist.getSongs());
    }

    @Test
    void testRemoveSong_Failure() {
        // Add songs to the playlist
        playlist.getSongs().add(testSong1);
        playlist.getSongs().add(testSong2);

        // Call the method to test with a non-existent song
        boolean result = playlist.removeSong("Non-existent Song");

        // Verify the result
        assertFalse(result);
        assertEquals(2, playlist.getSongs().size());

        // Verify that no message was added
        assertEquals(0, playlist.getMessages().size());

        // Verify that saveSongs was not called
        verify(persistenceService, never()).saveSongs(any());
    }

    @Test
    void testUpdateSong_Success() {
        // Add songs to the playlist
        playlist.getSongs().add(testSong1);
        playlist.getSongs().add(testSong2);

        // Create a new song for the update
        Song updatedSong = new Song();
        updatedSong.setTitle("Updated Song");
        updatedSong.setArtist("Updated Artist");
        updatedSong.setGenre("Updated Genre");

        // Call the method to test
        boolean result = playlist.updateSong("Test Song 1", updatedSong);

        // Verify the result
        assertTrue(result);
        assertEquals(2, playlist.getSongs().size());
        assertEquals("Updated Song", playlist.getSongs().get(0).getTitle());
        assertEquals("Updated Artist", playlist.getSongs().get(0).getArtist());
        assertEquals("Updated Genre", playlist.getSongs().get(0).getGenre());

        // Verify that a message was added
        assertEquals(1, playlist.getMessages().size());
        assertEquals("Song updated: Updated Song", playlist.getMessages().get(0));

        // Verify that saveSongs was called with the updated list
        verify(persistenceService, times(1)).saveSongs(playlist.getSongs());
    }

    @Test
    void testUpdateSong_Failure() {
        // Add songs to the playlist
        playlist.getSongs().add(testSong1);
        playlist.getSongs().add(testSong2);

        // Create a new song for the update
        Song updatedSong = new Song();
        updatedSong.setTitle("Updated Song");
        updatedSong.setArtist("Updated Artist");
        updatedSong.setGenre("Updated Genre");

        // Call the method to test with a non-existent song
        boolean result = playlist.updateSong("Non-existent Song", updatedSong);

        // Verify the result
        assertFalse(result);
        assertEquals(2, playlist.getSongs().size());
        assertEquals("Test Song 1", playlist.getSongs().get(0).getTitle());
        assertEquals("Test Song 2", playlist.getSongs().get(1).getTitle());

        // Verify that no message was added
        assertEquals(0, playlist.getMessages().size());

        // Verify that saveSongs was not called
        verify(persistenceService, never()).saveSongs(any());
    }
}
