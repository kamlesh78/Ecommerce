package org.ttn.ecommerce.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile_image")
public class Images {
    @Id
    @SequenceGenerator(name="image_sequence",sequenceName = "image_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "image_sequence")
    private Long id;

    @Column(name="upload_time")
    private LocalDateTime uploadedAt;

    @Column(name="file_name")
    private String name;

    @Column(name="file_type")
    private String fileType;

    @Column(name = "image", unique = false, nullable = false, length = 100000)
    private byte[] image;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity userEntity;


}
