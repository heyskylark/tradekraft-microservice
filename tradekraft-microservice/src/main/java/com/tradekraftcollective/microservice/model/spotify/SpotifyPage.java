package com.tradekraftcollective.microservice.model.spotify;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpotifyPage<T> {
    private String href;
    private List<T> items = new ArrayList<>();
    private int limit;
    private String next;
    private int offset;
    private String previous;
    private int total;
}
