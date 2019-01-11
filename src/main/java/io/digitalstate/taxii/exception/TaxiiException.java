package io.digitalstate.taxii.exception;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.digitalstate.taxii.common.TaxiiParsers;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.*;

@JsonAutoDetect(
        fieldVisibility = Visibility.NONE,
        setterVisibility = Visibility.NONE,
        getterVisibility = Visibility.NONE,
        isGetterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE
)
@JsonPropertyOrder({"title", "description", "error_id", "error_code", "http_status", "external_details", "details"})
public class TaxiiException extends RuntimeException {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("error_id")
    private String errorId;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("http_status")
    private String httpStatus;

    @JsonProperty("external_Details")
    private String externalDetails;

    @JsonProperty("details")
    private String details;

    public TaxiiException(@Nullable Throwable cause, @NotNull String title, String description, String errorId, String errorCode, String httpStatus, String externalDetails, String details) {
        super(title, cause);
        if (cause != null){
            cause.printStackTrace();
        }
        //@TODO update the logging to be proper!!!
        System.out.println("TAXII EXCEPTION: " + title);
        this.title = title;
        this.description = description;
        this.errorId = errorId;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.externalDetails = externalDetails;
        this.details = details;
    }


    @Override
    public String toString() {
        return "TaxiiException{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", errorId='" + errorId + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", httpStatus='" + httpStatus + '\'' +
                ", externalDetails='" + externalDetails + '\'' +
                ", details='" + details + '\'' +
                '}';
    }

    public String toJson(){
        try {
            return TaxiiParsers.getJsonMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to generate json body", e);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getErrorId() {
        return errorId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public String getExternalDetails() {
        return externalDetails;
    }

    public String getDetails() {
        return details;
    }
}
