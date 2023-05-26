package ma.enset.agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import ma.enset.mas.AGUtils;
import ma.enset.mas.Individual;

import java.util.*;

public class IslandAgent extends Agent {

    private Individual[] population=new Individual[AGUtils.POPULATION_SIZE];

    private Individual individual1;
    private Individual individual2;

    Random rnd=new Random();


    @Override
    protected void setup() {

        DFAgentDescription dfAgentDescription=new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription=new ServiceDescription();
        serviceDescription.setType("ga");
        serviceDescription.setName("Island");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this,dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        SequentialBehaviour sequentialBehaviour=new SequentialBehaviour();

        sequentialBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                initialaizePopulation();
                sortPopulation();
            }
        });

        sequentialBehaviour.addSubBehaviour(new Behaviour() {
            int it=0;
            @Override
            public void action() {
                crossover();
                mutation();
                sortPopulation();
            }

            @Override
            public boolean done() {
                return AGUtils.MAX_IT<it && getBestFitness()<AGUtils.CHROMOSOME_SIZE;
            }
        });

    }

    public void initialaizePopulation(){
        for (int i=0;i<AGUtils.POPULATION_SIZE;i++){
            population[i]=new Individual();
            population[i].calculateFitness();
        }
    }

    /*public void selection(){
        firstFitness=individuals.get(0);
        secondFitness=individuals.get(1);
    }*/
    //croisement
    public void crossover(){

        individual1=new Individual();
        individual2=new Individual();

        int pointCroisment=rnd.nextInt(AGUtils.CHROMOSOME_SIZE-1);
        pointCroisment++;


        for (int i=0;i<pointCroisment;i++) {
            individual1.getGenes()[i]=population[1].getGenes()[i];
            individual2.getGenes()[i]=population[0].getGenes()[i];
        }
    }
    public void mutation(){

        if (rnd.nextDouble()<AGUtils.MUTATION_PROB){
            int index=rnd.nextInt(AGUtils.CHROMOSOME_SIZE);
            individual1.getGenes()[index]= (char) (1-individual1.getGenes()[index]);
        }

        if (rnd.nextDouble()<AGUtils.MUTATION_PROB){
            int index=rnd.nextInt(AGUtils.CHROMOSOME_SIZE);
            individual2.getGenes()[index]= (char) (1-individual2.getGenes()[index]);
        }

        individual1.calculateFitness();
        individual1.calculateFitness();
        population[AGUtils.POPULATION_SIZE-2]=individual1;
        population[AGUtils.POPULATION_SIZE-1]=individual2;
    }

    public void sortPopulation(){
        Arrays.sort(population, Comparator.reverseOrder());
    }

    public void showPopulation(){
        for (Individual individual:population) {
            System.out.println(Arrays.toString((individual.getGenes()+" = "+individual.getFitness()).toCharArray()));
        }
    }

    public int getBestFitness(){
        return population[0].getFitness();
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }
}
