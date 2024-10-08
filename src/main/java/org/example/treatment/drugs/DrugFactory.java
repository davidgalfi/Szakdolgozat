package org.example.treatment.drugs;

import org.example.treatment.drugs.efficacies.Efficacy;
import org.example.treatment.drugs.efficacies.EfficacyFactory;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DrugFactory {

    public static Drug createDrug(JSONObject jsonObject){

        String name = (String) jsonObject.getOrDefault("name", "drug");

        Map<String, Efficacy> efficacy = new HashMap<>();

        for (String key : Drug.efficacies) {

            efficacy.put(key, EfficacyFactory.createEfficacy(jsonObject, key));
        }

        return new Drug(name, efficacy);
    }
}
