/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopapplication.Model.Users;

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
public class GuardTest {
    
    public GuardTest() {
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
    public void testLogin() {
    }

    @Test
    public void testValidateTicket() {
        System.out.println("Testing Validate Ticket");
        Guard guard = new Guard("GLman", "Glman");
        boolean response;
        boolean expected = true;
        
        response = guard.ValidateTicket(1);
        assertEquals(expected, response);
    }
    
}
