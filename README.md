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
Áp dụng giả định lỗi đơn (Single Fault Assumption) với công thức `4n + 1` cho 2 biến liên tục (n=2) là `Age` và `BMI`. Cố định 2 biến Boolean ở giá trị thông thường (False).
Tổng số Test Cases: 4*2 + 1 = 9.

| Test Case | Biến đang test | Age | BMI | Cố định Booleans | Kết quả kỳ vọng (VNĐ) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC1 (nom)** | Cả hai | **40** | **22.0** | False, False | 10,500,000 |
| **TC2 (min)** | `Age` | **18** | 22.0 | False, False | 8,550,000 |
| **TC3 (min+)**| `Age` | **19** | 22.0 | False, False | 8,550,000 |
| **TC4 (max-)**| `Age` | **64** | 22.0 | False, False | 12,500,000 |
| **TC5 (max)** | `Age` | **65** | 22.0 | False, False | 12,500,000 |
| **TC6 (min)** | `BMI` | 40 | **10.0** | False, False | 12,500,000 |
| **TC7 (min+)**| `BMI` | 40 | **10.1** | False, False | 12,500,000 |
| **TC8 (max-)**| `BMI` | 40 | **49.9** | False, False | 14,000,000 |
| **TC9 (max)** | `BMI` | 40 | **50.0** | False, False | 14,000,000 |

---

## 3. Kỹ Thuật 2: Bảng Quyết Định (Decision Table)
Do BVA không hiệu quả khi các biến có sự phụ thuộc logic chặt chẽ (Ví dụ: Chính sách KH Kim cương và điều kiện Từ chối bảo hiểm). Ta áp dụng kỹ thuật Bảng quyết định cho 4 điều kiện đầu vào.
Sau khi thực hiện gộp cột (Collapse Columns) các trường hợp không quan tâm, ta rút gọn từ 16 tổ hợp xuống còn 7 Test Cases cốt lõi.

| Điều kiện (Conditions) | R1 | R2 | R3 | R4 | R5 | R6 | R7 |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **C1:** Hút thuốc? | T | T | F | F | F | F | F |
| **C2:** Bệnh nền? | T | F | T | F | F | F | F |
| **C3:** Tuổi 18-25? | - | - | - | T | T | F | F |
| **C4:** BMI Bình thường? | - | - | - | T | F | T | F |
| **Hành động (Actions)** | | | | | | | |
| **A1:** Từ chối bảo hiểm | X | | | | | | |
| **A2:** Cộng 20% phí | | X | | | | | |
| **A3:** Cộng 25% phí | | | X | | | | |
| **A4:** Tính phí bình thường | | | | | X | X | X |
| **A5:** Giảm giá 10% (Kim cương)| | | | X | | | |

---

## 4. Công nghệ sử dụng
- **Ngôn ngữ:** Java 
- **Quản lý dependencies:** Maven
- **Testing Framework:** JUnit 5 (Jupiter) với Parameterized Tests.

## 5. Hướng dẫn chạy Test

Chạy bằng Terminal/Command Line**
Đảm bảo bạn đã cài đặt Maven, mở terminal tại thư mục gốc của project và chạy lệnh:
```bash
mvn clean test