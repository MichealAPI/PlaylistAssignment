package it.mikeslab.playlist.service;

import it.mikeslab.playlist.pojo.Song;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongPersistenceService {

    @Value("${song.csv.file.path:songs.csv}")
    private String csvFilePath;

    private static final String CSV_HEADER = "title,artist,genre";
    private static final String CSV_DELIMITER = ",";

    /**
     * Loads all songs from the CSV file.
     * 
     * @return List of songs
     */
    public List<Song> loadSongs() {
        List<Song> songs = new ArrayList<>();
        File file = new File(csvFilePath);

        // Create file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
                // Write header
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(CSV_HEADER);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error creating CSV file: " + e.getMessage());
                return songs;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                // Skip header
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Parse CSV line
                String[] values = line.split(CSV_DELIMITER);
                if (values.length >= 3) {
                    Song song = new Song();
                    song.setTitle(values[0]);
                    song.setArtist(values[1]);
                    song.setGenre(values[2]);
                    songs.add(song);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return songs;
    }

    /**
     * Saves all songs to the CSV file.
     * 
     * @param songs List of songs to save
     */
    public void saveSongs(List<Song> songs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            // Write header
            writer.write(CSV_HEADER);
            writer.newLine();

            // Write songs
            for (Song song : songs) {
                String line = song.getTitle() + CSV_DELIMITER + 
                              song.getArtist() + CSV_DELIMITER + 
                              song.getGenre();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
