package com.orbitdesign.jsonexample.models;

/**
 * Created by shmuel on 6/21/15.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "definitions"
})
public class ServerResponse {

    @JsonProperty("definitions")
    private List<Definition> definitions = new ArrayList<Definition>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The definitions
     */
    @JsonProperty("definitions")
    public List<Definition> getDefinitions() {
        return definitions;
    }

    /**
     *
     * @param definitions
     * The definitions
     */
    @JsonProperty("definitions")
    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
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
