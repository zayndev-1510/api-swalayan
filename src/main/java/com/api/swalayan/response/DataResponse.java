package com.api.swalayan.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse {
    private String message;
    private Boolean success;
    private List<?> data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long total;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer totalPages;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer start;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer end;

    // Constructor without pagination info
    public DataResponse(String message, boolean success,List<?> data) {
        this.data = data;
        this.message = message;
        this.success = success;
    }
}
