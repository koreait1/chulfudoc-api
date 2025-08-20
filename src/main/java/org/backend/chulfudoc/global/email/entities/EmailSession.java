package org.backend.chulfudoc.global.email.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash(timeToLive = 3*60)
public class EmailSession implements Serializable {
    // redis에 저장 및 불러오기를 위해 Serializable로 직렬화 redis 저장 형태(byte) 사람이 읽으려면 String

    @Id
    private String hash;

    private int value;
}
