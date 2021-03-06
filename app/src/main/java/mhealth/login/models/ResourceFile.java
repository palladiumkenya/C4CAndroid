package mhealth.login.models;

import java.io.Serializable;

public class ResourceFile implements Serializable {
    private String file_name;
    private String link;

    public ResourceFile(String file_name, String link) {
        this.file_name = file_name;
        this.link = link;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
