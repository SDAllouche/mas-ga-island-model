package ma.enset.agent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import ma.enset.mas.AGUtils;

public class SimpleContainer {

    public static void main(String[] args) throws StaleProxyException {

        Runtime runtime=Runtime.instance();
        ProfileImpl profile=new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST,"localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);
        AgentController mainAgent=null;
        for (int i = 0; i< AGUtils.ISLAND_NUMBER; i++){
            mainAgent = agentContainer.createNewAgent("Island "+i+1, IslandAgent.class.getName(), new Object[]{});
            mainAgent.start();
        }
        mainAgent = agentContainer.createNewAgent("mainAgent", MasterAgent.class.getName(), new Object[]{});
        mainAgent.start();

    }
}