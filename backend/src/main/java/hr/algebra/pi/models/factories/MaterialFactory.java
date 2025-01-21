package hr.algebra.pi.models.factories;

import hr.algebra.pi.models.Material;
import hr.algebra.pi.models.MaterialType;
import hr.algebra.pi.models.User;

import java.time.LocalDate;

public class MaterialFactory {

    public static Material createDefaultMaterial(User user, MaterialType materialType) {
        Material material = new Material();
        material.setUser(user);
        material.setMaterialType(materialType);
        material.setName("Novi " + materialType.getName() + " dokument");
        material.setDescription(materialType.getName() + " dokument");
        material.setCreationDate(LocalDate.now());
        return material;
    }

    public static Material createMaterialWithTags(User user, MaterialType materialType, String tags) {
        Material material = createDefaultMaterial(user, materialType);
        material.setTags(tags);
        return material;
    }
}


