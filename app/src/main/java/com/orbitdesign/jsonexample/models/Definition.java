package com.orbitdesign.jsonexample.models;

/**
 * Created by sdros_000 on 6/14/2015.
 */

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
        "text",
        "attribution"
})
public class Definition {

    @JsonProperty("text")
    private String text;
    @JsonProperty("attribution")
    private String attribution;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The text
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     * The text
     */
    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     * The attribution
     */
    @JsonProperty("attribution")
    public String getAttribution() {
        return attribution;
    }

    /**
     *
     * @param attribution
     * The attribution
     */
    @JsonProperty("attribution")
    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
