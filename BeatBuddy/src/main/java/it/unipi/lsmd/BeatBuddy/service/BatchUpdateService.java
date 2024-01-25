package it.unipi.lsmd.BeatBuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BatchUpdateService {

    @Autowired
    private Neo4jClient neo4jClient;

    public void executeBatchUpdate(int batchSize) {
        int startId = 0;
        int maxId = getMaxNodeId();

        for (int i = startId; i <= maxId; i += batchSize) {
            int endId = i + batchSize;
            updateNodesInBatch(i, endId);
        }
    }

    private void updateNodesInBatch(int startId, int endId) {
        String cypherQuery = "MATCH (n:Song) " +
                "WHERE n.id >= $startId AND n.id < $endId " +
                "AND n.artistName IS NOT NULL " +
                "SET n.artists = n.artistName " +
                "REMOVE n.artistName";

        neo4jClient.query(cypherQuery)
                .bindAll(Map.of("startId", startId, "endId", endId))
                .run();
    }

    private int getMaxNodeId() {
        return neo4jClient.query("MATCH (n:Song) RETURN max(n.id) as maxId")
                .fetchAs(Integer.class)
                .one()
                .orElse(0);
    }
}
