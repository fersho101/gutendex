package com.example.gutendex.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GutendexResponse {
    private Integer count;
    private String next;
    private String previous;
    private List<BookResult> result;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BookResult {
        private Integer id;
        private String title;

        @JsonProperty("download_count")
        private Integer downloadCount;
        @JsonProperty("languages")
        private List<String> languages;
        @JsonProperty("authors")
        private List<AuthorResult> authors;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthorResult {
        private String name;

        @JsonProperty("birth_year")
        private Integer birthYear;
        @JsonProperty("death_year")
        private Integer deathYear;
    }

}
