package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.entdes.todolist.*;
import org.junit.jupiter.api.*;

public class TascaTest {

 //Mirar ID que incrementa
 
    @Test
    void TestGetId (){
        Tasca tasca = new Tasca(null);  
        assertEquals(1, tasca.getId());
        Tasca tasca2 = new Tasca(null);
        assertEquals(2, tasca2.getId());
    }
    
    @Test
    void testToStringCompletada (){
        Tasca tasca = new Tasca("nova tasca");
        tasca.setCompletada(true);
        assertEquals("nova tasca: Completada", tasca.toString());
    }
    @Test
    void testToStringPendent (){
        Tasca tasca = new Tasca("nova tasca");
        tasca.setCompletada(false);
        assertEquals("nova tasca: Pendent", tasca.toString());
    }
    @Test
    void testIsCompletada (){
        Tasca tasca = new Tasca("nova tasca");
        tasca.setCompletada(true);
        assertTrue(tasca.isCompletada());
    }
    @Test
    void testGetDescripcio (){
        Tasca tasca = new Tasca("nova tasca");
        tasca.setDescripcio("nova descripcio");
        assertEquals("nova descripcio", tasca.getDescripcio());
    }
    @Test
    void testGetDataInici (){
        Tasca tasca = new Tasca("nova tasca");
        tasca.setDataInici(LocalDate.of(2024,2,13)); 
        assertEquals(LocalDate.of(2024,2,13), tasca.getDataInici());
    }
    @Test
    void testGetDataFiPrevista (){
        Tasca tasca = new Tasca("nova tasca");
        tasca.setDataFiPrevista(LocalDate.of(2022,2,1)); 
        assertEquals(LocalDate.of(2022,2,1), tasca.getDataFiPrevista());
    }
    @Test
    void testGetPrioritat(){
        Tasca tasca = new Tasca("nova tasca");
        tasca.setPrioritat(2);
        assertEquals(2, tasca.getPrioritat());
    }
    @Test
    void testGetDataFiReal (){
        Tasca tasca = new Tasca("nova tasca");
        tasca.setDataFiReal(LocalDate.of(2022,12,1)); 
        assertEquals(LocalDate.of(2022,12,1), tasca.getDataFiReal());
    }
    
}
