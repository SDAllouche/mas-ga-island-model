package ma.enset.mas;

import java.util.Random;

public class Individual implements Comparable{

    private char genes[]=new char[AGUtils.CHROMOSOME_SIZE];
    private int fitness;

    public Individual() {
        Random rnd=new Random();
        for (int i=0;i<genes.length;i++){
            genes[i]= AGUtils.CHARATERS.charAt(rnd.nextInt(AGUtils.CHARATERS.length()));
        }
    }
    public void calculateFitness(){
        fitness=0;
        for (int i=0;i<AGUtils.CHROMOSOME_SIZE;i++) {
            if(genes[i]==AGUtils.SOLUTION.charAt(i))
                fitness+=1;
        }
    }

    public int getFitness() {
        return fitness;
    }

    public void setGenes(char[] genes) {
        this.genes = genes;
    }

    public char[] getGenes() {
        return genes;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Object o) {
        Individual individual=(Individual) o;
        if (this.fitness>individual.fitness)
            return 1;
        else if(this.fitness<individual.fitness){
            return -1;
        }else
            return 0;
    }
}
