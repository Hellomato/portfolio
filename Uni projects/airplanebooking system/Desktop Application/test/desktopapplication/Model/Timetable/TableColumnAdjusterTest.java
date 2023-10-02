/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopapplication.Model.Timetable;

import javax.swing.JTable;
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
public class TableColumnAdjusterTest {
    
    public TableColumnAdjusterTest() {
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
    public void testAdjustColumns() {
        System.out.println("testWidth");
        
        //create a Jtable
        Object rowData[][] = { { "Row1-Column1", "Row1-Column2Row1-Column2Row1-Column2" },
            { "Row2-Column1", "Row2-Column2Row1-Column2Row1-Column2" } };
        Object columnNames[] = { "Column One", "Column Two"};
        
        //have two table variables table(modified) and table 2 (unmodified)
        JTable table = new JTable(rowData, columnNames);
        
        //create class instance and attempt to modify the columns margins
        View.TableColumnAdjuster tca = new View.TableColumnAdjuster(table);
        tca.adjustColumns();
        
        // if the first column width is smaller than the second one, it means it worked
        // the content of the rows of the second column are much lengthier
        assertTrue(table.getColumnModel().getColumn(0).getWidth() < table.getColumnModel().getColumn(1).getWidth());
    }
    
}
