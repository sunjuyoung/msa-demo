package sun.board.common.outboxmessagerelay;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssignedShardTest {

    @Test
    void ofTest(){

        Long shardCount = 50L;
        List<String> list = List.of("appId1", "appId2", "appId3", "appId4", "appId5", "appId6", "appId7", "appId8", "appId9", "appId10");

        AssignedShard assignedShard = AssignedShard.of("appId1", list, shardCount);
        AssignedShard assignedShard2 = AssignedShard.of("invalidAppId", list, shardCount);

        assertEquals(assignedShard.getShards().size(), 5);
        assertEquals(assignedShard2.getShards().size(), 0);


    }

}