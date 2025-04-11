/**
 * Question :
 * <p>
 * Add a method find() to the union-find data type so that
 * find(i) returns the largest element in the connected
 * component containing i. The operations, union(), connected(),
 * find() should all take logarithmic time or better.
 * <p>
 * For example, if one of the connected components is {1,2,6,9}
 * then the find() method should return 9 for each of the
 * four elements in the connected components.
 */

public class UnionFindCanonicalElement {
    private int[] parent;
    private int[] size; // size[i] = number of nodes rooted at i
    private int[] max; // max[i] = largest node rooted at i
    private int count; // number of connected components

    public UnionFindCanonicalElement(int n) {
        parent = new int[n];
        size = new int[n];
        max = new int[n];
        count = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
            max[i] = i;
        }
    }

    public int count() {
        return count;
    }

    private int root(int p) {
        validate(p);

        while (p != parent[p]) {
            p = parent[p];
        }

        return p;
    }

    private void validate(int p) {
        int n = parent.length;

        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    public boolean connected(int p, int q) {
        int rootP = root(p);
        int rootQ = root(q);

        return rootP == rootQ;
    }

    public int find(int p) {
        int root = root(p);
        return max[root];
    }

    public void union(int p , int q) {
        int rootP = root(p);
        int rootQ = root(q);

        if (rootP == rootQ) return;

        int sizeP = size[rootP];
        int sizeQ = size[rootQ];
        int maxP = max[rootP];
        int maxQ = max[rootQ];

        if (sizeP < sizeQ) {
            parent[rootP] = rootQ;
            size[rootQ] += sizeP;
            max[rootQ] = Math.max(maxP, maxQ);
        } else {
            parent[rootQ] = rootP;
            size[rootP] += sizeQ;
            max[rootP] = Math.max(maxP, maxQ);
        }

        count--;
    }

    private static void runTest(UnionFindCanonicalElement uf, int p, int expected) {
        int actual = uf.find(p);

        if (actual == expected) {
            System.out.println("Test Passed");
        } else {
            System.out.println("Test Failed (Expected: " + expected + ", Got: " + actual + ")");
        }
    }

    public static void main(String[] args) {
        UnionFindCanonicalElement uf = new UnionFindCanonicalElement(10);

        int[][] pairs = new int[][]{
                { 0, 2 },
                { 3, 7 },
                { 1, 6 },
                { 1, 8 },
                { 6, 3 },
                { 4, 0 }
        };

        for (int[] pair : pairs) {
            int p = pair[0];
            int q = pair[1];

            if (!uf.connected(p, q)) {
                uf.union(p, q);
            }
        }

        runTest(uf, 0, 4);
        runTest(uf, 1, 8);
        runTest(uf, 2, 4);
        runTest(uf, 3, 8);
        runTest(uf, 4, 4);
        runTest(uf, 5, 5);
        runTest(uf, 6, 8);
        runTest(uf, 7, 8);
        runTest(uf, 8, 8);
        runTest(uf, 9, 9);
    }
}