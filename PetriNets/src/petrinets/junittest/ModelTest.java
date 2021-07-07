package petrinets.junittest;

import org.hamcrest.MatcherAssert;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import petrinets.domain.Model;
import petrinets.domain.net.OrderedPair;
import systemservices.PropertiesHandler;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ModelTest {

    private Model testModel;


    @BeforeAll
    public void setTestEnvironment(){
        PropertiesHandler.initializeProperties();

        try(InputStream in = new FileInputStream(PropertiesHandler.REPO_PROPERTIES_PATH.toFile())) {

            Properties repoProp = new Properties();
            repoProp.load(in);
            in.close();

            OutputStream out = new FileOutputStream(PropertiesHandler.REPO_PROPERTIES_PATH.toFile());
            repoProp.setProperty(PropertiesHandler.DIRECTORY_PROPERTY, PropertiesHandler.DEFAULT_TEST_SAVING_DIR.toString());
            repoProp.store(out, null);
            out.close();

            testModel = new Model();
            //genera le proprietà originali
            PropertiesHandler.initializeProperties();
        }catch(IOException e){
            e.printStackTrace();
            fail("Errore nella lettura del file delle proprietà");
        } catch (ReflectiveOperationException e){
            e.printStackTrace();
            fail("Errore nell'inizializzazione della Repository");
        }

        }

    @Test
    public void testNetCreationAndSavig() throws IOException, ReflectiveOperationException {
            String net1 = "net1";
            testModel.createNet(net1);
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
}
