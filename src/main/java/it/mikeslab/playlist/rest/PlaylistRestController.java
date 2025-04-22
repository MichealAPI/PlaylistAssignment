package it.mikeslab.playlist.rest;

import it.mikeslab.playlist.pojo.Playlist;
import it.mikeslab.playlist.pojo.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PlaylistRestController {

    private final Playlist playlist;

    @Autowired
    public PlaylistRestController(Playlist playlist) {
        this.playlist = playlist;
    }

    @PostMapping("/playlist")
    public String addSong(@ModelAttribute Song song) {
        playlist.addSong(song);
        return "redirect:/";
    }

    @PostMapping("/playlist/update/{songName}")
    public String updateSong(@PathVariable String songName, @ModelAttribute Song song) {
        playlist.updateSong(songName, song);
        return "redirect:/";
    }

    @PostMapping("/playlist/delete/{songName}")
    public String deleteSong(@PathVariable String songName) {
        playlist.removeSong(songName);

        // redirect to the playlist page
        return "redirect:/";
    }

    @GetMapping("/playlist")
    public Playlist getPlaylist() {
        return playlist;
    }
}