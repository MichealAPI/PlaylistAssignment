package it.mikeslab.playlist.controller;

import it.mikeslab.playlist.pojo.Playlist;
import it.mikeslab.playlist.pojo.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlaylistController {

    private final Playlist playlist;

    @Autowired
    public PlaylistController(Playlist playlist) {
        this.playlist = playlist;
    }

    @GetMapping("/")
    public String playlist(Model model) {

        model.addAttribute("song", new Song());
        model.addAttribute("playlist", playlist);

        return "index";
    }

}
