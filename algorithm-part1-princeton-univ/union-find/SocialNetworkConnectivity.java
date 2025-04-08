/**
 * Question :
 * <p>
 * Given a social network containing n members and a log file
 * containing m timestamps at which times pairs of members
 * formed friendships, design an algorithm to determine
 * the earliest time at which all members are connected
 * (i.e., every member is a friend of a friend of a friend ... of a friend).
 * <p>
 * Assume that the log file is sorted by timestamp and that
 * friendship is an equivalence relation.
 * <p>
 * The running time of your algorithm should be m log n
 * or better and use extra space proportional to n.
 */

import edu.princeton.cs.algs4.UF;
import java.util.List;

public class SocialNetworkConnectivity {
    public static class FriendshipLog {
        final long timestamp;
        final int userA;
        final int userB;

        public FriendshipLog(long timestamp, int userA, int userB) {
            this.timestamp = timestamp;
            this.userA = userA;
            this.userB = userB;
        }
    }

    public long findEarliestFullConnection(int n, List<FriendshipLog> logs) {
        UF uf = new UF(n);

        for (FriendshipLog log : logs) {
            if (uf.find(log.userA) != uf.find(log.userB)) {
                uf.union(log.userA, log.userB);
            }

            if (uf.count() == 1) return log.timestamp;
        }

        return -1;
    }

    static void runTest(int n, List<FriendshipLog> logs, long expected) {
        SocialNetworkConnectivity snc = new SocialNetworkConnectivity();

        long actual = snc.findEarliestFullConnection(n, logs);

        if (actual == expected) {
            System.out.println("Test Passed");
        } else {
            System.out.println("Test Failed (Expected: " + expected + ", Got: " + actual + ")");
        }
    }

    public static void main(String[] args) {
        runTest(
                4,
                List.of(
                        new FriendshipLog(1744121662000L, 0, 1),
                        new FriendshipLog(1744125010000L, 0, 3),
                        new FriendshipLog(1744211410000L, 1, 2),
                        new FriendshipLog(1744384210000L, 1, 3)
                ),
                1744211410000L
        );

        runTest(
                10,
                List.of(
                        new FriendshipLog(1744121662000L, 0, 1),
                        new FriendshipLog(1744125010000L, 5, 3),
                        new FriendshipLog(1744211410000L, 6, 2),
                        new FriendshipLog(1744384210000L, 1, 3),
                        new FriendshipLog(1744384210000L, 2, 4),
                        new FriendshipLog(1744384210000L, 5, 9),
                        new FriendshipLog(1744384210000L, 7, 8)
                ),
                -1
        );

        runTest(
                2,
                List.of(
                        new FriendshipLog(1744121662000L, 0, 1)
                ),
                1744121662000L
        );
    }
}
