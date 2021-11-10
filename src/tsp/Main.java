package tsp;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[][] edges = new int[][]
                {{0 , 10 , 15, 20, 1},
                 {10, 0, 35, 25, 0},
                 {15, 35, 0, 30, 3},
                 {20, 25, 30, 0, 0},
                 {1, 0, 3, 0, 0}};
        Data data = new Data(edges,5);
        new GeneticAlg().start(data);
    }
}
