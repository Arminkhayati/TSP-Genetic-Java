package tsp;

import java.util.Arrays;
import java.util.stream.IntStream;

public class GeneticAlg {

    public void start(Data data){

        Util util = new Util();
        int[][] choosenPopulation = new int[data.popSize][data.cityCount];
        double fitnessOfChoosenPop = Integer.MAX_VALUE;
        double[] fitness = new double[data.popSize];
        for(int count = 0 ; count < 10 ; count++){
            fitness = util.fitness(data);
            System.out.println("Fitness : ");
            Arrays.stream(fitness).forEach(x-> System.out.print(x + " "));
            double sumOfFitness = Arrays.stream(fitness).sum();
            System.out.println("\nSum of fitness is : " + sumOfFitness);
            if(fitnessOfChoosenPop > sumOfFitness) {
                System.arraycopy(data.getPopulation(), 0,choosenPopulation, 0,data.popSize);
                fitnessOfChoosenPop = sumOfFitness;
            }

            int[] parent1 = util.selectParent(data, fitness, 3);
            int[] parent2 = util.selectParent(data, fitness, 3);

            int[][] childs = util.crossover(parent1,parent2);
            util.mutation(childs, 0.15);
            util.replacement(data,fitness,childs);

            System.out.println("\nNew Population is : ");
            for (int[] ints : data.getPopulation()) {
                for (int anInt : ints) {
                    System.out.print(" " + anInt);
                }
                System.out.println();
            }
            System.out.println(" \n \n ");
        }


        System.out.println("\nChoosen Population is : ");
        for (int[] ints : choosenPopulation) {
            for (int anInt : ints) {
                System.out.print(" " + anInt);
            }
            System.out.println();
        }
        System.out.println(" \n Sum of Fittness =  " + fitnessOfChoosenPop);

        int fittest = util.fittest(fitness);
        System.out.print(" \n Best Individual (Answer) is : ");
        Arrays.stream(choosenPopulation[fittest]).forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

}
