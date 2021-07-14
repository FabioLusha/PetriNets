import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import petrinets.domain.Model;
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

    private Model testModel;


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
                testRepoProp.setProperty(PropertiesHandler.FILE_NAME_PATH, PropertiesHandler.DEFAULT_TEST_SAVING_PATH.toString());
                testRepoProp.store(out, null);
                out.close();

                testModel = new Model();
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
            assertThat(true,equalTo(testModel.createNet(net1)));
            testModel.addFluxElem("p1","t1", 0);
            testModel.saveCurrentNet();

            assertThat(testModel.containsINet(net1), is(true));

            testModel.remove(net1);

    }

    @Test
    public void testAddingFluxRelElementNet(){
        String testNet = "net";
        String placeName = "p1";
        String transitionName = "t1";
        OrderedPair.Direction direction = OrderedPair.Direction.tp;

        testModel.createNet(testNet);
        testModel.addFluxElem(placeName, transitionName, direction.ordinal());
        testModel.saveCurrentNet();
        OrderedPair fluxElem = testModel.getINet(testNet).getFluxRelation().stream().findFirst().orElse(null);

        if(fluxElem == null) fail("Expected OrderedPair, found null");

        assertThat(fluxElem.getCurrentPlace().getName(),equalTo(placeName));
        assertThat(fluxElem.getCurrentTransition().getName(),is(equalTo(transitionName)));
        assertThat(fluxElem.getDirection(),is(equalTo(direction)));

        testModel.remove(testNet);
    }

    @Test
    public void testPetriNetCreationAndSaving(){
        String testNet = "net";
        String placeName = "p1";
        String transitionName = "t1";
        OrderedPair.Direction direction = OrderedPair.Direction.tp;

        testModel.createNet(testNet);
        testModel.addFluxElem(placeName, transitionName, direction.ordinal());
        testModel.saveCurrentNet();


        String testPetriNetName = "PetriNet";
        Net baseNet = (Net) testModel.getINet(testNet);
        testModel.createPetriNet(testPetriNetName, testNet);

        PetriNet pn = new PetriNet(testPetriNetName,baseNet);

        assertThat(pn, is(equalTo(testModel.getCurrentPetriNet())));

        testModel.saveCurrentPetriNet();

        assertThat(true,equalTo(testModel.containsINet(testPetriNetName)));

        testModel.remove(testNet);
        testModel.remove(testPetriNetName);
    }

    @Test
    public void testAddingSamePetriNet(){
        String testNet = "net";
        String placeName = "p1";
        String transitionName = "t1";
        OrderedPair.Direction direction = OrderedPair.Direction.tp;

        testModel.createNet(testNet);
        testModel.addFluxElem(placeName, transitionName, direction.ordinal());
        testModel.saveCurrentNet();


        String testPetriNetName = "PetriNet";
        Net baseNet = (Net) testModel.getINet(testNet);
        testModel.createPetriNet(testPetriNetName, testNet);

        testModel.saveCurrentPetriNet();

        assertThat(false, equalTo(testModel.createPetriNet(testPetriNetName, testNet)));

        String differentPetriNetName = "pn2";
        assertThat(true,equalTo(testModel.createPetriNet(differentPetriNetName,testNet)));
        assertThat(false,equalTo(testModel.saveCurrentPetriNet()));
        testModel.remove(testNet);
        testModel.remove(testPetriNetName);

    }

    @Test
    public void testPetriNetChangeMarc(){
        String testNet = "net";
        String placeName = "p1";
        String transitionName = "t1";
        OrderedPair.Direction direction = OrderedPair.Direction.tp;

        testModel.createNet(testNet);
        testModel.addFluxElem(placeName, transitionName, direction.ordinal());
        testModel.saveCurrentNet();

        String testPetriNetName = "PetriNet";
        Net baseNet = (Net) testModel.getINet(testNet);
        testModel.createPetriNet(testPetriNetName, testNet);

        assertThat(0, equalTo(testModel.getCurrentPetriNet().getMarcValue(new Place(placeName))));

        int newMarcVal = 4;
        testModel.changeMarc(placeName, newMarcVal);

        assertThat(newMarcVal, equalTo(testModel.getCurrentPetriNet().getMarcValue(new Place(placeName))));

        testModel.saveCurrentPetriNet();
    }

    @Test
    public void testReturnListOfCorrectTypeNet(){
        String netName = "net1";
        Net net1 = new Net(netName);
        String pnName = "pn1";
        PetriNet pn1 = new PetriNet(pnName,net1);

        testModel.saveINet(net1);
        testModel.saveINet(pn1);

        List<String> list = testModel.getSavedGenericNetsNames(Net.class.getName());
        assertThat(netName,is(equalTo(list.get(0))));


        testModel.remove(netName);
        testModel.remove(pnName);
    }

    @Test
    public void testReturnListDoNotContainOtherTypes(){
        String netName = "net1";
        Net net1 = new Net(netName);
        String pnName = "pn1";
        PetriNet pn1 = new PetriNet(pnName,net1);

        testModel.saveINet(net1);
        testModel.saveINet(pn1);


            List<String> list = testModel.getSavedGenericNetsNames(PetriNet.class.getName());
            assertThat(false,is(equalTo(list.contains(netName))));

        testModel.remove(netName);
        testModel.remove(pnName);
    }

    @Test
    public void testReturnListOfCorrectTypeNetWorksWithInterface(){
        String netName = "net1";
        Net net1 = new Net(netName);
        String pnName = "pn1";
        PetriNet pn1 = new PetriNet(pnName,net1);

        testModel.saveINet(net1);
        testModel.saveINet(pn1);

            List<String> list = testModel.getSavedGenericNetsNames(INet.class.getName());
            assertThat(true,is(equalTo(list.containsAll(Arrays.asList(new String[]{netName, pnName})))));

        testModel.remove(netName);
        testModel.remove(pnName);
    }

}
