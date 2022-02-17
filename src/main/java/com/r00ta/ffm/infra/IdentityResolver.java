package com.r00ta.ffm.infra;

import org.eclipse.microprofile.jwt.JsonWebToken;

public interface IdentityResolver {
    String resolve(JsonWebToken jwt);
}
