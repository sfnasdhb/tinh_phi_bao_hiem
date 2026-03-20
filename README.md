# Kiểm thử hệ thống tính tiền bảo hiểm

Dự án áp dụng các kỹ thuật kiểm thử hộp đen (Black-box Testing) để kiểm thử một module tính phí bảo hiểm phức tạp. Project được xây dựng bằng **Java** và framework kiểm thử **JUnit 5**.

## 1. Mô tả bài toán
Hệ thống tính toán phí bảo hiểm cơ bản hàng năm (Mặc định: 10,000,000 VNĐ) dựa trên 4 biến đầu vào:
- `Age`: Tuổi (18 đến 65).
- `BMI`: Chỉ số khối cơ thể (10.0 đến 50.0).
- `Smoker`: Có hút thuốc không (Boolean).
- `Chronic Disease`: Có bệnh nền không (Boolean).

**Luật nghiệp vụ:**
- Từ chối bảo hiểm (Ném ngoại lệ) nếu `Age` hoặc `BMI` ngoài phạm vi hệ thống cho phép, HOẶC nếu khách hàng VỪA hút thuốc VỪA có bệnh nền.
- Phụ phí Tuổi: `[26-45]` (+10%), `[46-65]` (+30%).
- Phụ phí BMI: `<18.5` (+15%), `[18.5-24.9]` (-5%), `[25.0-29.9]` (+15%), `>=30.0` (+30%).
- Phụ phí thói quen: Chỉ hút thuốc (+20%), Chỉ bệnh nền (+25%).
- **Chính sách Khách hàng Kim Cương:** Nếu (18 <= Tuổi <= 25) VÀ (BMI bình thường) VÀ (Không hút thuốc) VÀ (Không bệnh nền) -> Giảm 10% trên tổng phí.

---

## 2. Kỹ Thuật 1: Phân Tích Giá Trị Biên (BVA)
## 3. Kỹ Thuật 2: Bảng Quyết Định (Decision Table)
## 4. Công nghệ sử dụng
- **Ngôn ngữ:** Java 
- **Quản lý dependencies:** Maven
- **Testing Framework:** JUnit 5 (Jupiter) với Parameterized Tests.

## 5. Hướng dẫn chạy Test
Chạy bằng Terminal/Command Line**
Đảm bảo bạn đã cài đặt Maven, mở terminal tại thư mục gốc của project và chạy lệnh:
```bash
mvn clean test
