import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final boolean[] isSitesOpen;
    private final WeightedQuickUnionUF topBottomUF; // Help simulate connections in percolation
    private final WeightedQuickUnionUF topUF; // Help simulate connections in full sites
    private int count; // Number of open sites
    private final int SIZE, TOP_VIRTUAL_INDEX, BOTTOM_VIRTUAL_INDEX;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        int numberOfSites = n * n;

        SIZE = n;
        TOP_VIRTUAL_INDEX = numberOfSites;
        BOTTOM_VIRTUAL_INDEX = numberOfSites + 1;
        count = 0;
        isSitesOpen = new boolean[numberOfSites];
        topBottomUF = new WeightedQuickUnionUF(numberOfSites + 2);
        topUF = new WeightedQuickUnionUF(numberOfSites + 1);
    }

    public void open(int row, int col) {
        int index = getIndex(row, col);

        if (isSitesOpen[index]) return;

        // Open this site
        isSitesOpen[index] = true;
        count++;

        // Determine neighbour index
        int topIndex = row == 1 ? -1 : getIndex(row - 1, col);
        int bottomIndex = row == SIZE ? -1 : getIndex(row + 1, col);
        int leftIndex = col == 1 ? -1 : getIndex(row, col - 1);
        int rightIndex = col == SIZE ? -1 : getIndex(row, col + 1);

        // Connect this site with its neighbour
        connectNeighbour(index, topIndex);
        connectNeighbour(index, bottomIndex);
        connectNeighbour(index, leftIndex);
        connectNeighbour(index, rightIndex);

        // Connect to virtual top site
        if (row == 1) {
            topBottomUF.union(index, TOP_VIRTUAL_INDEX);
            topUF.union(index, TOP_VIRTUAL_INDEX);
        }

        // Connect to virtual bottom site
        if (row == SIZE) {
            topBottomUF.union(index, BOTTOM_VIRTUAL_INDEX);
        }
    }

    public boolean isOpen(int row, int col) {
        int index = getIndex(row, col);
        return isSitesOpen[index];
    }

    public boolean isFull(int row, int col) {
        int index = getIndex(row, col);

        return topUF.find(index) == topUF.find(TOP_VIRTUAL_INDEX);
    }

    public int numberOfOpenSites() {
        return count;
    }

    public boolean percolates() {
        return topBottomUF.find(TOP_VIRTUAL_INDEX) == topBottomUF.find(BOTTOM_VIRTUAL_INDEX);
    }

    private int getIndex(int row, int col) {
        if (row <= 0 || row > SIZE || col <= 0 || col > SIZE) {
            throw new IllegalArgumentException("row and col must be between 1 and " + SIZE);
        }

        return (row - 1) * SIZE + (col - 1);
    }

    private void connectNeighbour(int site, int neighbour) {
        if (neighbour != -1 && isSitesOpen[neighbour]) {
            topBottomUF.union(site, neighbour);
            topUF.union(site, neighbour);
        }
    }

    public static void main(String[] args) {
        int n = 5;
        Percolation percolation = new Percolation(n);

        int[][] sitesToOpen = new int[][]{
                { 2, 2 },
                { 2, 3 },
                { 3, 4 },
                { 2, 3 },
                { 3, 3 },
                { 1, 2 },
                { 4, 4 },
                { 5, 4 },
                { 4, 1 }
        };

        // { isFull, percolates }
        boolean[][] expectedResults = new boolean[][]{
                { false, false },
                { false, false },
                { false, false },
                { false, false },
                { false, false },
                { true, false },
                { true, false },
                { true, true },
                { false, true }
        };

        for (int i = 0; i < sitesToOpen.length; i++) {
            int row = sitesToOpen[i][0];
            int col = sitesToOpen[i][1];

            System.out.println("row " + row + " and col " + col);

            if (!percolation.isOpen(row, col)) {
                percolation.open(row, col);
            }

            boolean actualIsFull = percolation.isFull(row, col);
            boolean expectedIsFull = expectedResults[i][0];
            boolean actualPercolates = percolation.percolates();
            boolean expectedPercolates = expectedResults[i][1];

            System.out.println("Full ?                  : " + (actualIsFull == expectedIsFull ? "✅ PASSED" : "❌ FAILED") + " (actual: " + actualIsFull + ", expected: " + expectedIsFull + ")");
            System.out.println("Percolates ?            : " + (actualPercolates == expectedPercolates ? "✅ PASSED" : "❌ FAILED") + " (actual: " + actualPercolates + ", expected: " + expectedPercolates + ")");
            System.out.println("Number of open sites    : " + percolation.numberOfOpenSites());
            System.out.println("=====================================================================");
        }
    }
}
