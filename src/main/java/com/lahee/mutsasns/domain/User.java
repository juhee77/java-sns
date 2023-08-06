package com.lahee.mutsasns.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "user")
@Builder
@SQLDelete(sql = "UPDATE sales_item SET deleted_at = datetime('now') WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; //loginId
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    @OneToOne
    private File image;


    public void updateProfileImage(File file) {
        this.image = file;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
