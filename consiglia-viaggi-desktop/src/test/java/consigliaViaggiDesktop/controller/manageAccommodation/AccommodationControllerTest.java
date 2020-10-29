package consigliaViaggiDesktop.controller.manageAccommodation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccommodationControllerTest {

    static AccommodationController accommodationController;
    @BeforeAll
    static void init(){
        accommodationController = new AccommodationController();
    }
    // White-Box Test

    @Test
    void validateLatLong_4_7_10() {
        assertEquals(true,accommodationController.validateLatLong(50,0));
    }

    @Test
    void validateLatLong_4_5() {
        assertEquals(false,accommodationController.validateLatLong(-200,0));
    }

    @Test
    void validateLatLong_4_7_8() { assertEquals(false,accommodationController.validateLatLong(0,235)); }


    // Black-Box Test

    @Test
    void validateLatLongWithLatLessThanMinus90() {

        assertEquals(false,accommodationController.validateLatLong(-91,0));
    }

    @Test
    void validateLatLongWithLatGreaterThan90() {
        assertEquals(false,accommodationController.validateLatLong(91,0));
    }

    @Test
    void validateLatLongWithLongGreaterThan180() {
        assertEquals(false,accommodationController.validateLatLong(0,181));
    }


    @Test
    void validateLatLongWithLongLessThanMinus180() {
        assertEquals(false,accommodationController.validateLatLong(0,-181));
    }

    @Test
    void validateLatLongWithValidValues() {
        assertEquals(true,accommodationController.validateLatLong(0,0));
    }



}