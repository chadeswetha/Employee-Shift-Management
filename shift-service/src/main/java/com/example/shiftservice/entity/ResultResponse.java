package com.example.shiftservice.entity;



import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * ResultResponse: Generic wrapper class for API responses.
 * <p>
 * This class is used to encapsulate the response structure for various API endpoints.
 * It includes details about the success of the operation, a message providing context,
 * the data returned by the API, and the timestamp of the response.
 * </p>
 * 
 * <p>
 * **Annotations**:
 * - **@Builder**: Enables the builder pattern for constructing instances of this class.
 * - **@Data**: Generates boilerplate methods such as getters, setters, `equals()`, `hashCode()`, and `toString()`.
 * </p>
 *
 * <p>
 * **Type Parameter**:
 * - `T`: The type of data returned by the response. This allows flexibility to use the class
 *   for any type of data, making it reusable across different API endpoints.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse<T> {

    /**
     * Indicates whether the operation was successful.
     */
    private boolean success;

    /**
     * Message providing details about the response or operation.
     */
    private String message;

    /**
     * The actual data returned by the API. Can be any type (e.g., DTO, entity, or list).
     */
    private T data;

    /**
     * The timestamp at which the response was generated.
     */
    private LocalDateTime timestamp;


}
 
 

