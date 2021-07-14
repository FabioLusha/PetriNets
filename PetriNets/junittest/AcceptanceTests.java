import org.junit.jupiter.api.*;
import petrinets.UI.controller.Starter;
import petrinets.domain.net.Place;
import petrinets.domain.petrinet.PetriNet;
import systemservices.INetRepository;
import systemservices.PropertiesHandler;
import systemservices.PropertiesInitializationException;
import systemservices.RepositoryFactory;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.fail;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AcceptanceTests {

    public static final String PLACE_NAME = "posto";
    public static final String TRANSITION_NAME = "transizione";
    private static Properties original;
    public static final String PETRI_NET_NAME = "retePetri";
    public static final String NET_NAME = "rete";

    @BeforeAll
    public static void setTestEnvironement(){
        try{
            if(! PropertiesHandler.REPO_PROPERTIES_PATH.toFile().exists())
                PropertiesHandler.initializeRepositoryProperties();
            if(! PropertiesHandler.TEST_REPO_PROPERTIES_PATH.toFile().exists())
                PropertiesHandler.initializeTestProperties();

            try(InputStream in = new FileInputStream(PropertiesHandler.REPO_PROPERTIES_PATH.toFile());
                InputStream testIn = new FileInputStream(PropertiesHandler.TEST_REPO_PROPERTIES_PATH.toFile())
            ) {

                original = new Properties();
                original.load(in);
                in.close();

                Properties testRepoProp = new Properties();
                testRepoProp.load(testIn);


                OutputStream out = new FileOutputStream(PropertiesHandler.REPO_PROPERTIES_PATH.toFile());
                testRepoProp.store(out, null);
                out.close();

                File savingFile = new File(testRepoProp.getProperty(PropertiesHandler.FILE_NAME_PATH));
                if(savingFile.exists())
                    savingFile.delete();
            }
        }catch(IOException e){
            e.printStackTrace();
            fail("Errore nella lettura del file delle proprietà");
        }catch (PropertiesInitializationException e){
            fail("Errore nell'inizializzaizone del file delle proprietà");
        }
    }

    @AfterAll
    public static void resetProductionEnvironment(){
        //genera le proprietà originali

        try (OutputStream out = new FileOutputStream(PropertiesHandler.REPO_PROPERTIES_PATH.toFile())){
            original.store(out, null);
            out.close();
            InputStream in = new FileInputStream(PropertiesHandler.REPO_PROPERTIES_PATH.toFile());
            Properties prop = new Properties();
            prop.load(in);

            assertThat(original,equalTo(prop));
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    @Order(1)
    public void createNetTest() {


        String inputs = "1\n" //accedi come configuratore
        + "1\n" //crea rete
        + NET_NAME +"\n" //nome rete
        + PLACE_NAME + "\n" //nome posto
        + TRANSITION_NAME + "\n" //nome transizione
        + "0\n" //direzione
        + "n\n" //termina l'inserimento
        + "1\n" //salva rete
        + "0\n"; //esci
        //+ "0\n"; //chiudi

        InputStream in = new ByteArrayInputStream(inputs.getBytes());
        OutputStream out = System.out;

        Runnable toBeRun = () -> {
            Starter starter = new Starter(in, out);
            starter.startView();
        };

        try {
        Thread normalExec = new Thread(toBeRun);
        normalExec.start();
        normalExec.join();

        INetRepository repo = RepositoryFactory.getInstance().getRepo();
        assertThat(true, equalTo(repo.contains(NET_NAME)));
        } catch (IOException | ReflectiveOperationException | PropertiesInitializationException | InterruptedException e) {
            fail(e);
        }

    }

    @Test
    @Order(2)
    public void createPetriNetTest() throws PropertiesInitializationException, ReflectiveOperationException, IOException {

        String inputs = "1\n" //accedi come configuratore
                + "3\n" //crea rete di petri
                + NET_NAME + "\n" //rete base
                + PETRI_NET_NAME + "\n" //nome rete di petri
                + "4\n" //salva
                + "0\n"; //esci
                //+ "0\n"; //chiudi

        InputStream in = new ByteArrayInputStream(inputs.getBytes());
        OutputStream out = OutputStream.nullOutputStream();

        Runnable toBeRun = () -> {
            Starter starter = new Starter(in, out);
            starter.startView();
        };

        try {
        Thread normalExec = new Thread(toBeRun);
        normalExec.start();
        normalExec.join();



        INetRepository repo = RepositoryFactory.getInstance().getRepo();
        assertThat(true, equalTo(repo.contains(PETRI_NET_NAME)));
        assertThat(repo.get(PETRI_NET_NAME).getClass(), equalTo(PetriNet.class));
        } catch (IOException | ReflectiveOperationException | PropertiesInitializationException | InterruptedException e) {
            fail(e);
        }

    }

    @Test
    @Order(3)
    public void changeMarcCorrectValue(){
        int correctValue = 40;

        String anotherPetriNetName = "petri2";
        String inputs = "1\n" //accedi come configuratore
                + "3\n" //crea rete di petri
                + NET_NAME + "\n" //rete base
                + anotherPetriNetName + "\n" //nome rete di petri
                + "1\n" //scegli di cambiare relazione di flusso
                + PLACE_NAME + "\n" //nome del posto da cambiare
                + correctValue + "\n" //nuovo valore
                + "4\n" //salva
                + "0\n"; //esci
                //+ "0\n"; //chiudi

        InputStream in = new ByteArrayInputStream(inputs.getBytes());
        OutputStream out = OutputStream.nullOutputStream();

        Runnable toBeRun = () -> {
            Starter starter = new Starter(in, out);
            starter.startView();
        };

        try {
            Thread normalExec = new Thread(toBeRun);
            normalExec.start();
            normalExec.join();

            INetRepository repo = RepositoryFactory.getInstance().getRepo();

            assertThat(true, equalTo(repo.contains(PETRI_NET_NAME)));
            assertThat(repo.get(anotherPetriNetName).getClass(), equalTo(PetriNet.class));

            PetriNet petriNet = (PetriNet) repo.get(anotherPetriNetName);
            assertThat(correctValue,equalTo(petriNet.getMarcValue(new Place(PLACE_NAME))));

        } catch (IOException | ReflectiveOperationException | PropertiesInitializationException | InterruptedException e) {
            fail(e);
        }
    }


}
