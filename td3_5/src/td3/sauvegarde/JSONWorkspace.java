package td3.sauvegarde;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;

import td3.model.Contact;
import td3.model.Group;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONWorkspace {
    protected List<Group> groups = new ArrayList();

    public JSONWorkspace(ObservableList<Group> groups) {
        groups.forEach((gr) -> {
            this.groups.add(gr);
        });
    }

    public JSONWorkspace() {
    }

    public List<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(List<Group> grs) {
        this.groups = grs;
    }

    public List<Group> fromFile(File file) throws Exception {
        return this.readJSON(file);
    }

    private List<Group> readJSON(File file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JSONWorkspace jw = null;
        new ArrayList();
        jw = (JSONWorkspace)mapper.readValue(new FileReader(file), JSONWorkspace.class);
        List<Group> grs = jw.getGroups();
        return grs;
    }

    private String toJSON() {
        String result = "{}";
        ObjectMapper mapper = new ObjectMapper();

        try {
            result = mapper.writeValueAsString(this);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public void save(File f) throws IOException {
        FileWriter fw = new FileWriter(f);
        fw.write(this.toJSON());
        fw.close();
    }
}
