package org.dataguardians.openai.api.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Inspired by liLittleCat's ChatGPT package.
 *
 * Codifies the Message request
 * </p>
 *
 * original @author <a href="https://github.com/LiLittleCat">LiLittleCat</a>
 *
 * @since 2023/3/2
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @JsonProperty(value = "role")
    public String role;
    @JsonProperty(value = "content")
    public String content;

}
