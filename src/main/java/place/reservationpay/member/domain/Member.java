package place.reservationpay.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import place.reservationpay.member.constant.Gender;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name ="member")
public class Member {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String loginId;
    @Column(nullable = false)
    private String pw;
    @Column(nullable = false)
    private String birthday;
    @Column(nullable = false)
    private Gender gender;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String mobile;
    @Column(nullable = false)
    private String grade;
    @Column(nullable = false, updatable = false)
    private LocalDate createAt;

    public Member(String loginId, String pw, String birthday, Gender gender, String email, String mobile, String grade, LocalDate createAt) {
        this.loginId = loginId;
        this.pw = pw;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.grade = grade;
        this.createAt = createAt;
    }

    public Member(Long id, String loginId, String pw, String birthday, Gender gender, String email, String mobile, String grade, LocalDate createAt) {
        this.id = id;
        this.loginId = loginId;
        this.pw = pw;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.grade = grade;
        this.createAt = createAt;
    }

    // 직원 생성메소드
    public static Member createMember(
            String loginId, String pw, String birthday, Gender gender, String email, String mobile
    ){
        return new Member(
                loginId,pw,birthday,gender,email,mobile,"일반회원",LocalDate.now()
        );
    }
    // 직원 수정 메소드
    public void updateMember(String email, String mobile) {
        this.email = email;
        this.mobile = mobile;
    }
    // 직원 등급 수정 메소드
    public void changeGrade(String grade){
        this.grade = grade;
    }
    // 직원 생성메소드
    public static Member createMemberForTest(
            Long id, String loginId, String pw, String birthday, Gender gender, String email, String mobile
    ){
        return new Member(
                id, loginId,pw,birthday,gender,email,mobile,"일반회원",LocalDate.now()
        );
    }
}
