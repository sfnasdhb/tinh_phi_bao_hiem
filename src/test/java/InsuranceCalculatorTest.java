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

    // --- KỸ THUẬT 1 & 2 (Của bạn đã viết) ---
    @Test
    void testTC1_AllNominalValues() {
        assertEquals(10500000.0, calculator.calculatePremium(40, 22.0, false, false));
    }

    @ParameterizedTest
    @CsvSource({
            "18, 8550000.0", "19, 8550000.0",
            "64, 12500000.0", "65, 12500000.0"
    })
    void testTC2_to_TC5_AgeBoundaries(int age, double expectedPremium) {
        assertEquals(expectedPremium, calculator.calculatePremium(age, 22.0, false, false));
    }

    @ParameterizedTest
    @CsvSource({
            "10.0, 12500000.0", "10.1, 12500000.0",
            "49.9, 14000000.0", "50.0, 14000000.0"
    })
    void testTC6_to_TC9_BmiBoundaries(double bmi, double expectedPremium) {
        assertEquals(expectedPremium, calculator.calculatePremium(40, bmi, false, false));
    }

    @Test
    void testTC10_R1_BothRisksRejected() {
        assertThrows(InsuranceRejectedException.class, () -> {
            calculator.calculatePremium(30, 22.0, true, true);
        });
    }

    @ParameterizedTest
    @CsvSource({
            "30, 22.0, true,  false, 12500000.0",
            "30, 22.0, false, true,  13000000.0",
            "22, 22.0, false, false, 8550000.0",
            "22, 28.0, false, false, 11500000.0",
            "30, 22.0, false, false, 10500000.0",
            "30, 28.0, false, false, 12500000.0"
    })
    void testTC11_to_TC16_DecisionTableRules(int age, double bmi, boolean smoker, boolean chronic, double expectedPremium) {
        assertEquals(expectedPremium, calculator.calculatePremium(age, bmi, smoker, chronic));
    }

    // --- KỸ THUẬT 3: KIỂM THỬ DÒNG DỮ LIỆU (ALL-USES) ---
    @ParameterizedTest(name = "TC All-Uses: age={0}, bmi={1}, smoker={2}, chronic={3} => expected={4}")
    @CsvSource({
            "20, 22.0, false, false, 8550000.0",   // TC17: Đi qua nhánh BMI chuẩn, kích hoạt Kim Cương
            "30, 17.0, true, false, 14500000.0",   // TC18: Đi qua nhánh phạt Tuổi 1, phạt BMI gầy, phạt Hút thuốc
            "55, 35.0, false, true, 18500000.0",   // TC19: Đi qua nhánh phạt Tuổi 2, phạt BMI béo, phạt Bệnh nền
            "20, 27.0, false, false, 11500000.0"   // TC20: BMI quá cân -> cờ isBmiNormal=false -> Không được Kim Cương
    })
    void testTC17_to_TC20_AllUsesCoverage(int age, double bmi, boolean smoker, boolean chronic, double expectedPremium) {
        assertEquals(expectedPremium, calculator.calculatePremium(age, bmi, smoker, chronic));
    }
}