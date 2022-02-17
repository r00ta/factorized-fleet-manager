package com.r00ta.ffm;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.r00ta.ffm.dao.ShardDAO;
import com.r00ta.ffm.models.Shard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ShardServiceImpl implements ShardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardServiceImpl.class);

    private Set<String> shards;

    @Inject
    ShardDAO shardDAO;

    @PostConstruct
    public void init() {
        /*
         * Fetch the list of authorized shards at startup.
         * This needs to be changed when admin api will include the CRUD of a shard at runtime.
         */
        shards = shardDAO.listAll().stream().map(Shard::getId).collect(Collectors.toSet());
    }

    @Override
    public String getAssignedShardId(String id) {
        List<Shard> shards = shardDAO.listAll(); // TODO: Strategy to be implemented

        return shards.get(0).getId();
    }

    @Override
    public boolean isAuthorizedShard(String shardId) {
        return shards.contains(shardId);
    }
}
