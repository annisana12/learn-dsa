/**
 * Question :
 * <p>
 * Given a set of n integers S={0,1,...,n−1} and a sequence of
 * requests of the following form:
 * <ul>
 *     <li>Remove x from S</li>
 *     <li>Find the successor of x: the smallest y in S such that y≥xy</li>
 * </ul>
 * Design a data type so that all operations (except construction)
 * take logarithmic time or better in the worst case.
 * </p>
 */

public class SuccessorWithDelete {
    int[] parent;
    int[] size;
    int[] successors;

    public SuccessorWithDelete(int n) {
        parent = new int[n + 1];
        size = new int[n + 1];
        successors = new int[n + 1];

        for (int i = 0; i < n + 1; i++) {
            parent[i] = i;
            size[i] = 1;
            successors[i] = i;
        }
    }

    private int root(int p) {
        validate(p, parent.length);

        while (p != parent[p]) {
            int newP = parent[p];

            parent[p] = parent[parent[p]];
            p = newP;
        }

        return p;
    }

    private void validate(int p, int n) {
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    public int find(int p) {
        int n = successors.length - 1;
        validate(p, n);

        int root = root(p);
        int successor = successors[root];

        if (successor == n) return -1;
        else return successor;
    }

    public boolean isDeleted(int p) {
        int successor = find(p);

        return successor == -1 || p != successor;
    }

    public void delete(int p) {
        if (!isDeleted(p)) {
            union(p, p + 1);
        }
    }

    private void union(int p, int q) {
        int rootP = root(p);
        int rootQ = root(q);

        if (rootP == rootQ) return;

        int sizeP = size[rootP];
        int sizeQ = size[rootQ];
        int maxP = successors[rootP];
        int maxQ = successors[rootQ];

        if (sizeP < sizeQ) {
            parent[rootP] = rootQ;
            size[rootQ] += sizeP;
            successors[rootQ] = Math.max(maxP, maxQ);
        } else {
            parent[rootQ] = rootP;
            size[rootP] += sizeQ;
            successors[rootP] = Math.max(maxP, maxQ);
        }
    }

    private static void runTest(int n, int[] deleteArr, int[] expectedArr) {
         SuccessorWithDelete uf = new SuccessorWithDelete(n);

        for (int i = 0; i < deleteArr.length; i++) {
            int p = deleteArr[i];

            uf.delete(p);

            int actual = uf.find(p);
            int expected = expectedArr[i];
            boolean isPassed = actual == expected;

            System.out.println("Test " + (isPassed ? "Passed" : "Failed"));
            System.out.println("Remove      : " + p);
            System.out.println("Expected    : " + expected);
            System.out.println("Actual      : " + actual);
            System.out.println("=========================");
        }
    }

    public static void main(String[] args) {
        int n = 10;
        int[] deleteArr = new int[]{ 0,2,3,4,5,6,7,8,1,9,0,4 };
        int[] expectedArr = new int[]{ 1,3,4,5,6,7,8,9,9,-1,-1,-1 };

        runTest(n, deleteArr, expectedArr);
    }
}
