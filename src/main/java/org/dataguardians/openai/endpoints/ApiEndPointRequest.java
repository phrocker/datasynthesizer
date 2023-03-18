package org.dataguardians.openai.endpoints;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class ApiEndPointRequest {

    protected String user;

    protected String input;

    @Builder.Default
    protected int maxTokens = 4096;

    public abstract String getEndpoint();

    public abstract Object create();

}
