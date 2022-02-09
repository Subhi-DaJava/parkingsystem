package com.parkit.parkingsystem.util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;



class InputReaderUtilTest {

    private InputReaderUtil inputReaderUtil;

    @BeforeEach
    public void setUp() {
        inputReaderUtil = new InputReaderUtil();
    }


    @Test
    public void readSelection() {


        inputReaderUtil.setScan(new Scanner("1"));

        int choice = inputReaderUtil.readSelection();

        assertEquals(1, choice);
    }

    @Test
    @Disabled
    public void readSelectionThrowsException(){

        inputReaderUtil.setScan(new Scanner("subh"));

        assertThrows(NumberFormatException.class, ()-> inputReaderUtil.readSelection());
        assertThrows(IllegalArgumentException.class,()-> inputReaderUtil.readSelection());
    }


    @Test
    public void readVehicleRegistrationNumber() throws Exception {

        inputReaderUtil.setScan(new Scanner("ABCDE"));

        String regNumber = inputReaderUtil.readVehicleRegistrationNumber();

        assertEquals("ABCDE",regNumber);

    }
    @Test
    public void readVehicleRegistrationNumberNull() throws Exception {

        inputReaderUtil.setScan(new Scanner(" "));

        assertThrows(IllegalArgumentException.class,()-> inputReaderUtil.readVehicleRegistrationNumber());

    }

    @Test
    @Disabled
    protected void finalize() throws Throwable{

    }
}