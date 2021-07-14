import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import petrinets.domain.NetLogic;
import petrinets.domain.PetriNetLogic;
import petrinets.domain.PriorityPetriNetLogic;
import petrinets.domain.petrinet.PetriNet;
import petrinets.domain.net.*;
import systemservices.PropertiesHandler;
import systemservices.PropertiesInitializationException;

import java.util.*;
import java.io.*;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ModelTest {

    private NetLogic netLogic;
    private PetriNetLogic petriNetLogic;
    private PriorityPetriNetLogic priorityPetriNetLogic;


    @BeforeAll
    public void setTestEnvironment(){

        try{
            if(! PropertiesHandler.REPO_PROPERTIES_PATH.toFile().exists())
                PropertiesHandler.initializeRepositoryProperties();

            try(InputStream in = new FileInputStream(PropertiesHandler.REPO_PROPERTIES_PATH.toFile())) {

                Properties oldRepoProp = new Properties();
                Properties testRepoProp;
                oldRepoProp.load(in);
                testRepoProp =  SerializationUtils.clone(oldRepoProp);
                in.close();

                OutputStream out = new FileOutputStream(PropertiesHandler.REPO_PROPERTIES_PATH.toFile());
                testRepoProp.setProperty(PropertiesHandler.FILE_NAME_PROPERTY, PropertiesHandler.DEFAULT_TEST_SAVING_PATH.toString());
                testRepoProp.store(out, null);
                out.close();

                netLogic = new NetLogic();
                petriNetLogic = new PetriNetLogic();
                priorityPetriNetLogic = new PriorityPetriNetLogic();
                //genera le proprietà originali
                out = new FileOutputStream(PropertiesHandler.REPO_PROPERTIES_PATH.toFile());
                oldRepoProp.store(out, null);
                out.close();
        }
        }catch(IOException e){
            e.printStackTrace();
            fail("Errore nella lettura del file delle proprietà");
        } catch (ReflectiveOperationException e){
            e.printStackTrace();
            fail("Errore nell'inizializzazione della Repository");
        }catch (PropertiesInitializationException e){
            fail("Errore nell'inizializzaizone del file delle proprietà");
        }

        }

    @Test
    public void testNetCreationAndSavig() throws IOException, ReflectiveOperationException {
            String net1 = "net1";
            assertThat(true,equalTo(netLogic.createNet(net1)));
            netLogic.addFluxElem("p1","t1", 0);
            netLogic.saveCurrentNet();

            assertThat(netLogic.containsINet(net1), is(true));

            netLogic.remove(net1);

    }

    @Test
    public void testAddingFluxRelElementNet(){
        String testNet = "net";
        String placeName = "p1";
        String transitionName = "t1";
        OrderedPair.Direction direction = OrderedPair.Direction.tp;

        netLogic.createNet(testNet);
        netLogic.addFluxElem(placeName, transitionName, direction.ordinal());
        netLogic.saveCurrentNet();
        OrderedPair fluxElem = netLogic.getINet(testNet).getFluxRelation().stream().findFirst().orElse(null);

        if(fluxElem == null) fail("Expected OrderedPair, found null");

        assertThat(fluxElem.getCurrentPlace().getName(),equalTo(placeName));
        assertThat(fluxElem.getCurrentTransition().getName(),is(equalTo(transitionName)));
        assertThat(fluxElem.getDirection(),is(equalTo(direction)));

        netLogic.remove(testNet);
    }

    @Test
    public void testPetriNetCreationAndSaving(){
        String testNet = "net";
        String placeName = "p1";
        String transitionName = "t1";
        OrderedPair.Direction direction = OrderedPair.Direction.tp;

        netLogic.createNet(testNet);
        netLogic.addFluxElem(placeName, transitionName, direction.ordinal());
        netLogic.saveCurrentNet();


        String testPetriNetName = "PetriNet";
        Net baseNet = (Net) petriNetLogic.getINet(testNet);
        petriNetLogic.createPetriNet(testPetriNetName, testNet);

        PetriNet pn = new PetriNet(testPetriNetName,baseNet);

        assertThat(pn, is(equalTo(petriNetLogic.getCurrentPetriNet())));

        petriNetLogic.saveCurrentNet();

        assertThat(true,equalTo(petriNetLogic.containsINet(testPetriNetName)));

        petriNetLogic.remove(testNet);
        petriNetLogic.remove(testPetriNetName);
    }

    @Test
    public void testAddingSamePetriNet(){
        String testNet = "net";
        String placeName = "p1";
        String transitionName = "t1";
        OrderedPair.Direction direction = OrderedPair.Direction.tp;

        netLogic.createNet(testNet);
        netLogic.addFluxElem(placeName, transitionName, direction.ordinal());
        netLogic.saveCurrentNet();


        String testPetriNetName = "PetriNet";

        petriNetLogic.createPetriNet(testPetriNetName, testNet);

        petriNetLogic.saveCurrentNet();

        assertThat(false, equalTo(petriNetLogic.createPetriNet(testPetriNetName, testNet)));

        String differentPetriNetName = "pn2";
        //permette l'instazione di una PetriNet con nome diverso
        assertThat(true,equalTo(petriNetLogic.createPetriNet(differentPetriNetName,testNet)));
        //Non permette di salvare una PetriNet con una tipologia già esistente
        assertThat(false,equalTo(petriNetLogic.saveCurrentNet()));
        petriNetLogic.remove(testNet);
        petriNetLogic.remove(testPetriNetName);

    }

    @Test
    public void testPetriNetChangeMarc(){
        String testNet = "net";
        String placeName = "p1";
        String transitionName = "t1";
        OrderedPair.Direction direction = OrderedPair.Direction.tp;

        netLogic.createNet(testNet);
        netLogic.addFluxElem(placeName, transitionName, direction.ordinal());
        netLogic.saveCurrentNet();

        String testPetriNetName = "PetriNet";

        petriNetLogic.createPetriNet(testPetriNetName, testNet);

        assertThat(0, equalTo(petriNetLogic.getCurrentPetriNet().getMarcValue(new Place(placeName))));

        int newMarcVal = 4;
        petriNetLogic.changeMarc(placeName, newMarcVal);

        assertThat(newMarcVal, equalTo(petriNetLogic.getCurrentPetriNet().getMarcValue(new Place(placeName))));

        petriNetLogic.saveCurrentNet();
    }

    @Test
    public void testReturnListOfCorrectTypeNet(){
        String netName = "net1";
        Net net1 = new Net(netName);
        String pnName = "pn1";
        PetriNet pn1 = new PetriNet(pnName,net1);

        netLogic.saveINet(net1);
        netLogic.saveINet(pn1);

        List<String> list = netLogic.getSavedGenericNetsNames(Net.class.getName());
        assertThat(netName,is(equalTo(list.get(0))));


        netLogic.remove(netName);
        netLogic.remove(pnName);
    }

    @Test
    public void testReturnListDoNotContainOtherTypes(){
        String netName = "net1";
        Net net1 = new Net(netName);
        String pnName = "pn1";
        PetriNet pn1 = new PetriNet(pnName,net1);

        netLogic.saveINet(net1);
        netLogic.saveINet(pn1);


            List<String> list = netLogic.getSavedGenericNetsNames(PetriNet.class.getName());
            assertThat(false,is(equalTo(list.contains(netName))));

        netLogic.remove(netName);
        netLogic.remove(pnName);
    }

    /**
     * Testiamo se il metodo che ritorna i nomi delle reti in base al tipo della rete
     * funziona anche fornendoli un'interfaccia (e.g. Inet)
     */
    @Test
    public void testReturnListOfCorrectTypeNetWorksWithInterface(){
        String netName = "net1";
        Net net1 = new Net(netName);
        String pnName = "pn1";
        PetriNet pn1 = new PetriNet(pnName,net1);

        netLogic.saveINet(net1);
        netLogic.saveINet(pn1);

            List<String> list = netLogic.getSavedGenericNetsNames(INet.class.getName());
            assertThat(true,is(equalTo(list.containsAll(Arrays.asList(new String[]{netName, pnName})))));

        netLogic.remove(netName);
        netLogic.remove(pnName);
    }

}
