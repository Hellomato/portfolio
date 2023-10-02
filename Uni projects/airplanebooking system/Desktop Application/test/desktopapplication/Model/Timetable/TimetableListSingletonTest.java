/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopapplication.Model.Timetable;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gabryel
 */
public class TimetableListSingletonTest {
    
    public TimetableListSingletonTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetInstance() throws Exception {
        System.out.println("Testing Singleton Instance objects");
        
        TimetableListSingleton data = TimetableListSingleton.getInstance();
        System.out.println("sdasd");
        TimetableListSingleton data1 = TimetableListSingleton.getInstance();
           
        assertSame(data, data1);
    }
    
}
