package com.r00ta.ffm.api.internal;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.r00ta.ffm.DinosaurService;
import com.r00ta.ffm.ShardService;
import com.r00ta.ffm.infra.APIConstants;
import com.r00ta.ffm.infra.IdentityResolver;
import com.r00ta.ffm.infra.dto.DinosaurDTO;
import com.r00ta.ffm.infra.dto.DinosaurStatus;
import com.r00ta.ffm.infra.exceptions.definitions.user.ForbiddenRequestException;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.toList;

@SecuritySchemes(value = {
        @SecurityScheme(securitySchemeName = "bearer",
                type = SecuritySchemeType.HTTP,
                scheme = "Bearer")
})
@SecurityRequirement(name = "bearer")
@Path(APIConstants.SHARD_API_BASE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class ShardBridgesSyncAPI {
    private static final List<DinosaurStatus> statuses = Arrays.asList(DinosaurStatus.REQUESTED, DinosaurStatus.DELETION_REQUESTED);
    private static final Logger LOGGER = LoggerFactory.getLogger(ShardBridgesSyncAPI.class);

    @Inject
    DinosaurService dinosaurService;

    @Inject
    ShardService shardService;

    @Inject
    IdentityResolver identityResolver;

    @Inject
    JsonWebToken jwt;

    @GET
    public Response getDinosaurs() {
        String shardId = identityResolver.resolve(jwt);
        failIfNotAuthorized(shardId);
        LOGGER.info("Shard asks for Bridges to deploy or delete");
        return Response.ok(dinosaurService.getDinosaursByStatusesAndShardId(statuses, shardId)
                .stream()
                .map(dinosaurService::toDTO)
                .collect(toList()))
                .build();
    }

    @PUT
    public Response updateBridge(DinosaurDTO dto) {
        String subject = identityResolver.resolve(jwt);
        failIfNotAuthorized(subject);
        LOGGER.info("Shard wants to update the Bridge with id '{}' with the status '{}'", dto.getId(), dto.getStatus());
        dinosaurService.updateDinosaur(dto);
        return Response.ok().build();
    }

    private void failIfNotAuthorized(String shardId) {
        if (!shardService.isAuthorizedShard(shardId)) {
            throw new ForbiddenRequestException(String.format("User '%s' is not authorized to access this api.", shardId));
        }
    }
}
