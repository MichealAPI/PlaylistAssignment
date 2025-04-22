package it.mikeslab.playlist.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Song {

    private String title;
    private String artist;
    private String genre;

}
