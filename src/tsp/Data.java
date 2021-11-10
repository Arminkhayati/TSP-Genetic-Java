package tsp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Data {

    private int[][] edges;
    private int[][] population;
    public int popSize;
    public int cityCount;
    public Data(int[][] edges, int population){
        this.edges = edges;
        this.population = new int[population][edges.length];
        this.popSize = population;
        this.cityCount = edges.length;

        initialPopulation(cityCount, popSize);
        System.out.println("Your Initial Population is : ");

        for (int[] ints : this.population) {
            for (int anInt : ints) {
                System.out.print(" " + anInt);
            }
            System.out.println();
        }
        System.out.println(" \n \n ");



    }

    public int[][] getEdges() {
        return edges;
    }

    public int[][] getPopulation() {
        return population;
    }

    public void setPopulation(int[][] population) {
        this.population = population;
    }

    private void initialPopulation(int numOfCities, int population){
        List<List<Integer>> result = getPermutations(numOfCities, population);
        this.population = result.stream()
                .map(  u  ->  u.stream().mapToInt(i->i).toArray()  )
                .toArray(int[][]::new);
    }


    // getPermutations Function for Generating a permutaion of cities for given population size
    private List<List<Integer>> getPermutations(int numOfCities, int population) {
        List<Integer> array = IntStream.range(0, numOfCities)
                .boxed().collect(Collectors.toList());
        List<List<Integer>> allPermutations = getPermutation(array);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0 ; i < population && population <= allPermutations.size() ; i++){
            Random random = new Random(System.nanoTime());
            int index = Math.abs(random.nextInt()) % allPermutations.size();
            result.add(allPermutations.get(index));
            allPermutations.remove(index);
        }
        return result;
    }
    private List<List<Integer>> getPermutation(List<Integer> ints) {
        if (ints.size() == 1) {
            List<List<Integer>> list = new ArrayList<>();
            list.add(ints);
            return list;
        } else {
            List<List<Integer>> list = new ArrayList<>();
            for (Integer i: ints) {
                ArrayList<Integer> subList = new ArrayList<>(ints);
                subList.remove(i);
                List<List<Integer>> subListNew = getPermutation(subList);
                for (List<Integer> _list: subListNew) {
                    ArrayList<Integer> local = new ArrayList<>();
                    local.add(i);
                    local.addAll(_list);
                    list.add(local);
                }
            }
            return list;
        }
    }

    // If edges[i][j] > 0 then there is an existing edge from city 'i' to city 'j'
    // There is a path from City 'i' to city 'j' if 'i==j'
    public boolean isConnected(int i , int j){
        return (i==j || edges[i][j] > 0) ? true : false;
    }





}
