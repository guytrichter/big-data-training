package org.trichter.pail.structure;

import com.backtype.hadoop.pail.PailStructure;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.trichter.pail.model.Login;

import java.util.Collections;
import java.util.List;

/**
 * Created by guy on 9/10/16.
 */
public class LoginPailStructure implements PailStructure<Login> {

    private transient final TypeToken<Login> typeToken = new TypeToken<Login>(){};

    public boolean isValidTarget(String... strings) {
        return true;
    }

    public Login deserialize(byte[] bytes) {
        String jsonStr = new String(bytes);
        return new Gson().fromJson(jsonStr, typeToken.getType());
    }

    public byte[] serialize(Login login) {
        String loginStr = new Gson().toJson(login, typeToken.getType());
        return loginStr.getBytes();
    }

    public List<String> getTarget(Login login) {
        return Collections.emptyList();
    }

    public Class getType() {
        return Login.class;
    }
}
