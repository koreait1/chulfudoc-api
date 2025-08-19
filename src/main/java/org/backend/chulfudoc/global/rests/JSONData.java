package org.backend.chulfudoc.global.rests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JSONData<T> {
    private HttpStatus status;
    private boolean EmailSuccess;
}
