package org.entdes.todolist;

import java.time.LocalDate;
import org.entdes.mail.IEmailService;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import org.mockito.*;

class GestorTasquesTest {

    @Mock
    private IEmailService emailServiceMock;

    private GestorTasques gestor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); //inicialitza els mocks de Mockito 
        gestor = new GestorTasques(emailServiceMock, "test@example.com");//gestor amb un mock de emailServiceMock i un correu de prova
    }



    @Test
    void testAfegirTascaCorrectament() throws Exception {
        String descripcio = "Nova tasca";
        LocalDate dataInici = LocalDate.now();
        LocalDate dataFiPrevista = LocalDate.now().plusDays(5);//data d'ara mes 5 dies (plusDays(5))
        Integer prioritat = 3;
        int tascaId = gestor.afegirTasca(descripcio, dataInici, dataFiPrevista, prioritat);

        assertEquals(1, gestor.getNombreTasques(), "La tasca ha d'haver estat afegida.");
    }

    @Test
    void testAfegirTascaSenseDescripcio() {
        String descripcio = "";
        LocalDate dataInici = LocalDate.now();
        LocalDate dataFiPrevista = LocalDate.now().plusDays(5);
        Integer prioritat = 3;

        assertThrows(Exception.class, () -> gestor.afegirTasca(descripcio, dataInici, dataFiPrevista, prioritat), "La descripció no pot estar buida.");
    }

    @Test
    void testAfegirTascaDataIniciIncorrecta() {
        String descripcio = "Tasca amb data incorrecta";
        LocalDate dataInici = LocalDate.now().minusDays(1);  //data d'avui menys un dia (minusDays(1))
        LocalDate dataFiPrevista = LocalDate.now().plusDays(5);
        Integer prioritat = 3;

        assertThrows(Exception.class, () -> gestor.afegirTasca(descripcio, dataInici, dataFiPrevista, prioritat), "La data d'inici no pot ser anterior a l'actual.");
    }

    @Test
    void testAfegirTascaDataFiIncorrecta() {
        String descripcio = "Tasca amb data incorrecta";
        LocalDate dataInici = LocalDate.now();
        LocalDate dataFiPrevista = LocalDate.now().minusDays(1);  
        Integer prioritat = 3;

        assertThrows(Exception.class, () -> gestor.afegirTasca(descripcio, dataInici, dataFiPrevista, prioritat), "La data de fi no pot ser anterior a la data d'inici.");
    }

    @Test
    void testAfegirTascaPrioritatNull() throws Exception {
        String descripcio = "Tasca amb prioritat nul·la";
        LocalDate dataInici = LocalDate.now();
        LocalDate dataFiPrevista = LocalDate.now().plusDays(5);
        Integer prioritat = null;

        int tascaId = gestor.afegirTasca(descripcio, dataInici, dataFiPrevista, prioritat);

        Tasca tasca = gestor.obtenirTasca(tascaId);
        assertNull(tasca.getPrioritat(), "La prioritat ha de ser nul·la.");
    }

    @Test
    void testAfegirTascaConDescripcionDuplicada() throws Exception {
        gestor.afegirTasca("Tasca única", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        assertThrows(Exception.class, () -> gestor.afegirTasca("Tasca única", LocalDate.now(), LocalDate.now().plusDays(5), 3), "Ja existeix una tasca amb la mateixa descripció.");
    }

    @Test
    void testAfegirTascaSenseDataInici() throws Exception {
        String descripcio = "Tasca sense data d'inici";
        LocalDate dataFiPrevista = LocalDate.now().plusDays(5);
        Integer prioritat = 3;

        int tascaId = gestor.afegirTasca(descripcio, null, dataFiPrevista, prioritat);
        
        Tasca tasca = gestor.obtenirTasca(tascaId);
        assertNull(tasca.getDataInici(), "La data d'inici ha de ser null.");
    }

    @Test
    void testAfegirTascaSenseDataFiPrevista() throws Exception {
        String descripcio = "Tasca sense data de fi prevista";
        LocalDate dataInici = LocalDate.now();
        Integer prioritat = 3;

        int tascaId = gestor.afegirTasca(descripcio, dataInici, null, prioritat);

        Tasca tasca = gestor.obtenirTasca(tascaId);
        assertNull(tasca.getDataFiPrevista(), "La data de fi prevista ha de ser null.");
    }



    @Test
    void testEliminarTascaCorrectament() throws Exception {
        int tascaId = gestor.afegirTasca("Tasca eliminada", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        gestor.eliminarTasca(tascaId);

        assertEquals(0, gestor.getNombreTasques(), "S'ha d'eliminar la tasca.");
    }

    @Test
    void testEliminarTascaNoExistent() {
        assertThrows(Exception.class, () -> gestor.eliminarTasca(999), "La tasca no existeix");
    }



    @Test
    void testMarcarCompletadaCorrectament() throws Exception {
        int tascaId = gestor.afegirTasca("Tasca per completar", LocalDate.now(), LocalDate.now().plusDays(5), 3);
        gestor.marcarCompletada(tascaId);

        Tasca tasca = gestor.obtenirTasca(tascaId);
        assertTrue(tasca.isCompletada(), "La tasca ha de ser completada.");
    }

    @Test
    void testMarcarCompletadaNoExistent() {
        assertThrows(Exception.class, () -> gestor.marcarCompletada(999), "La tasca no existeix");
    }



    @Test
    void testModificarTascaSenseDescripcio() throws Exception{
        int tascaId = gestor.afegirTasca("Tasca original", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        assertThrows(Exception.class, () -> gestor.modificarTasca(tascaId, "", false, LocalDate.now(), LocalDate.now().plusDays(5), 3), "La descripció no pot estar buida.");
    }

    @Test
    void testModificarTascaDataIniciIncorrecta() throws Exception {
        int tascaId = gestor.afegirTasca("Tasca original", LocalDate.now(), LocalDate.now().plusDays(5), 3);
    
        gestor.modificarTasca(tascaId, "Tasca modificada", false, LocalDate.now().minusDays(1), LocalDate.now().plusDays(5), 3);
    
        Tasca tascaModificada = gestor.obtenirTasca(tascaId);
    
        assertNotEquals(LocalDate.now().minusDays(1), tascaModificada.getDataInici(), "La data d'inici no ha de ser anterior a l'actual");
    }

    @Test
    void testModificarTascaPrioritatIncorrecta() throws Exception {
        int tascaId = gestor.afegirTasca("Tasca original", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        assertThrows(Exception.class, () -> gestor.modificarTasca(tascaId, "Tasca modificada", false, LocalDate.now(), LocalDate.now().plusDays(5), 6), "La prioritat ha d'estar entre 1 i 5.");
    }

    @Test
    void testModificarTascaNoExistent() {
        assertThrows(Exception.class, () -> gestor.modificarTasca(999, "Tasca modificada", false, LocalDate.now(), LocalDate.now().plusDays(5), 3), "La tasca no existeix");
    }



    @Test
    void testObtenirTascaCorrecte() throws Exception {
        int tascaId = gestor.afegirTasca("Tasca obtenir", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        Tasca tasca = gestor.obtenirTasca(tascaId);
        assertNotNull(tasca, "La tasca ha de existir.");
    }

    @Test
    void testObtenirTascaError() {
        assertThrows(Exception.class, () -> gestor.obtenirTasca(999), "La tasca no existeix");
    }


    @Test
    void testGetNombreTasques() throws Exception {
        gestor.afegirTasca("Tasca 1", LocalDate.now(), LocalDate.now().plusDays(5), 3);
        gestor.afegirTasca("Tasca 2", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        assertEquals(2, gestor.getNombreTasques(), "Ha d'haver 2 tasques.");
    }



    @Test
    void testLlistarTasques() throws Exception {
        gestor.afegirTasca("Tasca 1", LocalDate.now(), LocalDate.now().plusDays(5), 3);
        gestor.afegirTasca("Tasca 2", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        assertEquals(2, gestor.llistarTasques().size(), "Ha d'haver 2 tasques.");
    }

    @Test
    void testLlistarTasquesPerDescripcio() throws Exception {
        gestor.afegirTasca("Tasca de prova", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        assertEquals(1, gestor.llistarTasquesPerDescripcio("prova").size(), "Ha d'haver 1 tasca.");
    }



    @Test
    void testModificarTascaDataFiRealNull() throws Exception {
        int tascaId = gestor.afegirTasca("Tasca original", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        gestor.marcarCompletada(tascaId);

        gestor.modificarTasca(tascaId, "Tasca modificada", false, LocalDate.now(), LocalDate.now().plusDays(5), 3);

        Tasca tascaModificada = gestor.obtenirTasca(tascaId);

        assertNull(tascaModificada.getDataFiReal(), "La data de fi real ha de ser null.");
    }

    @Test
    void testModificarTascaNomesCompletada() throws Exception {
        int tascaId = gestor.afegirTasca("Tasca original", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        gestor.modificarTasca(tascaId, "Tasca original", true, LocalDate.now(), LocalDate.now().plusDays(5), 3);

        Tasca tascaModificada = gestor.obtenirTasca(tascaId);

        assertTrue(tascaModificada.isCompletada(), "La tasca ha d'estar marcada com a completada.");
        assertEquals("Tasca original", tascaModificada.getDescripcio(), "La descripció no ha de canviar.");
    }


    @Test
    void testLlistarTasquesPerDescripcioSenseResultats() throws Exception {
        gestor.afegirTasca("Tasca 1", LocalDate.now(), LocalDate.now().plusDays(5), 3);
        gestor.afegirTasca("Tasca 2", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        assertEquals(0, gestor.llistarTasquesPerDescripcio("no existeix").size(), "No ha de haver tasques coincidents.");
    }

    @Test
    void testLlistarTasquesPerComplecioSenseCompletades() throws Exception {
        int tascaId1 = gestor.afegirTasca("Tasca 1", LocalDate.now(), LocalDate.now().plusDays(5), 3);
        int tascaId2 = gestor.afegirTasca("Tasca 2", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        assertEquals(0, gestor.llistarTasquesPerComplecio(true).size(), "No ha de haver tasques completades.");
    }

    @Test
    void testLlistarTasquesPerComplecioAmbCompletades() throws Exception {
        int tascaId1 = gestor.afegirTasca("Tasca 1", LocalDate.now(), LocalDate.now().plusDays(5), 3);
        int tascaId2 = gestor.afegirTasca("Tasca 2", LocalDate.now(), LocalDate.now().plusDays(5), 3);

        gestor.marcarCompletada(tascaId1);
        gestor.marcarCompletada(tascaId2);

        assertEquals(2, gestor.llistarTasquesPerComplecio(true).size(), "Han d'aparèixer 2 tasques completades.");
    }

}
