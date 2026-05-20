package com.example.nutritionapp.util

object NutritionCalculator {

    /**
     * Tính chỉ số khối lượng cơ thể (BMI)
     * @param weightKg Cân nặng tính bằng kg
     * @param heightCm Chiều cao tính bằng cm
     * @return BMI
     */
    fun calculateBMI(weightKg: Double, heightCm: Double): Double {
        if (heightCm <= 0.0) return 0.0
        val heightM = heightCm / 100.0
        return weightKg / (heightM * heightM)
    }

    data class BMIResult(val classification: String, val recommendation: String, val recommendedGoal: String)

    /**
     * Phân loại BMI theo chuẩn Châu Á
     */
    fun getBMIClassification(bmi: Double): BMIResult {
        return when {
            bmi < 18.5 -> BMIResult("Thiếu cân", "Bạn đang hơi gầy. Nên ăn nhiều hơn để tăng cường sức khỏe và cơ bắp.", "BUILD_MUSCLE")
            bmi < 23.0 -> BMIResult("Bình thường", "Tuyệt vời! Chỉ số của bạn rất tốt. Hãy tiếp tục duy trì chế độ ăn uống và tập luyện nhé.", "MAINTAIN")
            bmi < 25.0 -> BMIResult("Thừa cân", "Bạn đang hơi thừa cân. Nên chú ý kiểm soát lượng calo nạp vào và tăng cường vận động.", "LOSE_WEIGHT")
            else -> BMIResult("Béo phì", "Bạn đang ở mức béo phì. Cần có kế hoạch giảm cân để tránh các nguy cơ bệnh lý.", "LOSE_WEIGHT")
        }
    }

    /**
     * Tính Tỉ lệ trao đổi chất cơ bản (BMR) theo phương trình Mifflin-St Jeor
     * @param weightKg Cân nặng tính bằng kg
     * @param heightCm Chiều cao tính bằng cm
     * @param age Tuổi
     * @param isMale Xét giới tính Nam/Nữ
     */
    fun calculateBMR(weightKg: Double, heightCm: Double, age: Int, isMale: Boolean): Double {
        val baseBmr = (10.0 * weightKg) + (6.25 * heightCm) - (5.0 * age)
        return if (isMale) {
            baseBmr + 5.0
        } else {
            baseBmr - 161.0
        }
    }

    /**
     * Activity levels multipliers:
     * 1.2: Ít vận động (Sedentary)
     * 1.375: Vận động nhẹ (Lightly active)
     * 1.55: Vận động vừa phải (Moderately active)
     * 1.725: Vận động nhiều (Very active)
     * 1.9: Vận động cực nhiều (Super active)
     */
    fun calculateTDEE(bmr: Double, activityMultiplier: Double): Double {
        return bmr * activityMultiplier
    }

    /**
     * Tính toán Macro cho 3 chất cơ bản: Protein, Carbs, Fat (đơn vị: gram) 
     * Dựa trên mục tiêu calo (TDEE đã có thể hiện thặng dư hoặc thâm hụt)
     * 
     * Tỉ lệ tham khảo chung (cắt giảm hoặc duy trì):
     * - Protein: 30% tổng Calo (1g P = 4 kcal)
     * - Fat: 25% tổng Calo (1g F = 9 kcal)
     * - Carbs: 45% tổng Calo (1g C = 4 kcal)
     */
    fun calculateMacros(targetCalories: Double, proteinRatio: Double = 0.3, fatRatio: Double = 0.25): Map<String, Int> {
        val carbsRatio = 1.0 - proteinRatio - fatRatio
        
        val proteinKcal = targetCalories * proteinRatio
        val fatKcal = targetCalories * fatRatio
        val carbsKcal = targetCalories * carbsRatio
        
        return mapOf(
            "Protein" to (proteinKcal / 4.0).toInt(),
            "Fat" to (fatKcal / 9.0).toInt(),
            "Carbs" to (carbsKcal / 4.0).toInt()
        )
    }
}
