package it.mikeslab.playlist.config;

import it.mikeslab.playlist.pojo.Playlist;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlaylistConfig {

    private final Playlist playlist = new Playlist();

    @Bean
    public Playlist playlist() {
        return playlist;
    }


}
