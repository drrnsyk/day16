package vttp2022.ssf.day16giphy.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class giphyService {

    // API call from giphy
    public static final String URL = "https://api.giphy.com/v1/gifs/search";

    // inject in the key
    @Value("${API_KEY}")
    private String key;

    public List<
}
