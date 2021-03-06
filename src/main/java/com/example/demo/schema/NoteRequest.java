
package com.example.demo.schema;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "content",
    "isCompleted"
})
public class NoteRequest {

    @NotNull
    @JsonProperty("content")
    private String content;
    @JsonProperty("isCompleted")
    private Boolean isCompleted;

    /**
     * No args constructor for use in serialization
     * 
     */
    public NoteRequest() {
    }

    /**
     * 
     * @param content
     * @param isCompleted
     */
    public NoteRequest(String content, Boolean isCompleted) {
        super();
        this.content = content;
        this.isCompleted = isCompleted;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty("isCompleted")
    public Boolean getIsCompleted() {
        return isCompleted;
    }

    @JsonProperty("isCompleted")
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(NoteRequest.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("content");
        sb.append('=');
        sb.append(((this.content == null)?"<null>":this.content));
        sb.append(',');
        sb.append("isCompleted");
        sb.append('=');
        sb.append(((this.isCompleted == null)?"<null>":this.isCompleted));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
