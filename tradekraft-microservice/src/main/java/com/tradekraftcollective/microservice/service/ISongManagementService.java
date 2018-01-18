package com.tradekraftcollective.microservice.service;

import com.tradekraftcollective.microservice.persistence.entity.Release;
import com.tradekraftcollective.microservice.persistence.entity.Song;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * Created by brandonfeist on 10/22/17.
 */
public interface ISongManagementService {
    Song createSong(String releaseSlug, Song song);

    Song uploadSongFile(String songSlug, MultipartFile songFile);

    HashMap<String, MultipartFile> createSongFileHashMap(MultipartFile[] songFiles);

    void deleteSong(Song song);

    void deleteSong(String songSlug);

    List<Song> songLinkAuthorization(Release release);
}
