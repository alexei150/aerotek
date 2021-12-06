package com.swecor.aerotek.model.media;

import com.swecor.aerotek.model.Auditing;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media_content")
public class MediaContent extends Auditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    private Integer id;

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "path")
    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaContent that = (MediaContent) o;
        return id.equals(that.id) && name.equals(that.name) && description.equals(that.description) && path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, path);
    }
}
