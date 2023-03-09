package org.dataguardians.openai.endpoints;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class ApiEndPointRequest {

    protected  String user;

    protected  String input;

    public abstract String getEndpoint();

    public abstract Object create();

}
