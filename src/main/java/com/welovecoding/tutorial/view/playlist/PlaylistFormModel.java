package com.welovecoding.tutorial.view.playlist;

import com.welovecoding.tutorial.view.scaffolding.FormInput;
import com.welovecoding.tutorial.view.scaffolding.FormModel;
import com.welovecoding.tutorial.view.scaffolding.RenderType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistFormModel extends FormModel {

  public PlaylistFormModel() {
    super.PROPERTY_ORDER = new String[]{
      "author",
      "id",
      "name",
      "category",
      "difficulty",
      "description",
      "languageCode",
      "provider",
      "code",
      "videos",
      "created",
      "lastModified"
    };
  }

  @Override
  public FormInput[] parseProperties(Map<String, Class<?>> properties) {

    HashMap<String, FormInput> inputs = new HashMap<>(properties.size());
    List<FormInput> formFields = new ArrayList<>();

    // Parse each property and collect them in a map
    for (Map.Entry<String, Class<?>> property : properties.entrySet()) {
      FormInput input = new FormInput(property);

      String key = input.getKey();

      // Set read-only
      if (key.equals("id") || key.equals("created") || key.equals("lastModified")) {
        input.setReadOnly(true);
      }

      // Set render-type
      switch (key) {
        case "author":
          input.setRenderType(RenderType.DROPDOWN);
          break;
        case "category":
          input.setRenderType(RenderType.DROPDOWN);
          break;
        case "description":
          input.setRenderType(RenderType.TEXTAREA);
          break;
        case "difficulty":
          input.setRenderType(RenderType.ENUM);
          break;
        case "languageCode":
          input.setRenderType(RenderType.ENUM);
          break;
        case "provider":
          input.setRenderType(RenderType.ENUM);
          break;
        default:
          setDefaultRenderType(input);
          break;
      }

      inputs.put(key, input);
    }

    // Sort and filter properties according to "PROPERTY_ORDER"
    for (String orderedKey : PROPERTY_ORDER) {
      FormInput field = inputs.get(orderedKey);
      if (field != null) {
        formFields.add(field);
      } else {
        System.out.println("WARNING: Couldn't find a field with the key " + orderedKey);
      }
    }

    return formFields.toArray(new FormInput[]{});
  }
}
