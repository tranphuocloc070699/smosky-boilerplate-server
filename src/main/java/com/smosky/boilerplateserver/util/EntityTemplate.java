package com.smosky.boilerplateserver.util;

import com.smosky.boilerplateserver.shared.Constant;
import com.smosky.boilerplateserver.spring.dtos.EntityDto;
import com.smosky.boilerplateserver.spring.dtos.TemplateDto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EntityTemplate {

  public static void createEntityTemplate(EntityDto dto, String packagePath, String entityPath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(entityPath))) {
      CustomWriter customWriter = new CustomWriter(writer);
      List<String> defaultTypes = Constant.DEFAULT_JAVA_TYPE;
      customWriter.write(2, packagePath, ";");
      customWriter.write("import jakarta.persistence.*;");
      customWriter.write("import lombok.*;");
      customWriter.write(2, "import java.util.List;");
      customWriter.write("@Data");
      customWriter.write("@NoArgsConstructor");
      customWriter.write("@AllArgsConstructor");
      customWriter.write("@Builder");
      customWriter.write("@Entity");
      customWriter.write("@Table(name = \"tbl_", dto.getName().toLowerCase(), "\")");
      /*Class*/
      customWriter.write("public class ", dto.getName(), "{");

      String id = "";
      for (TemplateDto templateDto : dto.getTemplates()) {
        if (templateDto.getPrimary()) {
          customWriter.write("@Id");
          customWriter.write("@GeneratedValue");
          customWriter.write(2, "private ", templateDto.getType(), " ", templateDto.getName(), ";");
          id = templateDto.getName();
        } else if (!defaultTypes.contains(templateDto.getType())) {

          switch (templateDto.getType()) {
            case "OneToOne": {
              if (templateDto.getMappedBy() != null && templateDto.getMappedBy().isEmpty()) {
                customWriter.write("@", templateDto.getType(), "(", "cascade = CascadeType.ALL",
                    ")");
                customWriter.write("@JoinColumn(name = ", "\"", templateDto.getName().toLowerCase(),
                    "_", templateDto.getReferencedColumnName(), "\"", ", referencedColumnName = ",
                    "\"", templateDto.getReferencedColumnName(), "\"", ")");
              } else if (templateDto.getMappedBy() != null && templateDto.getReferencedColumnName()
                  .isEmpty()) {
                customWriter.write("@", templateDto.getType(), "(", "mappedBy = ", "\"",
                    templateDto.getMappedBy(), "\"", ")");
              }

              customWriter.write(2, "private ", templateDto.getName(), " ",
                  templateDto.getName().toLowerCase(), ";");
              break;
            }
            case "OneToMany": {
              if (!templateDto.getMappedBy().isEmpty()) {
                customWriter.write("@OneToMany(mappedBy = \"", templateDto.getMappedBy(), "\")");
                customWriter.write(2, "private ", "List<", templateDto.getName(), ">", " ",
                    templateDto.getName().toLowerCase(), "s;");
              }
              break;
            }
            case "ManyToOne": {
              if (!templateDto.getReferencedColumnName().isEmpty()) {
                customWriter.write("@ManyToOne(fetch = FetchType.EAGER)");
                customWriter.write("@JoinColumn(name=\"", templateDto.getName().toLowerCase(), "_",
                    templateDto.getReferencedColumnName(), "\")");
                customWriter.write(2, "private ", templateDto.getName(), " ",
                    templateDto.getName().toLowerCase(), ";");
              }
              break;
            }

            case "ManyToMany": {
              if (templateDto.getMappedBy() != null && templateDto.getMappedBy().isEmpty()) {
                customWriter.write(
                    "@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)");
                customWriter.write("@EqualsAndHashCode.Exclude");
                customWriter.write("@ToString.Exclude");
                String joinTableName = String.join(dto.getName().toLowerCase(), "_",
                    templateDto.getName().toLowerCase());
                String joinColumn = String.join(dto.getName().toLowerCase(), "_", id);
                String inverseJoinColumn = String.join(templateDto.getName().toLowerCase(), "_",
                    templateDto.getReferencedColumnName());
                customWriter.write("@JoinTable(name = \"", joinTableName, "\",\n",
                    "                    joinColumns = @JoinColumn(name = \"", joinColumn, "\"),\n",
                    "                    inverseJoinColumns = @JoinColumn(name = \"",
                    inverseJoinColumn, "\")\n", "                )");
              } else if (templateDto.getMappedBy() != null && templateDto.getReferencedColumnName()
                  .isEmpty()) {
                customWriter.write("@ManyToMany(mappedBy = \"", templateDto.getMappedBy(), "\")");
                customWriter.write("@EqualsAndHashCode.Exclude");
                customWriter.write("@ToString.Exclude");
              }
              customWriter.write(2, "private List<", templateDto.getName(), ">", " ",
                  templateDto.getName().toLowerCase(), "s;");
              break;
            }
            default:
              break;
          }

        } else {
          customWriter.write("@Column");
          customWriter.write(2, "private ", templateDto.getType(), " ", templateDto.getName(), ";");
        }
      }
      customWriter.write("}");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
