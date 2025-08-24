package org.backend.chulfudoc.global.rests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JSONData<T> {
    // status 값을 가져오기 어려운 환경에서 개발할 경우 해당 객체를 활용해서 body값에 담아 value 전달할 것
    private HttpStatus status;
    private boolean EmailSuccess;
}
