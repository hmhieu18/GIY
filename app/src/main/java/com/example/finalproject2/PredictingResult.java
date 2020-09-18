package com.example.finalproject2;

import java.util.ArrayList;

public class PredictingResult {

    public class StructuredName {
        public String genus;
        public String species;
    }

    public class WikiDescription {
        public String value;
        public String citation;
        public String license_name;
        public String license_url;

    }

    public class Taxonomy {
        public String kingdom;
        public String phylum;
        public String order;
        public String family;
        public String genus;
    }

    public class PlantDetails {
        public String getScientific_name() {
            return scientific_name;
        }

        public void setScientific_name(String scientific_name) {
            this.scientific_name = scientific_name;
        }

        public ArrayList<String> getCommon_names() {
            return common_names;
        }

        public void setCommon_names(ArrayList<String> common_names) {
            this.common_names = common_names;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public WikiDescription getWiki_description() {
            return wiki_description;
        }

        public void setWiki_description(WikiDescription wiki_description) {
            this.wiki_description = wiki_description;
        }

        public String scientific_name;
        public StructuredName structured_name;
        public ArrayList<String> common_names;
        public String url;
        public WikiDescription wiki_description;
        public Taxonomy taxonomy;
    }

    public class SimilarImage {
        public String id;
        public double similarity;
        public String url;
        public String url_small;
    }

    public class Suggestion {
        public int getId() {
            return id;
        }

        public String getPlant_name() {
            return plant_name;
        }

        public PlantDetails getPlant_details() {
            return plant_details;
        }

        public double getProbability() {
            return probability;
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public ArrayList<SimilarImage> getSimilar_images() {
            return similar_images;
        }

        public int id;
        public String plant_name;
        public PlantDetails plant_details;
        public double probability;
        public boolean confirmed;
        public ArrayList<SimilarImage> similar_images;
    }

    public class Root {
        public int id;
        public ArrayList<Suggestion> suggestions;
    }

}
