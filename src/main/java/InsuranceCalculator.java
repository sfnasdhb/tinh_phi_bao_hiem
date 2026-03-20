public class InsuranceCalculator {

    public double calculatePremium(int age, double bmi, boolean smoker, boolean hasChronicDisease) {
        // 1. Ranh giới vật lý & Bảng quyết định R1
        if (age < 18 || age > 65) {
            throw new InsuranceRejectedException("Tuổi không hợp lệ (18-65).");
        }
        if (bmi < 10.0 || bmi > 50.0) {
            throw new InsuranceRejectedException("BMI ngoài phạm vi hệ thống (10.0-50.0).");
        }
        if (smoker && hasChronicDisease) {
            throw new InsuranceRejectedException("Từ chối do rủi ro quá cao (Hút thuốc + Bệnh nền).");
        }

        double basePremium = 10000000.0; // Phí cơ bản: 10 triệu
        double premiumMultiplier = 1.0;

        // 2. Tính phụ phí Tuổi
        if (age >= 26 && age <= 45) {
            premiumMultiplier += 0.10;
        } else if (age >= 46 && age <= 65) {
            premiumMultiplier += 0.30;
        }

        // 3. Tính phụ phí BMI
        boolean isBmiNormal = false;
        if (bmi < 18.5) {
            premiumMultiplier += 0.15;
        } else if (bmi >= 18.5 && bmi < 25.0) {
            premiumMultiplier -= 0.05;
            isBmiNormal = true;
        } else if (bmi >= 25.0 && bmi < 30.0) {
            premiumMultiplier += 0.15;
        } else { // >= 30.0
            premiumMultiplier += 0.30;
        }

        // 4. Tính phụ phí rủi ro
        if (smoker) {
            premiumMultiplier += 0.20;
        } else if (hasChronicDisease) {
            premiumMultiplier += 0.25;
        }

        double totalPremium = basePremium * premiumMultiplier;

        // 5. Khách hàng Kim Cương
        boolean isYoung = (age >= 18 && age <= 25);
        if (isYoung && !smoker && !hasChronicDisease && isBmiNormal) {
            totalPremium *= 0.90; // Giảm 10%
        }

        return Math.round(totalPremium * 100.0) / 100.0;
    }
}