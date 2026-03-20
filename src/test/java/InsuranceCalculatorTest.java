import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class InsuranceCalculatorTest {

    private InsuranceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new InsuranceCalculator();
    }

    // =========================================================================
    // KỸ THUẬT 1: KIỂM THỬ GIÁ TRỊ BIÊN (BVA - 4n + 1)
    // =========================================================================

    @Test
    void testTC1_AllNominalValues() {
        // Cả hai biến ở nom (Age 40, BMI 22.0)
        assertEquals(10500000.0, calculator.calculatePremium(40, 22.0, false, false));
    }

    @ParameterizedTest(name = "TC Biên Tuổi: age={0} => expected={1}")
    @CsvSource({
            "18, 8550000.0",   // TC2: min (Được KH Kim Cương)
            "19, 8550000.0",   // TC3: min+ (Được KH Kim Cương)
            "64, 12500000.0",  // TC4: max-
            "65, 12500000.0"   // TC5: max
    })
    void testTC2_to_TC5_AgeBoundaries(int age, double expectedPremium) {
        assertEquals(expectedPremium, calculator.calculatePremium(age, 22.0, false, false));
    }

    @ParameterizedTest(name = "TC Biên BMI: bmi={0} => expected={1}")
    @CsvSource({
            "10.0, 12500000.0",  // TC6: min
            "10.1, 12500000.0",  // TC7: min+
            "49.9, 14000000.0",  // TC8: max-
            "50.0, 14000000.0"   // TC9: max
    })
    void testTC6_to_TC9_BmiBoundaries(double bmi, double expectedPremium) {
        assertEquals(expectedPremium, calculator.calculatePremium(40, bmi, false, false));
    }

    // =========================================================================
    // KỸ THUẬT 2: BẢNG QUYẾT ĐỊNH (Decision Table)
    // =========================================================================

    @Test
    void testTC10_R1_BothRisksRejected() {
        assertThrows(InsuranceRejectedException.class, () -> {
            calculator.calculatePremium(30, 22.0, true, true);
        });
    }

    @ParameterizedTest(name = "TC Bảng quyết định R2-R7: age={0}, bmi={1}, smoker={2}, chronic={3} => expected={4}")
    @CsvSource({
            "30, 22.0, true,  false, 12500000.0", // TC11 (R2): Chỉ hút thuốc
            "30, 22.0, false, true,  13000000.0", // TC12 (R3): Chỉ bệnh nền
            "22, 22.0, false, false, 8550000.0",  // TC13 (R4): Đạt KH Kim Cương
            "22, 28.0, false, false, 11500000.0", // TC14 (R5): Trượt Kim Cương (BMI thừa cân)
            "30, 22.0, false, false, 10500000.0", // TC15 (R6): Trượt Kim Cương (Lố tuổi)
            "30, 28.0, false, false, 12500000.0"  // TC16 (R7): Trượt Kim Cương (Cả 2)
    })
    void testTC11_to_TC16_DecisionTableRules(int age, double bmi, boolean smoker, boolean chronic, double expectedPremium) {
        assertEquals(expectedPremium, calculator.calculatePremium(age, bmi, smoker, chronic));
    }
}