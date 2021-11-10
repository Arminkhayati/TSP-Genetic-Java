package tsp;

import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Util {

    // Check number of edges and sum of weights of those edges for every indivual in population
    // then it calculates the probability of each individuals
    public double[] fitness(Data data){
        int[][] population = data.getPopulation();
        int[][] edges = data.getEdges();
        double[] reslut = new double[data.popSize];
        for(int i = 0 ; i< data.popSize ; i++) {
            double sumOfWeights = 0;
            double countOfEdges = 0;
            for (int j = 0; j < data.cityCount; j++) {
                int from = population[i][j];
                int to = ((j + 1) % data.cityCount == 0)? population[i][0] : population[i][j+1];
                if (data.isConnected(from, to)) {
                    countOfEdges++;
                    sumOfWeights += edges[from][to];
                }
            }
//            reslut[i] = 100 - (sumOfWeights / countOfEdges);
            reslut[i] = sumOfWeights / countOfEdges;
        }
//        double sumOfFitnesses = Arrays.stream(reslut).sum();
//        System.out.println("Sum of fitness = " + sumOfFitnesses);
//        Arrays.stream(reslut).forEach(x -> System.out.print(" " + (100 - x)));
//        System.out.println();
//        Arrays.stream(reslut).forEach(x -> System.out.print(" " + x));
//        System.out.println();
//        reslut = Arrays.stream(reslut).
//                map(x -> x / sumOfFitnesses).toArray();
        return reslut;
    }

    // Select the best from Selected Population
    public int[] selectParent(Data data, double[] fitness, int selectionRate){
        Random random = new Random();
        int[][] selectedPop = new int[selectionRate][data.getPopulation()[0].length];
        double[] selectedPopFitness = new double[selectionRate];
        //Selecting 'n = selectionRate' individual from Population
        for(int i = 0 ; i < selectionRate ; i++){
            int index = Math.abs(random.nextInt()) % data.popSize;
            selectedPop[i] = data.getPopulation()[index];
            selectedPopFitness[i] = fitness[index];
        }
        //Selecting the best individual from selected population
        int indexOfBest = fittest(selectedPopFitness);
        return selectedPop[indexOfBest];
    }

    //Selecting the best from population
    public int fittest(double[] fitness){
        double min = 0;
        int index = 0;
        for (int i = 0; i < fitness.length; i++)
            if(min > fitness[i]){
                min = fitness[i];
                index = i;
            }
        return index;
    }

    public int[][] crossover(int[] parent1, int[] parent2){
        int[] child1 = new int[parent1.length];
        int[] child2 = new int[parent2.length];
        Random random = new Random(System.nanoTime());
        int point1 = Math.abs(random.nextInt()) % parent1.length;
        int point2 = Math.abs(random.nextInt()) % parent2.length;
        if(point1 > point2) { // Swap Point1 and point 2
            point1 = point1 + point2;
            point2 = point1 - point2;
            point1 = point1 - point2;
        }
//        System.out.println("Point 1 = " + point1);
//        System.out.println("Point 2 = " + point2);
        LinkedHashSet<Integer> parent1Set = new LinkedHashSet<>();
        LinkedHashSet<Integer> parent1Intersect = new LinkedHashSet<>();
        LinkedHashSet<Integer> parent2Set = new LinkedHashSet<>();
        LinkedHashSet<Integer> parent2Intersect = new LinkedHashSet<>();
        for (int i = point1; i <= point2 ; i++) {
            child1[i] = parent1[i];
            child2[i] = parent2[i];
            parent1Intersect.add(parent1[i]);
            parent2Intersect.add(parent2[i]);
        }


//        System.out.println("Parent 1 Intersect " + parent1Intersect);
//        System.out.println("Parent 2 Intersect " + parent2Intersect);
        for(int i = point2 + 1; (i % parent1.length) != point2 ; i++){
            int index = i % parent1.length;
            parent1Set.add(parent1[index]);
            parent2Set.add(parent2[index]);
        }
        parent1Set.add(parent1[point2]);
        parent2Set.add(parent2[point2]);

//        System.out.println("Parent 1 set : " + parent1Set);
//        System.out.println("Parent 2 set : " + parent2Set);
        parent1Set.removeAll(parent2Intersect);
        parent2Set.removeAll(parent1Intersect);
//        System.out.println("Parent 1 set  after difference: " + parent1Set);
//        System.out.println("Parent 2 set after difference: " + parent2Set);


        Iterator<Integer> it1 = parent1Set.iterator();
        Iterator<Integer> it2 = parent2Set.iterator();
        // Copy some part of parent1 to child2
        // Copy some part of parent2 to child1
        for (int i = point2 + 1; it1.hasNext() && it2.hasNext() && (i % parent1.length) != point1; i++) {
            int index = i % parent1.length;

            int val1 = it1.next();
            child2[index] = val1;

            int val2 = it2.next();
            child1[index] = val2;
        }

//        System.out.println("\nChild 1 : ");
//        Arrays.stream(child1).forEach(x-> System.out.print(x + " "));
//        System.out.println();
//        System.out.println("\nChild 2 : ");
//        Arrays.stream(child2).forEach(x-> System.out.print(x + " "));
//        System.out.println();
        return new int[][]{child1,child2};
    }


    public void mutation(int[][] childs, double mutationRate){
        Random random = new Random(System.nanoTime());
        for (int[] child : childs)
            if(Math.random() > mutationRate){
                int i = Math.abs(random.nextInt()) % child.length;
                int j = Math.abs(random.nextInt()) % child.length;
                int temp = child[i];
                child[i] = child[j];
                child[j] = temp;
            }
    }

    public void replacement(Data data, double[] fitness, int[][] childs){
        int[] indexes = twoWorse(fitness);
        int i = indexes[0], j = indexes[1];
        int[][] pop = data.getPopulation();
        pop[i] = childs[0];
        pop[j] = childs[1];
    }

    public int[] twoWorse(double[] fitness){
        double max1 = 0;
        int index1 = 0;
        double max2 = 0;
        int index2 = 0;
        for (int i = 0; i < fitness.length; i++) {
            if(fitness[i] > max2){
                index1 = index2;
                max1 = max2;
                index2 = i;
                max2 = fitness[i];

            }
            else if(fitness[i] > max1) {
                max1 = fitness[i];
                index1 = i;
            }
        }
//        System.out.println("\n max 1 = " + index1 + " max 2 = " + index2);
        return new int[]{index1,index2};
    }

}
