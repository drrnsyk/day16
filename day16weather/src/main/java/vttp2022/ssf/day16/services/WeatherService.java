package vttp2022.ssf.day16.services;

import java.io.Reader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.day16.models.Weather;
import vttp2022.ssf.day16.repositories.WeatherRepository;

@Service
public class WeatherService {


    // API call from OpenWeatherMap
    // the URL excludes the query string
    public static final String URL = "https://api.openweathermap.org/data/2.5/weather";

    // inject in the key
    @Value("${API_KEY}")
    private String key;

    @Autowired
    private WeatherRepository weatherRepo;

    public List<Weather> getWeather(String city) {

        // Check if we have the weather cached
        Optional<String> opt = weatherRepo.get(city);
        String payload;

        System.out.printf(">>> city: %s\n", city);

        // Check if the box is empty
        if (opt.isEmpty()) {

            System.out.println("Getting weather from OpenWeatherMap");

            try {
                // construct the query string
                String url = UriComponentsBuilder.fromUriString(URL)
                    .queryParam("q", URLEncoder.encode(city, "UTF-8"))
                    .queryParam("appid", key)
                    .toUriString();

                // create a request entity
                // create the GET request, GET url
                RequestEntity<Void> req = RequestEntity.get(url).build();

                // to make the call to OpenWeatherMap
                // need to create restTemplate
                RestTemplate template = new RestTemplate();
                // make the call
                ResponseEntity<String> resp;

                
                // Throws an exception if status code not in between 200 - 399
                resp = template.exchange(req, String.class);

                // check status code
                // if (resp.getStatusCodeValue() != 200) {
                //     System.err.println("Error: Status code is not 200");
                //     return Collections.emptyList();
                // }

                // get the payload and do something with it
                payload = resp.getBody();
                System.out.println("payload: " + payload);

                weatherRepo.save(city,payload);
            } catch (Exception ex) {
                System.err.printf("Error: %s\n", ex.getMessage());
                return Collections.emptyList();
            }

        } else {
            // Retrieve the value for the box
            payload = opt.get();
            System.out.printf(">>>> cache: %s\n", payload);
        }


        // parsing from String to Json object?
        // convert payload from string to json object
        // create a StringReader to read the payload string
        Reader strReader = new StringReader(payload);
        // create a JsonReader to read the StringReader
        JsonReader jsonReader = Json.createReader(strReader);
        // read the payload as a json object (entire API result)
        JsonObject weatherResult = jsonReader.readObject();

        // get array (weather array portion) from the json object (entire API result)
        JsonArray cities = weatherResult.getJsonArray("weather");

        // declare a new list
        List<Weather> list = new LinkedList<>();
        
        System.out.println(cities.size());
        // cities is a json array with size = 1
        System.out.println(cities); // prints entire array
        System.out.println(cities.get(0)); // prints first item (jsonobject) in array
        System.out.println(cities.getJsonObject(0).getString("main"));

        for (int i = 0; i < cities.size(); i++) {
            JsonObject jo = cities.getJsonObject(i);
            list.add(Weather.create(jo));
            // Weather w = new Weather();
            // w.setMain(jo.getString("main"));
            // w.setDescription(jo.getString("description"));
            // w.setIcon(jo.getString("icon"));
        }

        return list;

    }
}