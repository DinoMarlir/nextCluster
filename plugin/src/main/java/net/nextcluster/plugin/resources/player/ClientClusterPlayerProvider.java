package net.nextcluster.plugin.resources.player;

import dev.httpmarco.osgan.utils.executers.ThreadAsyncExecutor;
import dev.httpmarco.osgon.files.configuration.gson.JsonDocument;
import net.nextcluster.driver.NextCluster;
import net.nextcluster.driver.resource.player.ClusterPlayer;
import net.nextcluster.driver.resource.player.PlayerProvider;
import net.nextcluster.driver.resource.player.packets.AbstractClusterPlayerPacket;
import net.nextcluster.driver.resource.player.packets.ClusterPlayerResponsePacket;

import java.util.Optional;
import java.util.UUID;

public final class ClientClusterPlayerProvider implements PlayerProvider {

    @Override
    public ThreadAsyncExecutor<Optional<ClusterPlayer>> getPlayerAsync(UUID uniqueId) {
        var executor = new ThreadAsyncExecutor<Optional<ClusterPlayer>>();
        NextCluster.instance().transmitter().request("nextcluster-users-online", new JsonDocument().append("uuid", uniqueId),
                AbstractClusterPlayerPacket.class, it -> executor.complete(Optional.of(it.clusterPlayer())));
        return executor;
    }

    @Override
    public ThreadAsyncExecutor<Optional<ClusterPlayer>> getPlayerAsync(String username) {
        var executor = new ThreadAsyncExecutor<Optional<ClusterPlayer>>();
        NextCluster.instance().transmitter().request("nextcluster-users-online", new JsonDocument().append("username", username),
                AbstractClusterPlayerPacket.class, it -> executor.complete(Optional.of(it.clusterPlayer())));
        return executor;
    }

    @Override
    public ThreadAsyncExecutor<Boolean> isOnlineAsync(String username) {
        var executor = new ThreadAsyncExecutor<Boolean>();
        NextCluster.instance().transmitter().request("nextcluster-users-online", new JsonDocument().append("username", username),
                ClusterPlayerResponsePacket.class, it -> executor.complete(it.online()));
        return executor;
    }

    @Override
    public ThreadAsyncExecutor<Boolean> isOnlineAsync(UUID uniqueId) {
        var executor = new ThreadAsyncExecutor<Boolean>();
        NextCluster.instance().transmitter()
                .request("nextcluster-users-online", new JsonDocument().append("uuid", uniqueId.toString()),
                        ClusterPlayerResponsePacket.class, it -> executor.complete(it.online()));
        return executor;
    }

    @Override
    public ClusterPlayer createPlayer(String name, UUID uniqueId, String currentProxyName, String currentServerName) {
        // TODO discussion open: API cant create cluster player
        return null;
    }
}
