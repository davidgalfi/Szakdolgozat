package org.example.treatment.drugs.efficacies;

import org.json.simple.JSONObject;

public class EfficacyFactory {
    public static Efficacy createEfficacy(JSONObject jsonObject, String key) {

        if (jsonObject.containsKey(key)) {

            return HillFactory.createHill((JSONObject) jsonObject.get(key), (double) jsonObject.get("molarMass"));
        } else {

            return new NoEffect();
        }
    }
}
