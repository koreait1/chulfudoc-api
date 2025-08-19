package org.backend.chulfudoc.global.email.entities;

/**
 *
 * @param to 수신인
 * @param subject 제목
 * @param content 메시지 내용
 */
public record EmailMessage(String to,
                           String subject,
                           String content) {
}
